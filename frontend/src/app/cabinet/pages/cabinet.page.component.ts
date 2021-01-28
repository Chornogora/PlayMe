import {MusicianPageComponent} from '../../common/pages/musician-page.component';
import {Component, HostListener, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {CookieService} from 'ngx-cookie-service';
import {UserService} from '../../common/services/user.service';
import {SocketConnector} from '../../common/services/socket-connector';
import {CabinetMessage} from '../../common/dto/socket/cabinet-message.dto';
import {RehearsalDto} from '../../common/dto/rehearsal.dto';
import {RehearsalService} from '../../common/services/rehearsal.service';
import {RehearsalMemberDto} from '../../common/dto/rehearsal-member.dto';
import {ErrorMessage} from '../../common/dto/socket/error-message.dto';
import {MusicianDto} from '../../common/dto/musician.dto';
import {MetronomeConfiguration} from '../../common/dto/socket/metronome-configuration.dto';
import {Subject} from 'rxjs';
import {AudioService} from '../../common/services/audio.service';
// @ts-ignore
import Timeout = NodeJS.Timeout;

@Component({
  selector: 'app-cabinet-page',
  templateUrl: './cabinet.page.component.html',
  styleUrls: ['./cabinet.page.component.css']
})
export class CabinetPageComponent extends MusicianPageComponent implements OnInit {

  readonly ADDRESS = 'http://localhost:8080/connect';

  eventsSubject = new Subject<any>();

  rehearsal: RehearsalDto;

  members: RehearsalMemberDto[];

  socketConnector: SocketConnector;

  metronomeInterval: Timeout;

  metronomeConfiguration = new MetronomeConfiguration();

  alreadyConnected = false;

  isMetronomeConfiguring = false;

  rehearsalState = 'STOPPED';

  countdownText = '';

  constructor(protected cookieService: CookieService, protected userService: UserService,
              private rehearsalService: RehearsalService, private audioService: AudioService,
              private route: ActivatedRoute) {
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

  @HostListener('window:keydown', ['$event'])
  onKeyDown(event): void {
    const metronomeNumber = parseInt(event.key, 36);
    this.eventsSubject.next(metronomeNumber);
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

  enableMetronome($event: string): void {
    const newConfig = {enabled: true, metronomeId: $event};
    if (!this.metronomeConfiguration.equals(newConfig)) {
      this.socketConnector.changeMetronome(newConfig);
    }
    this.isMetronomeConfiguring = false;
  }

  disableMetronome(): void {
    if (this.metronomeConfiguration.enabled) {
      const newConfig = {enabled: false, metronomeId: this.metronomeConfiguration.metronomeId};
      this.socketConnector.changeMetronome(newConfig);
    }
    this.isMetronomeConfiguring = false;
  }

  isCreator(musician: MusicianDto): boolean {
    return this.rehearsal && musician && this.rehearsal.creator.id === musician.id;
  }

  updateRehearsal($event: RehearsalDto): void {
    this.rehearsal = $event;
  }

  sendStopCommand(): void {
    this.socketConnector.stopRehearsal();
  }

  changeMetronomeManually(microphoneNumber): void {
    this.eventsSubject.next(microphoneNumber);
  }

  private initializeSocketConnection(): void {
    this.socketConnector = new SocketConnector(this.ADDRESS, this.rehearsal.id, this.musician.id);
    this.socketConnector.messageEvent
      .subscribe((message: CabinetMessage) => this.updateCabinet(message.content));
    this.socketConnector.errorEvent
      .subscribe((message: ErrorMessage) => this.processError(message));
    this.socketConnector.rehearsalStateChangedEvent
      .subscribe((state: string) => this.rehearsalStateChanged(state));
    this.socketConnector.metronomeEvent
      .subscribe((signal: any) => this.processMetronome(signal));
  }

  private updateCabinet(cabinet: any): void {
    this.members = cabinet.members;
    if (!this.metronomeConfiguration.equals(cabinet.metronomeConfiguration)) {
      this.metronomeConfiguration = new MetronomeConfiguration();
      this.metronomeConfiguration.enabled = cabinet.metronomeConfiguration.enabled;
      this.metronomeConfiguration.metronomeId = cabinet.metronomeConfiguration.metronomeId;
    }
    this.rehearsalStateChanged(cabinet.rehearsalState);
  }

  private rehearsalStateChanged(state: string): void {
    switch (state) {
      case 'COUNTDOWN':
        if (this.rehearsalState !== 'COUNTDOWN') {
          this.startCountdown();
        }
        break;
      case 'STARTED':
        this.rehearsalState = 'STARTED';
        break;
      case 'STOPPED':
        this.rehearsalState = 'STOPPED';
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
        if (this.isCreator(this.musician)) {
          this.socketConnector.start();
        }
      }
    }, 1000);
  }

  private processMetronome(signal: Uint8Array): void {
    this.audioService.play(signal);
  }
}
