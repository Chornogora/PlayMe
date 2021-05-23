import {AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TrackDto} from '../../../../common/dto/track.dto';
import * as WaveSurfer from 'wavesurfer.js';
import {Observable, Subject} from 'rxjs';
import {TrackCommand} from '../../../../common/dto/track-command.dto';
import {RehearsalMemberDto} from '../../../../common/dto/rehearsal-member.dto';
import {RecordDto} from '../../../../common/dto/record.dto';

@Component({
  selector: 'app-track-component',
  templateUrl: './track.component.html',
  styleUrls: ['./track.component.css']
})
export class TrackComponent implements OnInit, AfterViewInit {

  @Input() member: RehearsalMemberDto;

  @Input() track: TrackDto;

  @Input() elementId: string;

  @Input() isCreator: boolean;

  @Input() events: Observable<TrackCommand>;

  @Input() updateTracksSubject: Subject<RecordDto>;

  wavesurfer: WaveSurfer;

  status: 'playing' | 'paused' | 'stopped' = 'stopped';

  @Output() stopPlayingEvent = new EventEmitter<void>();

  @Output() pauseEvent = new EventEmitter<void>();

  @Output() pinnedEvent = new EventEmitter<boolean>();

  constructor(private changeDetector: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.events.subscribe((message: TrackCommand) => {
      switch (message.command) {
        case 'start':
          this.playTrack();
          break;
        case 'pause':
          this.pauseTrack();
          break;
        case 'stop':
          this.stopTrack();
          break;
        case 'move':
          this.move(message.options.progress);
      }
    });
  }

  ngAfterViewInit(): void {
    this.initializeWaveSurfer(this.elementId);
    this.loadWaveSurfer(this.track.id);
  }

  playTrack(): void {
    this.status = 'playing';
    this.wavesurfer.play();
  }

  pauseTrack(): void {
    this.status = 'paused';
    this.wavesurfer.pause();
  }

  stopTrack(): void {
    this.status = 'stopped';
    this.wavesurfer.stop();
    this.changeDetector.detectChanges();
  }

  switchPinnedStatus(): void {
    const changeTo = !(this.member.microphoneStatus === 'MUTED');
    this.pinnedEvent.emit(changeTo);
  }

  pauseManually(): void {
    this.pauseTrack();
    this.pauseEvent.emit();
  }

  // Don't delete, function in use
  getPosition(): void {
    return this.wavesurfer.getCurrentTime();
  }

  private move(progress: number): void {
    this.stopTrack();
    this.wavesurfer.skip(progress);
  }

  private initializeWaveSurfer(containerId: string): void {
    this.wavesurfer = WaveSurfer.create({
      container: `#${containerId}`,
      waveColor: 'violet',
      progressColor: 'purple',
      fillParent: true,
      hideScrollbar: false
    });
    this.wavesurfer.on('finish', () => {
      this.stopPlayingEvent.emit();
    });
  }

  private loadWaveSurfer(trackId: string): void {
    this.wavesurfer.load(`/tracks/${trackId}`);
  }
}
