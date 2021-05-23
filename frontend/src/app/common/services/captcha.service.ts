import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class CaptchaService {

  constructor(private httpClient: HttpClient) {
    this.httpClient = httpClient;
  }

  getCaptcha(): any {
    return this.httpClient.get(`http://localhost:8080/signup/captcha`);
  }
}
