import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MusicianDto} from '../../../common/dto/musician.dto';
import {RehearsalService} from '../../../common/services/rehearsal.service';
import {RehearsalDto} from '../../../common/dto/rehearsal.dto';
import {BandDto} from '../../../common/dto/band.dto';
import * as moment from 'moment';
import {ToastService} from '../../../common/services/toast.service';

@Component({
  selector: 'app-rehearsal-editor',
  templateUrl: './rehearsal-edit.component.html',
  styleUrls: ['./rehearsal-edit.component.css']
})
export class RehearsalEditComponent implements OnInit {

  @Input() musician: MusicianDto;

  @Input() rehearsalToUpdate: RehearsalDto;

  updatedRehearsal = {
    id: null,
    description: '',
    startDatetime: '',
    finishDatetime: '',
    members: [],
    creator: null
  };

  isBandChoice = true;

  isMemberChoice = false;

  selectedBand: BandDto;

  @Output() rehearsalUpdatedEvent = new EventEmitter<RehearsalDto>();

  @Output() backEvent = new EventEmitter<void>();

  constructor(private rehearsalService: RehearsalService,
              private toastService: ToastService) {
  }

  ngOnInit(): void {
    this.updatedRehearsal = {
      id: this.rehearsalToUpdate.id,
      description: this.rehearsalToUpdate.description,
      startDatetime: moment(this.rehearsalToUpdate.startDatetime).format('YYYY-MM-DDTHH:mm'),
      finishDatetime: moment(this.rehearsalToUpdate.finishDatetime).format('YYYY-MM-DDTHH:mm'),
      creator: this.rehearsalToUpdate.creator,
      members: this.rehearsalToUpdate.members
    };
  }

  addMusician(): void {
    this.isMemberChoice = true;
  }

  deleteMember(musician: MusicianDto): void {
    this.updatedRehearsal.members = this.updatedRehearsal.members
      .filter(current => current !== musician);
  }

  updateRehearsal(): void {
    this.rehearsalService.updateRehearsal(this.collectData())
      .subscribe((rehearsal: RehearsalDto) => {
        this.rehearsalUpdatedEvent.emit(rehearsal);
      }, error => {
        switch (error.status) {
          case 409:
            this.showError(error.error.message);
            break;
          default:
            this.showError();
        }
      });
  }

  addMember(newMember: MusicianDto): void {
    this.isMemberChoice = false;
    if (newMember != null) {
      this.updatedRehearsal.members.push(newMember);
    }
  }

  selectBand($event: BandDto): void {
    this.isBandChoice = false;
    if ($event !== null) {
      this.selectedBand = $event;
      this.updatedRehearsal.members = this.selectedBand.members
        .map(member => member.musician)
        .filter(musician => this.musician.id !== musician.id);
    }
  }

  cancelUpdating(): void {
    this.backEvent.emit();
  }

  private collectData(): RehearsalDto {
    return {
      id: this.rehearsalToUpdate.id,
      description: this.updatedRehearsal.description,
      startDatetime: new Date(this.updatedRehearsal.startDatetime),
      finishDatetime: new Date(this.updatedRehearsal.finishDatetime),
      creator: this.rehearsalToUpdate.creator,
      members: this.updatedRehearsal.members,
      metronomes: [],
      record: null
    };
  }

  showError(message?: string): void {
    this.toastService.show('Error', {
      classname: 'bg-danger text-white',
      delay: 5000,
      autohide: true,
      headertext: message ? message : 'An error occurred while updating a rehearsal.' +
        ' Check if dates entered correctly and try again'
    });
  }
}
