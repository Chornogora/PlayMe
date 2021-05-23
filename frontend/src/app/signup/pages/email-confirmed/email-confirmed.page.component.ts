import {Component, OnInit} from '@angular/core';
import {NonAuthorizedPageComponent} from '../../../common/pages/non-authorized-page.component';

@Component({
  selector: 'app-email-confirmation-page',
  templateUrl: './email-confirmed.page.component.html',
  styleUrls: ['../signup-page/signup.page.component.css']
})
export class EmailConfirmedPageComponent extends NonAuthorizedPageComponent implements OnInit {
}
