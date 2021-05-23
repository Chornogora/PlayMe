import {TrackDto} from './track.dto';

export class RecordDto {
  id: string;
  startDatetime: Date;
  finishDatetime: Date;
  tracks: TrackDto[];
}
