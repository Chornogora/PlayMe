import {ChangeDetectionStrategy, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {PostDto} from '../../../common/dto/post.dto';
import {UserDto} from '../../../common/dto/user.dto';
import {PostService} from '../../../common/services/post.service';
import {NewsDto} from '../../../common/dto/news.dto';
import {UserService} from '../../../common/services/user.service';
import {BandDto} from '../../../common/dto/band.dto';
import {DateService} from '../../../common/services/date.service';
import {MusicianDto} from '../../../common/dto/musician.dto';

@Component({
  selector: 'app-home-news-line',
  templateUrl: './home.news-line.component.html',
  styleUrls: ['./home.news-line.component.css'],
  changeDetection: ChangeDetectionStrategy.Default
})
export class NewsLineComponent implements OnInit {

  @ViewChild('anchor') anchor: ElementRef<HTMLElement>;

  @Input() user: UserDto;

  @Input() musician: MusicianDto;

  posts: PostDto[] = [];

  commentOpened: any = {};

  subscriptions: BandDto[] = [];

  dateService: DateService;

  endOfNews = true;

  constructor(private newsService: PostService, private userService: UserService,
              dateService: DateService) {
    this.dateService = dateService;
  }

  ngOnInit(): void {
    this.userService.getSubscriptions()
      .subscribe((subscriptions: BandDto[]) => {
        this.subscriptions = subscriptions;
        this.getNews();
      }, error => console.log(error));
  }

  getNews(): void {
    this.newsService.getNews(this.subscriptions)
      .subscribe((news: NewsDto) => {
        this.posts = news.posts;
        this.posts.forEach(post => this.commentOpened[post.id] = false);
        this.endOfNews = false;
      }, error => console.log(error));
  }

  addNews(): void {
    if (this.endOfNews) {
      return;
    }
    setTimeout(() => {
      console.log('Get records from ' + this.posts.length);
      this.newsService.getNews(this.subscriptions, this.posts[0], this.posts.length)
        .subscribe((news: NewsDto) => {
          if (this.subscriptions.length > 0 && news.posts.length === 0) {
            this.endOfNews = true;
            return;
          }
          news.posts.forEach(post => {
            this.commentOpened[post.id] = false;
            this.posts.push(post);
          });
        });
    }, 100);
  }

  openComments(post: PostDto): void {
    if (this.commentOpened[post.id]) {
      this.commentOpened[post.id] = false;
      return;
    }
    this.newsService.getComments(post);
    this.commentOpened[post.id] = true;
  }

  createComment(newCommentText: string, post: PostDto): void {
    this.newsService.addComment(newCommentText, post);
  }

  deleteComment(commentId: string, post: PostDto): void {
    this.newsService.deleteComment(commentId, post);
  }

  isLiked(post: PostDto): boolean {
    return this.newsService.isLiked(post, this.musician);
  }

  putOrUnputLike(post: PostDto): void {
    if (this.isLiked(post)) {
      this.newsService.unputLike(post);
    } else {
      this.newsService.putLike(post);
    }
  }
}
