import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {UserDto} from '../dto/user.dto';
import {MusicianDto} from '../dto/musician.dto';
import {MusicianUpdateDto} from '../dto/musician-update.dto';

@Injectable()
export class UserService {

  constructor(private httpClient: HttpClient) {
  }

  getMyself(): any {
    return this.httpClient.get('http://localhost:8080/me', {withCredentials: true});
  }

  getMusician(): any {
    return this.httpClient.get('http://localhost:8080/me/musician', {withCredentials: true});
  }

  getMusicians(userData: UserDto): any {
    const params = {
      login: userData.login ? userData.login : '',
      firstName: userData.firstName ? userData.firstName : '',
      lastName: userData.lastName ? userData.lastName : ''
    };
    return this.httpClient.get('http://localhost:8080/musicians', {params, withCredentials: true});
  }

  getSubscriptions(): any {
    return this.httpClient.get('http://localhost:8080/subscriptions', {withCredentials: true});
  }

  updateMusician(musician: MusicianUpdateDto): any {
    return this.httpClient.patch('http://localhost:8080/user', musician, {withCredentials: true});
  }
}
