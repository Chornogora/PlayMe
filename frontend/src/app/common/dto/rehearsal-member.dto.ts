import {MusicianDto} from './musician.dto';

export class RehearsalMemberDto {
  sessionId: string;
  musician: MusicianDto;
  microphoneEnabled: boolean;
}
