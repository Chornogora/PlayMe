import {MusicianDto} from './musician.dto';
import {MetronomeDto} from './metronome.dto';

export class RehearsalDto {
  id: string;
  startDatetime: Date;
  finishDatetime: Date;
  description: string;
  creator: MusicianDto;
  members: MusicianDto[];
  metronomes: MetronomeDto[];
}
