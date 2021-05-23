import {NgModule} from '@angular/core';
import {RehearsalsPageComponent} from './pages/rehearsals-page/rehearsals.page.component';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {AppCommonModule} from '../common/app-common.module';
import {RehearsalCreatorComponent} from './components/rehearsal-creator/rehearsal-creator.component';
import {BandsModule} from '../bands/bands.module';
import {RehearsalListComponent} from './components/rehearsal-list/rehearsal-list.component';
import {RehearsalEditComponent} from './components/rehersal-edit/rehearsal-edit.component';

@NgModule({
  declarations: [
    RehearsalsPageComponent,
    RehearsalCreatorComponent,
    RehearsalListComponent,
    RehearsalEditComponent],
  imports: [
    BrowserModule,
    FormsModule,
    AppCommonModule,
    BandsModule
  ],
  exports: [RehearsalsPageComponent]
})
export class RehearsalsModule {
}
