import {Injectable, TemplateRef} from '@angular/core';

@Injectable()
export class ToastService {

  toasts: any[] = [];

  // Push new Toasts to array with content and options
  show(textOrTpl: string | TemplateRef<any>, options: any = {}): void {
    this.toasts.push({textOrTpl, ...options});
  }

  // Callback method to remove Toast DOM element from view
  remove(toast): void {
    this.toasts = this.toasts.filter(t => t !== toast);
  }
}
