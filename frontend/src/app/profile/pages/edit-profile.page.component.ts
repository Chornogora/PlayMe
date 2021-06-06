import {Component, OnInit} from '@angular/core';
import {MusicianPageComponent} from '../../common/pages/musician-page.component';
import {CookieService} from 'ngx-cookie-service';
import {UserService} from '../../common/services/user.service';
import {MusicianUpdateDto} from '../../common/dto/musician-update.dto';
import {ToastService} from '../../common/services/toast.service';
import {MusicianDto} from '../../common/dto/musician.dto';

@Component({
  selector: 'app-edit-profile-page',
  templateUrl: './edit-profile.page.component.html',
  styleUrls: ['./edit-profile.page.component.css']
})
export class EditProfilePageComponent extends MusicianPageComponent implements OnInit {

  update: MusicianUpdateDto = new MusicianUpdateDto();

  constructor(cookieService: CookieService, userService: UserService,
              private toastService: ToastService) {
    super(cookieService, userService);
    this.cookieService = cookieService;
  }

  ngOnInit(): void {
    super.ngOnInit();
    this.musicianInitializedEvent.subscribe(() => {
      this.update.email = this.musician.user.email;
      this.update.firstName = this.musician.user.firstName;
      this.update.lastName = this.musician.user.lastName;
      this.update.emailNotifications = this.musician.emailNotifications;
    });
  }

  musicianUpdatedAction(musician: MusicianDto): void {
    if (musician.user.email !== this.musician.user.email) {
      this.signOut();
      return;
    }
    this.toastService.show('Done', {
      classname: 'bg-success text-light',
      delay: 2000,
      autohide: true,
      headertext: 'Profile updated successfully'
    });
  }
}
