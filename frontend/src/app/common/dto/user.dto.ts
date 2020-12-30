import {RoleDto} from './role.dto';

export class UserDto {
  id: string;
  login: string;
  role: RoleDto;
  firstName: string;
  lastName: string;
}
