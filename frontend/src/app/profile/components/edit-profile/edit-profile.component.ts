import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MusicianUpdateDto} from '../../../common/dto/musician-update.dto';
import {UserService} from '../../../common/services/user.service';
import {ToastService} from '../../../common/services/toast.service';
import {MusicianDto} from '../../../common/dto/musician.dto';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent {

  @Input()
  musician: MusicianDto;

  @Input()
  update: MusicianUpdateDto;

  @Output()
  musicianUpdatedEvent = new EventEmitter<MusicianDto>();

  validationIssues: string[] = [];

  error: string;

  mailChanged = false;

  constructor(private userService: UserService) {
  }

  tryUpdateMusician(): void {
    if (this.musician.user.email !== this.update.email) {
      this.mailChanged = true;
      return;
    }
  }

  updateMusician(): void {
    this.userService.updateMusician(this.update)
      .subscribe((musician: MusicianDto) => {
        this.musicianUpdatedEvent.emit(musician);
      }, error => this.error = error.message);
  }

  closeNotification(): void {
    this.mailChanged = false;
  }
}
