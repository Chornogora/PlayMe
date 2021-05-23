import {MusicianDto} from './musician.dto';
import {MetronomeDto} from './metronome.dto';
import {RecordDto} from './record.dto';

export class RehearsalDto {
  id: string;
  startDatetime: Date;
  finishDatetime: Date;
  description: string;
  creator: MusicianDto;
  members: MusicianDto[];
  metronomes: MetronomeDto[];
  record: RecordDto;
}
