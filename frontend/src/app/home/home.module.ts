import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {HomePageComponent} from './pages/home.page.component';
import {AppCommonModule} from '../common/app-common.module';
import {NewsLineComponent} from './components/news-line/news-line.component';
import {CommentListComponent} from '../common/component/comment-list/comment-list.component';
import {IntersectionObserverModule} from '@ng-web-apis/intersection-observer';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [
    HomePageComponent,
    NewsLineComponent,
    CommentListComponent
  ],
    imports: [
        BrowserModule,
        FormsModule,
        AppCommonModule,
        IntersectionObserverModule,
        NgbModule
    ],
  exports: [HomePageComponent, CommentListComponent]
})
export class HomeModule {
}
