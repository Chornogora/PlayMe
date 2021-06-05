import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {PostDto} from '../dto/post.dto';
import {BandDto} from '../dto/band.dto';
import {CommentDto} from '../dto/comment.dto';
import {CreatePostDto} from '../dto/create-post.dto';
import {MusicianDto} from '../dto/musician.dto';

@Injectable()
export class PostService {

  constructor(private httpClient: HttpClient) {
  }

  getNews(bands: BandDto[], lastPost?: PostDto, offset?: number): any {
    if (!lastPost) {
      const params = {
        bandId: bands.map(band => band.id).join(',')
      };
      return this.httpClient.get('http://localhost:8080/posts', {params, withCredentials: true});
    } else {
      const params = {
        bandId: bands.map(band => band.id).join(','),
        startsWith: new Date(lastPost.creationDatetime).toLocaleString(),
        offset: offset.toString()
      };
      return this.httpClient.get('http://localhost:8080/posts', {params, withCredentials: true});
    }
  }

  getComments(post: PostDto): void {
    this.httpClient.get(`http://localhost:8080/posts/${post.id}/comments`, {withCredentials: true})
      .subscribe((comments: CommentDto[]) => post.comments = comments,
        error => console.log(error));
  }

  addComment(newCommentText: string, post: PostDto): void {
    this.httpClient.post(`http://localhost:8080/posts/${post.id}/comments`, {text: newCommentText},
      {withCredentials: true})
      .subscribe((comment: CommentDto) => post.comments.push(comment),
        error => console.log(error));
  }

  deleteComment(commentId: string, post: PostDto): void {
    this.httpClient.delete(`http://localhost:8080/posts/${post.id}/comments/${commentId}`, {withCredentials: true})
      .subscribe(() => post.comments = post.comments.filter(comment => comment.id !== commentId),
        error => console.log(error));
  }

  createPost(dto: CreatePostDto, bandId: string): any {
    return this.httpClient.post(`http://localhost:8080/bands/${bandId}/posts`, dto, {withCredentials: true});
  }

  deletePost(post: PostDto): any {
    return this.httpClient.delete(`http://localhost:8080/posts/${post.id}`, {withCredentials: true});
  }

  isLiked(post: PostDto, musician: MusicianDto): boolean {
    const currentLike = post.likedBy
      .find(likedMusician => likedMusician.id === musician.id);
    return currentLike != null;
  }

  putLike(post: PostDto): void {
    this.httpClient.post(`http://localhost:8080/posts/${post.id}/_like`, null, {withCredentials: true})
      .subscribe((postUpdated: PostDto) => post.likedBy = postUpdated.likedBy,
        error => console.log(error));
  }

  unputLike(post: PostDto): void {
    this.httpClient.post(`http://localhost:8080/posts/${post.id}/_unlike`, null, {withCredentials: true})
      .subscribe((postUpdated: PostDto) => post.likedBy = postUpdated.likedBy,
        error => console.log(error));
  }
}
