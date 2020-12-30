import {NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import {AuthorizationService} from './services/authorization.service';
import {UserService} from './services/user.service';
import {UserRolePageComponent} from './pages/user-role-page.component';
import {HeaderComponent} from './component/header/header.component';
import {PostService} from './services/post.service';
import {DateService} from './services/date.service';
import {CommonModule} from '@angular/common';
import {BandService} from './services/band.service';
import {SubscriptionService} from './services/subscription.service';
import {EncoderService} from './services/encoder.service';
import {DeletePostComponent} from './component/delete-post-component/delete-post.component';

@NgModule({
  declarations: [
    UserRolePageComponent,
    HeaderComponent,
    DeletePostComponent
  ],
  imports: [
    HttpClientModule,
    CommonModule
  ],
  exports: [
    HeaderComponent,
    DeletePostComponent
  ],
  providers: [AuthorizationService, UserService, PostService,
    DateService, BandService, SubscriptionService, EncoderService]
})
export class AppCommonModule {
}
