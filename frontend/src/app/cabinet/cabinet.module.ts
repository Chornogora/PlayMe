import {NgModule} from '@angular/core';
import {CabinetPageComponent} from './pages/cabinet.page.component';
import {BrowserModule} from '@angular/platform-browser';
import {BandsModule} from '../bands/bands.module';
import {AppCommonModule} from '../common/app-common.module';
import {FormsModule} from '@angular/forms';
import {MemberListComponent} from './components/member-list/member-list.component';

@NgModule({
  declarations: [
    CabinetPageComponent,
    MemberListComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppCommonModule,
    BandsModule
  ],
  exports: [CabinetPageComponent]
})
export class CabinetModule {
}
