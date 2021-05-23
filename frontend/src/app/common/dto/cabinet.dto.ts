import {RehearsalDto} from './rehearsal.dto';
import {RehearsalMemberDto} from './rehearsal-member.dto';
import {MetronomeConfiguration} from './socket/metronome-configuration.dto';

export class CabinetDto {
  rehearsal: RehearsalDto;
  members: RehearsalMemberDto[];
  rehearsalState: string;
  metronomeConfiguration: MetronomeConfiguration;
}
