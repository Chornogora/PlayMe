import {Component, OnInit, Output, EventEmitter, Input, OnDestroy} from '@angular/core';
import {CaptchaService} from '../../../common/services/captcha.service';
import {CaptchaDto} from '../../../common/dto/captcha.dto';
import {Observable, Subscription} from 'rxjs';

@Component({
  selector: 'app-captcha',
  templateUrl: './captcha.component.html',
  styleUrls: ['./captcha.component.css']
})
export class CaptchaComponent implements OnInit, OnDestroy {

  image: string;

  tokenId: string;

  number;

  private eventsSubscription: Subscription;

  @Input() events: Observable<void>;

  @Output() captchaChangedEvent = new EventEmitter<any>();

  constructor(private captchaService: CaptchaService) {
  }

  ngOnInit(): void {
    this.eventsSubscription = this.events.subscribe(() => this.reload());
    this.reload();
  }

  ngOnDestroy(): void {
    this.eventsSubscription.unsubscribe();
  }

  reload(): void {
    this.captchaService.getCaptcha()
      .subscribe((captcha: CaptchaDto) => {
        this.image = 'data:image/jpeg;base64,' + captcha.image;
        this.tokenId = captcha.tokenId;
        this.number = '';
        this.sendChange();
      });
  }

  sendChange(): void {
    this.captchaChangedEvent.emit({
      tokenId: this.tokenId,
      captchaNumber: this.number
    });
  }
}
