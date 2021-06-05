import {BandDto} from './band.dto';
import {CommentDto} from './comment.dto';
import {FileDto} from './file.dto';
import {PhotoDto} from './photoDto';
import {MusicianDto} from './musician.dto';

export class PostDto {
  id: string;
  photos: PhotoDto[];
  files: FileDto[];
  text: string;
  creationDatetime: Date;
  band: BandDto;
  comments: CommentDto[];
  likedBy: MusicianDto[];
}
