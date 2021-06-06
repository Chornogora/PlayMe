import {UserDto} from './user.dto';
import {MembershipDto} from './membership.dto';

export class MusicianDto {
  id: string;
  nickname: string;
  user: UserDto;
  memberships: MembershipDto[];
  emailNotifications: boolean;
}
