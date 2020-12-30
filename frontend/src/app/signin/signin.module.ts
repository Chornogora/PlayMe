import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';

import {SignInPageComponent} from './pages/signin.page.component';
import {SignInComponent} from './components/signin/signin.component';

@NgModule({
  declarations: [
    SignInComponent,
    SignInPageComponent
  ],
  imports: [
    BrowserModule,
    FormsModule
  ],
  exports: [SignInPageComponent]
})
export class SignInModule {
}
