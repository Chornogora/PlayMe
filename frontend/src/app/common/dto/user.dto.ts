import {RoleDto} from './role.dto';

export class UserDto {
  id: string;
  login: string;
  email: string;
  role: RoleDto;
  firstName: string;
  lastName: string;
}
