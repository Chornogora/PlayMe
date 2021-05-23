import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RehearsalDto} from '../../../common/dto/rehearsal.dto';
import {MusicianDto} from '../../../common/dto/musician.dto';
import {Subject} from 'rxjs';
import {RecordService} from '../../../common/services/record.service';
import {RecordDto} from '../../../common/dto/record.dto';
import {RehearsalMemberDto} from '../../../common/dto/rehearsal-member.dto';

@Component({
  selector: 'app-cabinet-main-content',
  templateUrl: './main-content.component.html',
  styleUrls: ['./main-content.component.css']
})
export class MainContentComponent {

  @Input() rehearsal: RehearsalDto;

  @Input() record: RecordDto;

  @Input() musician: MusicianDto;

  @Input() eventsSubject: Subject<any>;

  @Input() updateTracksSubject: Subject<RecordDto>;

  @Input() recordService: RecordService;

  @Input() members: RehearsalMemberDto[];

  @Output() metronomeEnabledEvent = new EventEmitter<string>();

  @Output() metronomeDisabledEvent = new EventEmitter<void>();

  @Output() updateRehearsalEvent = new EventEmitter<RehearsalDto>();

  @Output() pinnedEvent = new EventEmitter<{ musician: MusicianDto, pinned: boolean }>();

  state: 'tracks' | 'metronomes' | 'description';

  constructor() {
    this.state = 'tracks';
  }

  changeState(newState: 'tracks' | 'metronomes' | 'description'): void {
    this.state = newState;
  }

  isCreator(musician: MusicianDto): boolean {
    return this.rehearsal && musician && this.rehearsal.creator.id === musician.id;
  }

  switchPinnedStatus(dto: { musician: MusicianDto, pinned: boolean }): void {
    this.pinnedEvent.emit(dto);
  }
}

