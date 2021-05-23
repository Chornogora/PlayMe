import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';

import {SignInPageComponent} from './pages/signin.page.component';
import {SignInComponent} from './components/signin/signin.component';
import {SignUpModule} from '../signup/signup.module';

@NgModule({
  declarations: [
    SignInComponent,
    SignInPageComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    SignUpModule
  ],
  exports: [SignInPageComponent]
})
export class SignInModule {
}
