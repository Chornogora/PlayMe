import {Component, OnInit} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';
import {UserService} from '../../common/services/user.service';
import {NonAuthorizedPageComponent} from '../../common/pages/non-authorized-page.component';

@Component({
  selector: 'app-signin-page',
  templateUrl: './signin.page.component.html',
  styleUrls: ['./signin.page.component.css']
})
export class SignInPageComponent extends NonAuthorizedPageComponent implements OnInit {

  constructor(protected cookieService: CookieService, protected userService: UserService) {
    super(cookieService, userService);
  }
}
