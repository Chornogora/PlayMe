import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NotificationDto} from '../dto/notification.dto';

@Injectable()
export class NotificationService {

  constructor(private httpClient: HttpClient) {
    this.httpClient = httpClient;
  }

  getNotifications(): any {
    return this.httpClient.get(`http://localhost:8080/notifications/`, {withCredentials: true});
  }

  readNotification(notification: NotificationDto): void {
    this.httpClient.post(`http://localhost:8080/notifications/_read`, notification, {withCredentials: true})
      .subscribe(() => {
      }, error => console.log(error));
  }
}
