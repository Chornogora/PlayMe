import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {AppCommonModule} from '../common/app-common.module';
import {BandsPageComponent} from './pages/bands-page/bands.page.component';
import {BandsListComponent} from './component/band-list/band-list.component';
import {BandsCreatorComponent} from './component/band-creator/band-creator.component';
import {BandPageComponent} from './pages/band-page/band.page.component';
import {PostCreatorComponent} from './component/post-creator/post-creator.component';
import {BandPostsComponent} from './component/band-posts/band-posts.component';
import {HomeModule} from '../home/home.module';
import {BandEditPageComponent} from './pages/band-edit-page/band-edit.page.component';
import {AbstractBandPageComponent} from './pages/abstract-band-page/abstract-band.page.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations:
    [
      AbstractBandPageComponent,
      BandPageComponent,
      BandEditPageComponent,
      BandsPageComponent,
      BandsListComponent,
      BandsCreatorComponent,
      PostCreatorComponent,
      BandPostsComponent
    ],
  imports:
    [
      BrowserModule,
      FormsModule,
      AppCommonModule,
      HomeModule,
      NgbModule
    ],
  exports: [BandsPageComponent, BandPageComponent]
})
export class BandsModule {
}
