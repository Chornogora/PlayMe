import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {RehearsalDto} from '../../../common/dto/rehearsal.dto';
import {MetronomeService} from '../../../common/services/metronome.service';
import {MetronomeDto} from '../../../common/dto/metronome.dto';
import {Observable, Subscription} from 'rxjs';

@Component({
  selector: 'app-metronome-form',
  templateUrl: './metronome-form.component.html',
  styleUrls: ['./metronome-form.component.css']
})
export class MetronomeFormComponent implements OnInit, OnDestroy {

  @Input() rehearsal: RehearsalDto;

  @Input() events: Observable<void>;

  currentMetronome: MetronomeDto;

  private eventsSubscription: Subscription;

  @Output() metronomeEnabledEvent = new EventEmitter<string>();

  @Output() metronomeDisabledEvent = new EventEmitter<void>();

  @Output() updateRehearsalEvent = new EventEmitter<RehearsalDto>();

  constructor(private metronomeService: MetronomeService) {
  }

  ngOnInit(): void {
    this.eventsSubscription = this.events.subscribe(event => this.handleSwitchMicrophoneEvent(event));
  }

  ngOnDestroy(): void {
    this.eventsSubscription.unsubscribe();
  }

  enableMetronome(): void {
    if (this.currentMetronome) {
      this.metronomeEnabledEvent.emit(this.currentMetronome.id);
      return;
    } else {
      this.metronomeDisabledEvent.emit();
    }
  }

  disableMetronome(): void {
    this.metronomeDisabledEvent.emit();
  }

  createMetronome(): void {
    this.metronomeService.addMetronome(this.rehearsal.id, 60)
      .subscribe((updatedRehearsal: RehearsalDto) => {
        this.updateRehearsalEvent.emit(updatedRehearsal);
        this.currentMetronome = null;
      });
  }

  updateMetronome(): void {
    this.metronomeService.updateMetronome(this.currentMetronome.id, this.currentMetronome.tempo)
      .subscribe((updatedRehearsal: RehearsalDto) => {
        this.updateRehearsalEvent.emit(updatedRehearsal);
      });
  }

  deleteMetronome(): void {
    this.metronomeService.deleteMetronome(this.currentMetronome.id)
      .subscribe((updatedRehearsal: RehearsalDto) => {
        this.updateRehearsalEvent.emit(updatedRehearsal);
        this.currentMetronome = null;
      });
  }

  handleSwitchMicrophoneEvent(metronomeNumber): void {
    if (metronomeNumber && metronomeNumber <= this.rehearsal.metronomes.length) {
      this.currentMetronome = this.rehearsal.metronomes[metronomeNumber - 1];
      this.enableMetronome();
    }
  }
}
