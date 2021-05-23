import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output, QueryList, ViewChildren} from '@angular/core';
import {RecordDto} from '../../../common/dto/record.dto';
import {Subject} from 'rxjs';
import {TrackCommand} from '../../../common/dto/track-command.dto';
import {MusicianDto} from '../../../common/dto/musician.dto';
import {RehearsalMemberDto} from '../../../common/dto/rehearsal-member.dto';
import {TrackDto} from '../../../common/dto/track.dto';

@Component({
  selector: 'app-tracks-component',
  templateUrl: './tracks.component.html',
  styleUrls: ['./tracks.component.css']
})
export class TracksComponent implements OnInit {

  @Input() record: RecordDto;

  @Input() members: RehearsalMemberDto[];

  @Input() updateTracksSubject: Subject<RecordDto>;

  @Input() isCreator: boolean;

  tracks: TrackDto[];

  @ViewChildren('track') trackViews: QueryList<any> = new QueryList<any>();

  eventsSubject = new Subject<TrackCommand>();

  playingStatus: 'stopped' | 'playing' = 'stopped';

  @Output() pinnedEvent = new EventEmitter<{ musician: MusicianDto, pinned: boolean }>();

  constructor(private changeDetector: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.updateTracksSubject
      .subscribe((record: RecordDto) => {
        if (record) {
          this.eventsSubject = new Subject<TrackCommand>();
          this.record = record;
          this.tracks = record.tracks;
        }
      });
  }

  switchStatus(): void {
    if (this.playingStatus === 'stopped') {
      this.eventsSubject.next({command: 'start'});
      this.playingStatus = 'playing';
    } else {
      this.eventsSubject.next({command: 'pause'});
      this.playingStatus = 'stopped';
    }
  }

  stopAll(): void {
    this.eventsSubject.next({command: 'stop'});
    this.playingStatus = 'stopped';
    this.changeDetector.detectChanges();
  }

  moveToEarliest(): void {
    if (this.playingStatus === 'stopped') {
      let minimalPosition = Number.MAX_SAFE_INTEGER;
      this.trackViews.forEach(track => {
        if (track.getPosition() < minimalPosition) {
          minimalPosition = track.getPosition();
        }
      });
      this.eventsSubject.next({command: 'move', options: {progress: minimalPosition}});
    }
  }

  checkTotalPause(): void {
    let allPaused = true;
    this.trackViews.forEach(track => {
      if (track.status === 'playing') {
        allPaused = false;
      }
    });
    if (allPaused) {
      this.eventsSubject.next({command: 'pause'});
      this.playingStatus = 'stopped';
    }
  }

  switchPinnedStatus(musician: MusicianDto, pinned: boolean): void {
    this.pinnedEvent.emit({musician, pinned});
  }

  getTrackMember(musician: MusicianDto): RehearsalMemberDto {
    return this.members
      .filter(member => member.musician.id === musician.id)[0];
  }
}
