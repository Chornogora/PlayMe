import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {AppCommonModule} from '../common/app-common.module';
import {IntersectionObserverModule} from '@ng-web-apis/intersection-observer';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {NotificationsPageComponent} from './pages/notifications.page.component';
import {NotificationsListComponent} from './component/notifications-list/notifications-list.component';

@NgModule({
  declarations: [
    NotificationsPageComponent,
    NotificationsListComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppCommonModule,
    IntersectionObserverModule,
    NgbModule
  ],
  exports: [NotificationsPageComponent]
})
export class NotificationsModule {
}
