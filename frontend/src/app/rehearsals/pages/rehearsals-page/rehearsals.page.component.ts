import {Component, OnInit} from '@angular/core';
import {RehearsalDto} from '../../../common/dto/rehearsal.dto';
import {MusicianPageComponent} from '../../../common/pages/musician-page.component';
import {CookieService} from 'ngx-cookie-service';
import {UserService} from '../../../common/services/user.service';
import {RehearsalService} from '../../../common/services/rehearsal.service';
import {ToastService} from '../../../common/services/toast.service';

@Component({
  selector: 'app-rehearsals-page',
  templateUrl: './rehearsals.page.component.html',
  styleUrls: ['./rehearsals.page.component.css']
})
export class RehearsalsPageComponent extends MusicianPageComponent implements OnInit {

  mode = 'scheduled';

  rehearsals: RehearsalDto[];

  allRehearsals: RehearsalDto[];

  rehearsalToUpdate: RehearsalDto;

  constructor(cookieService: CookieService, userService: UserService,
              private rehearsalService: RehearsalService,
              private toastService: ToastService) {
    super(cookieService, userService);
  }

  ngOnInit(): void {
    super.ngOnInit();
    this.reload();
  }

  setMode(newMode: string): void {
    this.mode = newMode;
    this.setCurrentRehearsals();
  }

  reload(): void {
    this.rehearsalService.getRehearsals()
      .subscribe(rehearsals => {
        this.allRehearsals = rehearsals;
        this.setCurrentRehearsals();
      });
  }

  setCurrentRehearsals(): void {
    const now = new Date().getTime();
    switch (this.mode) {
      case 'scheduled':
        this.rehearsals = this.allRehearsals
          .filter(rehearsal => new Date(rehearsal.finishDatetime).getTime() > now);
        break;
      case 'past':
        this.rehearsals = this.allRehearsals
          .filter(rehearsal => new Date(rehearsal.finishDatetime).getTime() < now);
    }
  }

  updateRehearsal($event: RehearsalDto): void {
    this.rehearsalToUpdate = $event;
    this.mode = 'update';
  }

  reloadRehearsals(): void {
    this.mode = 'scheduled';
    this.rehearsalToUpdate = null;
    this.reload();
  }

  cancelCreatingEvent(): void {
    this.mode = 'scheduled';
    this.reload();
  }

  deleteRehearsal($event: RehearsalDto): void {
    this.rehearsalService.deleteRehearsal($event)
      .subscribe(() => this.reload(),
        () => this.showDeleteError('Can\'t delete this rehearsal. Maybe there are some metronomes ' +
          'set up for this rehearsal. Delete them first and try again'));
  }

  rehearsalUpdated($event: RehearsalDto): void {
    this.reloadRehearsals();
    this.toastService.show('Done', {
      classname: 'bg-success text-light',
      delay: 2000,
      autohide: true,
      headertext: 'Operation completed successfully'
    });
  }

  showDeleteError(message: string): void {
    this.toastService.show('An error occur', {
      classname: 'bg-danger text-white',
      delay: 5000,
      autohide: true,
      headertext: message
    });
  }
}
