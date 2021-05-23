import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MusicianDto} from '../../../common/dto/musician.dto';
import {RehearsalService} from '../../../common/services/rehearsal.service';
import {RehearsalDto} from '../../../common/dto/rehearsal.dto';
import {BandDto} from '../../../common/dto/band.dto';
import {$e} from 'codelyzer/angular/styles/chars';
import {ToastService} from '../../../common/services/toast.service';

@Component({
  selector: 'app-rehearsal-creator',
  templateUrl: './rehearsal-creator.component.html',
  styleUrls: ['./rehearsal-creator.component.css']
})
export class RehearsalCreatorComponent {

  @Input() musician: MusicianDto;

  newRehearsal: RehearsalDto = {
    id: null,
    description: '',
    startDatetime: new Date(),
    finishDatetime: new Date(),
    members: [],
    creator: null,
    metronomes: [],
    record: null
  };

  isBandChoice = true;

  isMemberChoice = false;

  selectedBand: BandDto;

  @Output() rehearsalCreatedEvent = new EventEmitter<RehearsalDto>();

  @Output() cancelEvent = new EventEmitter<void>();

  constructor(private rehearsalService: RehearsalService,
              private toastService: ToastService) {
  }

  addMusician(): void {
    this.isMemberChoice = true;
  }

  deleteMember(musician: MusicianDto): void {
    this.newRehearsal.members = this.newRehearsal.members
      .filter(current => current !== musician);
  }

  createRehearsal(): void {
    this.rehearsalService.createRehearsal(this.newRehearsal)
      .subscribe((rehearsal: RehearsalDto) => {
        this.rehearsalCreatedEvent.emit(rehearsal);
      }, (error) => {
        this.showError();
      });
  }

  addMember(newMember: MusicianDto): void {
    this.isMemberChoice = false;
    if (newMember != null) {
      this.newRehearsal.members.push(newMember);
    }
  }

  selectBand($event: BandDto): void {
    this.isBandChoice = false;
    if ($event !== null) {
      this.selectedBand = $event;
      this.newRehearsal.members = this.selectedBand.members
        .map(member => member.musician)
        .filter(musician => this.musician.id !== musician.id);
    }
  }

  cancelCreating(): void {
    this.cancelEvent.emit();
  }

  showError(): void {
    this.toastService.show('Error', {
      classname: 'bg-danger text-white',
      delay: 5000,
      autohide: true,
      headertext: 'An error occurred while creating a rehearsal.' +
        ' Check if dates entered correctly and try again'
    });
  }
}
