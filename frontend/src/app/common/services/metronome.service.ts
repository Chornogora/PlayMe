import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class MetronomeService {

  constructor(private httpClient: HttpClient) {
    this.httpClient = httpClient;
  }

  addMetronome(rehearsalId: string, tempo: number): any {
    return this.httpClient.post(`http://localhost:8080/rehearsals/${rehearsalId}/metronome`, {tempo},
      {withCredentials: true});
  }

  deleteMetronome(id: string): any {
    return this.httpClient.delete(`http://localhost:8080/rehearsals/metronome/${id}`, {withCredentials: true});
  }

  updateMetronome(id: string, tempo: number): any {
    return this.httpClient.put(`http://localhost:8080/rehearsals/metronome/${id}`, {tempo}, {withCredentials: true});
  }
}
