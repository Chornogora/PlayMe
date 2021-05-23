import {Injectable} from '@angular/core';

@Injectable()
export class AuthorizationData {
  login: string;
  password: string;

  constructor(login: string, password: string) {
    this.login = login;
    this.password = password;
  }
}
