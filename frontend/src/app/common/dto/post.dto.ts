import {BandDto} from './band.dto';
import {CommentDto} from './comment.dto';

export class PostDto {
  id: string;
  photoURL: string;
  fileURL: string;
  text: string;
  creationDatetime: Date;
  band: BandDto;
  comments: CommentDto[];
}
