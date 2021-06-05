import {Component} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';
import {UserService} from '../../common/services/user.service';
import {MusicianPageComponent} from '../../common/pages/musician-page.component';

@Component({
  selector: 'app-home-page',
  templateUrl: './home.page.component.html'
})
export class HomePageComponent extends MusicianPageComponent {

  constructor(cookieService: CookieService, userService: UserService) {
    super(cookieService, userService);
    this.cookieService = cookieService;
  }
}
