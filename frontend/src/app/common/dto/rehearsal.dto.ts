import {MusicianDto} from './musician.dto';

export class RehearsalDto {
  id: string;
  startDatetime: Date;
  finishDatetime: Date;
  description: string;
  creator: MusicianDto;
  members: MusicianDto[];
}
