import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {MusicianDto} from '../dto/musician.dto';
import {BandDto} from '../dto/band.dto';
import {MemberDto} from '../dto/member.dto';
import {MembershipDto} from '../dto/membership.dto';

@Injectable()
export class BandService {

  constructor(private httpClient: HttpClient) {
    this.httpClient = httpClient;
  }

  getBand(id: string): any {
    return this.httpClient.get(`http://localhost:8080/bands/${id}`, {withCredentials: true});
  }

  getBands(name: string): any {
    const params = {
      name
    };
    return this.httpClient.get('http://localhost:8080/bands', {params, withCredentials: true});
  }

  isMemberOf(musician: MusicianDto, band: BandDto): boolean {
    return musician.memberships
      .filter(membership => membership.status.name !== 'subscriber')
      .map(membership => membership.id.bandId)
      .filter(bandId => bandId === band.id)
      .length > 0;
  }

  isLeader(musician: MusicianDto, band: BandDto): boolean {
    return band.members
      .filter(membership => membership.id.musicianId === musician.id)
      .filter(membership => membership.status.name === 'leader')
      .length > 0;
  }

  isAdministrator(musician: MusicianDto, band: BandDto): boolean {
    return band.members
      .filter(membership => membership.id.musicianId === musician.id)
      .filter(membership => membership.status.name === 'leader'
        || membership.status.name === 'administrator')
      .length > 0;
  }

  createBand(name: string): any {
    const params = {
      name
    };
    return this.httpClient.post('http://localhost:8080/bands', params, {withCredentials: true});
  }

  addMember(band: BandDto, musician: MemberDto): any {
    return this.httpClient.post(`http://localhost:8080/bands/${band.id}/members`, musician,
      {withCredentials: true});
  }

  updateBand(band: BandDto): any {
    return this.httpClient.patch(`http://localhost:8080/bands/${band.id}`, {name: band.name, logo: band.logo},
      {withCredentials: true});
  }

  deleteMember(band: BandDto, member: MembershipDto): any {
    return this.httpClient.delete(`http://localhost:8080/bands/${band.id}/members/${member.musician.id}`,
      {withCredentials: true});
  }

  updateMember(band: BandDto, member: MemberDto): any {
    return this.httpClient.put(`http://localhost:8080/bands/${band.id}/members`, member,
      {withCredentials: true});
  }
}
