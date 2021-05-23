import {UserCreationDto} from '../dto/user-creation.dto';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class RegistrationService {

  httpClient: HttpClient;

  constructor(httpClient: HttpClient) {
    this.httpClient = httpClient;
  }

  signUp(newUser: UserCreationDto): any {
    return this.httpClient.post('http://localhost:8080/signup', newUser);
  }
}
