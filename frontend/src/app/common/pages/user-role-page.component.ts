import {Component, OnInit} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';
import {UserService} from '../services/user.service';
import {UserDto} from '../dto/user.dto';
import {RoleDto} from '../dto/role.dto';

@Component({template: ''})
export class UserRolePageComponent implements OnInit {

  constructor(protected cookieService: CookieService, protected userService: UserService) {
    this.cookieService = cookieService;
    this.userService = userService;
  }

  user: UserDto = {id: '', login: '', role: new RoleDto(' ', ' '), firstName: ' ', lastName: ' ', email: ' '};

  signOut(): void {
    this.cookieService.delete('token', '/', 'localhost');
    document.location.href = 'auth';
  }

  ngOnInit(): void {
    const token = this.cookieService.get('token');
    if (!token) {
      document.location.href = 'auth';
    } else {
      this.userService.getMyself()
        .subscribe((user: UserDto) => {
            if (user.role.name !== 'user') {
              document.location.href = 'auth';
              return;
            }
            this.user = user;
          },
          (error) => console.log(error));
    }
  }
}
