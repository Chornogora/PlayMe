import {Component} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';
import {UserService} from '../../common/services/user.service';
import {MusicianPageComponent} from '../../common/pages/musician-page.component';

@Component({
  selector: 'app-notifications-page',
  templateUrl: './notifications.page.component.html'
})
export class NotificationsPageComponent extends MusicianPageComponent {

  constructor(cookieService: CookieService, userService: UserService) {
    super(cookieService, userService);
    this.cookieService = cookieService;
  }
}
