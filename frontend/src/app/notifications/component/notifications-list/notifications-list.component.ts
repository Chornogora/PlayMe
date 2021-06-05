import {Component, OnInit} from '@angular/core';
import {NotificationService} from '../../../common/services/notification.service';
import {NotificationDto} from '../../../common/dto/notification.dto';

@Component({
  selector: 'app-notifications-list',
  templateUrl: './notifications-list.component.html',
  styleUrls: ['./notifications-list.component.css']
})
export class NotificationsListComponent implements OnInit {

  notifications: NotificationDto[];

  currentNotification: NotificationDto;

  constructor(private notificationService: NotificationService) {
  }

  ngOnInit(): void {
    this.notificationService.getNotifications()
      .subscribe((notifications: NotificationDto[]) => this.notifications = notifications,
        error => console.log(error));
  }

  openNotification(notification: NotificationDto): void {
    this.currentNotification = notification;
    if (!notification.read) {
      this.notificationService.readNotification(notification);
      notification.read = true;
    }
  }

  closeNotification(): void {
    this.currentNotification = null;
  }
}
