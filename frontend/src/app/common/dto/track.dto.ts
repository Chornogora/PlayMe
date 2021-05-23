import {MusicianDto} from './musician.dto';

export class TrackDto {
  id: string;
  fileUrl: string;
  musician: MusicianDto;
}
