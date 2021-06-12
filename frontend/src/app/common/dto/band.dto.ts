import {Injectable} from '@angular/core';
import {MembershipDto} from './membership.dto';

@Injectable()
export class BandDto {
  id: string;
  name: string;
  creationDate: Date;
  members: MembershipDto[];
  bandStatus: { id: string, name: string };
  logo: string;

  constructor(id: string, name: string, creationDate: Date, members: MembershipDto[]) {
    this.id = id;
    this.name = name;
    this.creationDate = creationDate;
    this.members = members;
  }
}
