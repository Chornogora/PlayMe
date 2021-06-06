import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {AppCommonModule} from '../common/app-common.module';
import {IntersectionObserverModule} from '@ng-web-apis/intersection-observer';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {EditProfilePageComponent} from './pages/edit-profile.page.component';
import {EditProfileComponent} from './components/edit-profile/edit-profile.component';

@NgModule({
  declarations: [
    EditProfilePageComponent,
    EditProfileComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppCommonModule,
    IntersectionObserverModule,
    NgbModule
  ],
  exports: [EditProfilePageComponent]
})
export class EditProfileModule {
}
