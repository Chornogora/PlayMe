import {Component, OnInit} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';
import {UserService} from '../../common/services/user.service';

@Component({
  selector: 'app-signin-page',
  templateUrl: './signin.page.component.html',
  styleUrls: ['./signin.page.component.css']
})
export class SignInPageComponent implements OnInit {

  constructor(private cookieService: CookieService, private userService: UserService) {
  }

  ngOnInit(): void {
    const token = this.cookieService.get('token');
    if (token) {
      this.userService.getMyself()
        .subscribe((user: any) => {
          switch (user.role.name) {
            case 'administrator':
              document.location.href = 'http://localhost:8080/admin/users';
              break;
            default:
              document.location.href = '';
          }
        }, (error) => console.log(error));
    }
  }
}
