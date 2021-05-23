import {Component, Output, EventEmitter} from '@angular/core';
import {UserCreationDto} from '../../../common/dto/user-creation.dto';
import {RegistrationService} from '../../../common/services/registration.service';
import {UserDto} from '../../../common/dto/user.dto';
import {Subject} from 'rxjs';

@Component({
  selector: 'app-signup-form',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignUpComponent {

  newUser: UserCreationDto = new UserCreationDto();

  passwordConfirmation = '';

  isSubmitDisabled = true;

  validationIssues: string[] = [];

  eventsSubject: Subject<void> = new Subject<void>();

  @Output() userCreatedEvent: EventEmitter<string> = new EventEmitter();

  constructor(private registrationService: RegistrationService) {
  }

  signup(): void {
    if (this.newUser.password !== this.passwordConfirmation) {
      this.validationIssues = ['password_not_matches'];
      return;
    }
    this.validationIssues = [];
    this.isSubmitDisabled = true;
    this.registrationService.signUp(this.newUser)
      .subscribe((created: UserDto) => this.userCreatedEvent.emit(created.email),
        error => {
          this.isSubmitDisabled = false;
          this.validationIssues = error.error;
          if (this.validationIssues.includes('wrong_captcha')) {
            this.eventsSubject.next();
          }
        });
  }

  parseDate(dateString: string): Date {
    if (dateString) {
      return new Date(dateString);
    }
    return null;
  }

  updateCaptcha($event: any): void {
    this.newUser.captchaTokenId = $event.tokenId;
    this.newUser.captchaNumber = $event.captchaNumber;
  }
}
