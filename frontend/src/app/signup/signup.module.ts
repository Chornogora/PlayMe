import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {SignUpPageComponent} from './pages/signup-page/signup.page.component';
import {SignUpComponent} from './components/signup/signup.component';
import {EmailConfirmedPageComponent} from './pages/email-confirmed/email-confirmed.page.component';
import {CaptchaComponent} from './components/captcha/captcha.component';

@NgModule({
  declarations: [
    SignUpComponent,
    SignUpPageComponent,
    EmailConfirmedPageComponent,
    CaptchaComponent
  ],
  imports: [
    BrowserModule,
    FormsModule
  ],
  exports: [SignUpPageComponent, EmailConfirmedPageComponent]
})
export class SignUpModule {
}
