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

const appRoutes: Routes = [
  {path: '', component: HomePageComponent},
  {path: 'auth', component: SignInPageComponent},
  {path: 'bands', component: BandsPageComponent},
  {path: 'band/:id', component: BandPageComponent}
];

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    RouterModule.forRoot(appRoutes),
    BrowserModule,
    FormsModule,
    SignInModule,
    HomeModule,
    BandsModule,
    AppCommonModule,
    NgbModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
