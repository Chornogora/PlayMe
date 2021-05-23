import {Component, OnInit} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';
import {UserService} from '../services/user.service';

@Component({template: ''})
export class NonAuthorizedPageComponent implements OnInit {

  constructor(protected cookieService: CookieService, protected userService: UserService) {
    this.cookieService = cookieService;
    this.userService = userService;
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
