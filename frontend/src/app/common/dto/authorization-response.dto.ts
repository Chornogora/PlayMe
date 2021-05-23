import {Injectable} from '@angular/core';

@Injectable()
export class AuthorizationResponse {
  login: string;
  role: string;
  token: string;

  constructor(login: string, role: string, token: string) {
    this.login = login;
    this.role = role;
    this.token = token;
  }
}
