import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {SignInModule} from './signin/signin.module';
import {AppCommonModule} from './common/app-common.module';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {Routes, RouterModule} from '@angular/router';
import {HomePageComponent} from './home/pages/home.page.component';
import {HomeModule} from './home/home.module';
import {AppComponent} from './app.component';
import {SignInPageComponent} from './signin/pages/signin.page.component';
import {BandsPageComponent} from './bands/pages/bands-page/bands.page.component';
import {BandsModule} from './bands/bands.module';
import {BandPageComponent} from './bands/pages/band-page/band.page.component';
import {BandEditPageComponent} from './bands/pages/band-edit-page/band-edit.page.component';
import {SignUpPageComponent} from './signup/pages/signup-page/signup.page.component';
import {EmailConfirmedPageComponent} from './signup/pages/email-confirmed/email-confirmed.page.component';
import {RehearsalsModule} from './rehearsals/rehearsals.module';
import {RehearsalsPageComponent} from './rehearsals/pages/rehearsals-page/rehearsals.page.component';
import {CabinetPageComponent} from './cabinet/pages/cabinet.page.component';
import {CabinetModule} from './cabinet/cabinet.module';
import {NotificationsPageComponent} from './notifications/pages/notifications.page.component';
import {NotificationsModule} from './notifications/notifications.module';
import {EditProfilePageComponent} from './profile/pages/edit-profile.page.component';
import {EditProfileModule} from './profile/profile.module';

const appRoutes: Routes = [
  {path: '', component: HomePageComponent},
  {path: 'auth', component: SignInPageComponent},
  {path: 'signup', component: SignUpPageComponent},
  {path: 'email_confirmed', component: EmailConfirmedPageComponent},
  {path: 'bands', component: BandsPageComponent},
  {path: 'band/:id', component: BandPageComponent},
  {path: 'band/:id/edit', component: BandEditPageComponent},
  {path: 'rehearsals', component: RehearsalsPageComponent},
  {path: 'rehearsal/:id', component: CabinetPageComponent},
  {path: 'notifications', component: NotificationsPageComponent},
  {path: 'profile', component: EditProfilePageComponent}
];

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    RouterModule.forRoot(appRoutes),
    BrowserModule,
    FormsModule,
    NgbModule,
    SignInModule,
    HomeModule,
    BandsModule,
    AppCommonModule,
    RehearsalsModule,
    CabinetModule,
    NotificationsModule,
    EditProfileModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
