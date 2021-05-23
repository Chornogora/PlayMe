import {Component} from '@angular/core';
import {AuthorizationService} from '../../../common/services/authorization.service';
import {AuthorizationData} from '../../../common/dto/authorization-data.dto';
import {AuthorizationResponse} from '../../../common/dto/authorization-response.dto';

@Component({
  selector: 'app-signin-form',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css'],
  providers: [AuthorizationService]
})
export class SignInComponent {

  authorizationData: AuthorizationData = new AuthorizationData('', '');

  error: string;

  constructor(private authorizationService: AuthorizationService) {
  }

  authorize(): void {
    this.authorizationService.authorize(this.authorizationData)
      .subscribe((response: AuthorizationResponse) => {
          switch (response.role) {
            case 'administrator' :
              document.location.href = 'http://localhost:8080/admin/users';
              break;
            default:
              document.location.href = '';
          }
        },
        error => this.error = error.error.message);
  }
}
