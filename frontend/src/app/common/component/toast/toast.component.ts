import {ToastService} from '../../services/toast.service';
import {Component, TemplateRef} from '@angular/core';

@Component({
  selector: 'app-toasts',
  templateUrl: './toast.component.html'
})
export class ToastComponent {

  toastService: ToastService;

  constructor(toastService: ToastService) {
    this.toastService = toastService;
  }

  removeToast(toast: any): void {
    this.toastService.remove(toast);
  }

  isTemplate(toast): boolean {
    return toast.textOrTpl instanceof TemplateRef;
  }
}
