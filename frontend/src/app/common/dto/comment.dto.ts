import {MusicianDto} from './musician.dto';

export class CommentDto {
  id: string;
  text: string;
  creationDatetime: Date;
  author: MusicianDto;
}
