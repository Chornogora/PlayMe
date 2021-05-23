import {MusicianDto} from './musician.dto';

export class RehearsalMemberDto {
  sessionId: string;
  musician: MusicianDto;
  microphoneStatus: 'ON' | 'OFF' | 'MUTED';
}
