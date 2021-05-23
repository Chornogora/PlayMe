import {Injectable} from '@angular/core';

@Injectable()
export class DateService {

  private static months = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December'
  ];

  private static getTodayStart(): Date {
    const now = new Date();
    now.setHours(0);
    now.setMinutes(0);
    now.setSeconds(0);
    now.setMilliseconds(0);
    return now;
  }

  private static getTime(date: Date): string {
    const minutes = date.getMinutes().toString().length === 1 ? `0${date.getMinutes()}` : date.getMinutes();
    const seconds = date.getSeconds().toString().length === 1 ? `0${date.getSeconds()}` : date.getSeconds();
    return `${date.getHours()}:${minutes}:${seconds}`;
  }

  private static getDay(date: Date): string {
    const todayStart = DateService.getTodayStart();
    if (date > todayStart && date.getDate() === todayStart.getDate()) {
      return 'today';
    }
    return date.getDate() + ' ' + this.months[date.getMonth()] + ' ' + date.getFullYear();
  }

  formatDate(date: Date): string {
    const ioDate = new Date(date);
    const day = DateService.getDay(ioDate);
    const time = DateService.getTime(ioDate);
    return `${day} at ${time}`;
  }

  formatDateOnly(date: Date): string {
    const ioDate = new Date(date);
    return DateService.getDay(ioDate);
  }
}
