import {Injectable} from '@angular/core';

@Injectable()
export class RoleDto {
  id: string;
  name: string;

  constructor(id: string, name: string) {
    this.id = id;
    this.name = name;
  }
}
