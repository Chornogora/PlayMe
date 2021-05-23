import {Component, OnInit} from '@angular/core';
import {NonAuthorizedPageComponent} from '../../../common/pages/non-authorized-page.component';

@Component({
  selector: 'app-signup-page',
  templateUrl: './signup.page.component.html',
  styleUrls: ['./signup.page.component.css']
})
export class SignUpPageComponent extends NonAuthorizedPageComponent implements OnInit {

  createdEmail: string;
}
