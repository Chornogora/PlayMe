import {ChangeDetectionStrategy, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {PostDto} from '../../../common/dto/post.dto';
import {PostService} from '../../../common/services/post.service';
import {NewsDto} from '../../../common/dto/news.dto';
import {UserService} from '../../../common/services/user.service';
import {BandDto} from '../../../common/dto/band.dto';
import {DateService} from '../../../common/services/date.service';
import {BandService} from '../../../common/services/band.service';
import {MusicianDto} from '../../../common/dto/musician.dto';

@Component({
  selector: 'app-band-posts',
  templateUrl: './band-posts.component.html',
  styleUrls: ['./band-posts.component.css'],
  changeDetection: ChangeDetectionStrategy.Default
})
export class BandPostsComponent implements OnInit {

  @ViewChild('anchor') anchor: ElementRef<HTMLElement>;

  @Input() band: BandDto;

  @Input() musician: MusicianDto;

  posts: PostDto[] = [];

  commentOpened: any = {};

  dateService: DateService;

  bandService: BandService;

  endOfNews = true;

  postToDelete: PostDto = null;

  constructor(private postService: PostService, private userService: UserService,
              dateService: DateService, bandService: BandService) {
    this.dateService = dateService;
    this.bandService = bandService;
  }

  ngOnInit(): void {
    this.getNews();
  }

  getNews(): void {
    this.postService.getNews([this.band])
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
      this.postService.getNews([this.band], this.posts[0], this.posts.length)
        .subscribe((news: NewsDto) => {
          if (news.posts.length === 0) {
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
    this.postService.getComments(post);
    this.commentOpened[post.id] = true;
  }

  createComment(newCommentText: string, post: PostDto): void {
    this.postService.addComment(newCommentText, post);
  }

  deleteComment(commentId: string, post: PostDto): void {
    this.postService.deleteComment(commentId, post);
  }

  showDeleteForm(post: PostDto): void {
    this.postToDelete = post;
  }

  cancelDeleting(): void {
    this.postToDelete = null;
  }

  deletePost(): void {
    this.postService.deletePost(this.postToDelete)
      .subscribe(() => {
        this.posts = this.posts
          .filter(currentPost => this.postToDelete !== currentPost);
        this.postToDelete = null;
      });
  }
}
