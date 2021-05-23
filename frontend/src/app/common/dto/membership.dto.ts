import {Injectable} from '@angular/core';
import {MusicianDto} from './musician.dto';

class MembershipId {
  musicianId: string;
  bandId: string;
}

class MembershipStatus {
  id: string;
  name: string;
}

@Injectable()
export class MembershipDto {
  id: MembershipId;
  musician: MusicianDto;
  status: MembershipStatus;
}
