import {MusicianPageComponent} from '../../common/pages/musician-page.component';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {CookieService} from 'ngx-cookie-service';
import {UserService} from '../../common/services/user.service';
import {SocketConnector} from '../../common/services/socket-connector';
import {CabinetMessage} from '../../common/dto/socket-message/cabinet-message.dto';
import {RehearsalDto} from '../../common/dto/rehearsal.dto';
import {RehearsalService} from '../../common/services/rehearsal.service';
import {RehearsalMemberDto} from '../../common/dto/rehearsal-member.dto';
import {ErrorMessage} from '../../common/dto/socket-message/error-message.dto';
import {MusicianDto} from '../../common/dto/musician.dto';

@Component({
  selector: 'app-cabinet-page',
  templateUrl: './cabinet.page.component.html',
  styleUrls: ['./cabinet.page.component.css']
})
export class CabinetPageComponent extends MusicianPageComponent implements OnInit {

  readonly ADDRESS = 'http://localhost:8080/connect';

  rehearsal: RehearsalDto;

  members: RehearsalMemberDto[];

  socketConnector: SocketConnector;

  alreadyConnected = false;

  rehearsalState = 'STOPPED';

  countdownText = '';

  constructor(protected cookieService: CookieService, protected userService: UserService,
              private rehearsalService: RehearsalService, private route: ActivatedRoute) {
    super(cookieService, userService);
  }

  ngOnInit(): void {
    super.ngOnInit();
    const rehearsalId = this.route.snapshot.paramMap.get('id');
    this.rehearsalService.getById(rehearsalId)
      .subscribe(rehearsal => {
        this.rehearsal = rehearsal;
        if (this.musician) {
          this.initializeSocketConnection();
        } else {
          this.musicianInitializedEvent.subscribe(() => this.initializeSocketConnection());
        }
      });
  }

  switchMicrophone(sessionId: string): void {
    this.socketConnector.switchMicrophone(sessionId);
  }

  exit(): void {
    location.href = 'rehearsals';
  }

  sendStartCountdownCommand(): void {
    this.socketConnector.startCountdown();
  }

  isCreator(musician: MusicianDto): boolean {
    return this.rehearsal && this.rehearsal.creator.id === musician.id;
  }

  private initializeSocketConnection(): void {
    this.socketConnector = new SocketConnector(this.ADDRESS, this.rehearsal.id, this.musician.id);
    this.socketConnector.messageEvent
      .subscribe((message: CabinetMessage) => this.updateCabinet(message.content));
    this.socketConnector.errorEvent
      .subscribe((message: ErrorMessage) => this.processError(message));
    this.socketConnector.rehearsalStateChangedEvent
      .subscribe((state: string) => this.rehearsalStateChanged(state));
  }

  private updateCabinet(cabinet: any): void {
    this.members = cabinet.members;
  }

  private rehearsalStateChanged(state: string): void {
    switch (state) {
      case 'COUNTDOWN':
        this.startCountdown();
        break;
    }
  }

  private processError(message: ErrorMessage): void {
    switch (message.name) {
      case 'already-connected':
        if (!this.members) {
          this.alreadyConnected = true;
        }
    }
  }

  private startCountdown(): void {
    if (this.countdownText === '') {
      this.countdownText = '5';
      this.rehearsalState = 'COUNTDOWN';
    }
    const interval = setInterval(() => {
      const count = parseInt(this.countdownText, 36);
      this.countdownText = (count === 1) ? '' : (count - 1).toString();
      if (count === 1) {
        clearInterval(interval);
        this.rehearsalState = 'STARTED';
      }
    }, 1000);
  }
}
