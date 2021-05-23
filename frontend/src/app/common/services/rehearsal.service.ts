import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {RehearsalDto} from '../dto/rehearsal.dto';

@Injectable()
export class RehearsalService {

  constructor(private httpClient: HttpClient) {
    this.httpClient = httpClient;
  }

  getRehearsals(): any {
    return this.httpClient.get('http://localhost:8080/rehearsals', {withCredentials: true});
  }

  createRehearsal(newRehearsal: RehearsalDto): any {
    const param = {
      description: newRehearsal.description,
      startDatetime: new Date(newRehearsal.startDatetime).toISOString(),
      finishDatetime: new Date(newRehearsal.finishDatetime).toISOString(),
      membersId: newRehearsal.members
        .map(musician => musician.id)
    };
    return this.httpClient.post('http://localhost:8080/rehearsals', param, {withCredentials: true});
  }

  deleteRehearsal(rehearsal: RehearsalDto): any {
    return this.httpClient.delete(`http://localhost:8080/rehearsals/${rehearsal.id}`,
      {withCredentials: true});
  }

  updateRehearsal(updatedRehearsal: RehearsalDto): any {
    const param = {
      id: updatedRehearsal.id,
      description: updatedRehearsal.description,
      startDatetime: new Date(updatedRehearsal.startDatetime).toISOString(),
      finishDatetime: new Date(updatedRehearsal.finishDatetime).toISOString(),
      membersId: updatedRehearsal.members
        .map(musician => musician.id)
    };
    return this.httpClient.put(`http://localhost:8080/rehearsals/`, param,
      {withCredentials: true});
  }

  getById(rehearsalId: string): any {
    return this.httpClient.get(`http://localhost:8080/rehearsals/${rehearsalId}`,
      {withCredentials: true});
  }
}
