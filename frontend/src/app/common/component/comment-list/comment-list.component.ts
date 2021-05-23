import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CommentDto} from '../../dto/comment.dto';
import {DateService} from '../../services/date.service';
import {UserDto} from '../../dto/user.dto';

@Component({
  selector: 'app-comments-list',
  templateUrl: './comment-list.component.html',
  styleUrls: ['./comment-list.component.css']
})
export class CommentListComponent {

  newText: string;

  dateService: DateService;

  @Input() comments: CommentDto[];

  @Input() user: UserDto;

  @Output() commentCreationEvent = new EventEmitter<string>();

  @Output() commentDeletingEvent = new EventEmitter<string>();

  constructor(dateService: DateService) {
    this.dateService = dateService;
  }

  createComment(): void {
    if (this.newText.trim() !== '') {
      this.commentCreationEvent.emit(this.newText);
      this.newText = '';
    }
  }

  deleteComment(comment: CommentDto): void {
    this.commentDeletingEvent.emit(comment.id);
  }
}
