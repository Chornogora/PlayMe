import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AuthorizationData} from '../dto/authorization-data.dto';

@Injectable()
export class AuthorizationService {

  httpClient: HttpClient;

  constructor(httpClient: HttpClient) {
    this.httpClient = httpClient;
  }

  authorize(authorizationData: AuthorizationData): any {
    return this.httpClient.post('http://localhost:8080/auth', authorizationData, {withCredentials: true});
  }
}
