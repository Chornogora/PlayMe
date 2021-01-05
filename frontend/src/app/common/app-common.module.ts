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
import {ModalComponent} from './component/modal-component/modal.component';
import {ToastComponent} from './component/toast/toast.component';
import {NgbModule, NgbToastModule} from '@ng-bootstrap/ng-bootstrap';
import {ToastService} from './services/toast.service';

@NgModule({
  declarations: [
    UserRolePageComponent,
    HeaderComponent,
    ModalComponent,
    ToastComponent
  ],
  imports: [
    HttpClientModule,
    CommonModule,
    NgbModule
  ],
  exports: [
    HeaderComponent,
    ModalComponent,
    ToastComponent
  ],
  providers: [AuthorizationService, UserService, PostService,
    DateService, BandService, SubscriptionService, EncoderService,
    ToastService]
})
export class AppCommonModule {
}
