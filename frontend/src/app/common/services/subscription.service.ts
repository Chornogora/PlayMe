import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BandDto} from '../dto/band.dto';
import {MusicianDto} from '../dto/musician.dto';

@Injectable()
export class SubscriptionService {

  constructor(private httpClient: HttpClient) {
  }

  isSubscriberOf(musician: MusicianDto, band: BandDto): boolean {
    return musician.memberships
      .filter(membership => membership.status.name === 'subscriber')
      .map(membership => membership.id.bandId)
      .filter(bandId => bandId === band.id)
      .length > 0;
  }

  subscribe(band: BandDto): any {
    return this.httpClient.post(`http://localhost:8080/bands/${band.id}/_subscribe`,
      null, {withCredentials: true});
  }

  unsubscribe(band: BandDto): any {
    return this.httpClient.post(`http://localhost:8080/bands/${band.id}/_unsubscribe`,
      null, {withCredentials: true});
  }
}

