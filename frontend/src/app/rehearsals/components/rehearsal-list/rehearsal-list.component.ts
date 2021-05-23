import {Component, Input, Output, EventEmitter} from '@angular/core';
import {DateService} from '../../../common/services/date.service';
import {RehearsalDto} from '../../../common/dto/rehearsal.dto';
import {MusicianDto} from '../../../common/dto/musician.dto';

@Component({
  selector: 'app-rehearsal-list',
  templateUrl: './rehearsal-list.component.html',
  styleUrls: ['./rehearsal-list.component.css']
})
export class RehearsalListComponent {

  @Input() musician: MusicianDto;

  @Input() rehearsals: RehearsalDto[];

  @Input() actualRehearsals: boolean;

  dateService: DateService;

  rehearsalToDelete: RehearsalDto;

  deleteModalText: string;

  @Output() updateRehearsalEvent = new EventEmitter<RehearsalDto>();

  @Output() deleteRehearsalEvent = new EventEmitter<RehearsalDto>();

  constructor(dateService: DateService) {
    this.dateService = dateService;
  }

  editRehearsal(rehearsal: RehearsalDto): void {
    this.updateRehearsalEvent.emit(rehearsal);
  }

  deleteRehearsal(rehearsal: RehearsalDto): void {
    this.deleteModalText = (rehearsal.metronomes.length || rehearsal.record) ?
      'Metronomes or records were detected. Are you sure want to delete this rehearsal?'
      : 'Are you sure want to delete this rehearsal?';
    this.rehearsalToDelete = rehearsal;
  }

  cancelDeleting(): void {
    this.rehearsalToDelete = null;
  }

  submitDeleting(): void {
    this.deleteRehearsalEvent.emit(this.rehearsalToDelete);
    this.rehearsalToDelete = null;
  }

  canBeUpdated(rehearsal: RehearsalDto): boolean {
    return new Date(rehearsal.startDatetime) > new Date() && this.isCreator(rehearsal);
  }

  isCreator(rehearsal: RehearsalDto): boolean {
    return rehearsal.creator.id === this.musician.id;
  }
}

