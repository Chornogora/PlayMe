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
import {RecordService} from '../../common/services/record.service';
import {CabinetDto} from '../../common/dto/cabinet.dto';
import {RecordDto} from '../../common/dto/record.dto';
// @ts-ignore
import Timeout = NodeJS.Timeout;

@Component({
  selector: 'app-cabinet-page',
  templateUrl: './cabinet.page.component.html',
  styleUrls: ['./cabinet.page.component.css'],
  providers: []
})
export class CabinetPageComponent extends MusicianPageComponent implements OnInit {

  readonly ADDRESS = 'http://localhost:8080/connect';

  eventsSubject = new Subject<any>();

  updateTracksSubject = new Subject<RecordDto>();

  rehearsal: RehearsalDto;

  members: RehearsalMemberDto[];

  record: RecordDto;

  socketConnector: SocketConnector;

  recordService: RecordService;

  metronomeInterval: Timeout;

  metronomeConfiguration = new MetronomeConfiguration();

  alreadyConnected = false;

  rehearsalState;

  countdownText = '';

  constructor(protected cookieService: CookieService, protected userService: UserService,
              private rehearsalService: RehearsalService, private audioService: AudioService,
              recordService: RecordService, private route: ActivatedRoute) {
    super(cookieService, userService);
    this.recordService = recordService;
  }

  ngOnInit(): void {
    super.ngOnInit();
    navigator.mediaDevices.getUserMedia({audio: true})
      .then(() => {
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
      })
      .catch((error) => {
        console.log(error);
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

  switchMetronome(): void {
    if (this.metronomeConfiguration && this.metronomeConfiguration.metronomeId &&
      this.rehearsal.metronomes && this.rehearsal.metronomes.length) {
      if (this.metronomeConfiguration.enabled) {
        this.disableMetronome();
      } else {
        this.enableMetronome(this.metronomeConfiguration.metronomeId);
      }
    }
  }

  enableMetronome($event: string): void {
    const newConfig = {enabled: true, metronomeId: $event};
    if (!this.metronomeConfiguration.equals(newConfig)) {
      this.socketConnector.changeMetronome(newConfig);
    }
  }

  disableMetronome(): void {
    if (this.metronomeConfiguration.enabled) {
      const newConfig = {enabled: false, metronomeId: this.metronomeConfiguration.metronomeId};
      this.socketConnector.changeMetronome(newConfig);
    }
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

  switchPinnedStatus(dto: { musician: MusicianDto, pinned: boolean }): void {
    this.socketConnector.switchPinnedStatus(dto);
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
    this.socketConnector.personalCabinetUpdateEvent
      .subscribe((message: CabinetMessage) => this.setRecord(message.content));
  }

  private updateCabinet(cabinet: CabinetDto): void {
    this.members = cabinet.members;
    this.updateMicrophoneState(cabinet);
    if (!this.metronomeConfiguration.equals(cabinet.metronomeConfiguration)) {
      this.metronomeConfiguration = new MetronomeConfiguration();
      this.metronomeConfiguration.enabled = cabinet.metronomeConfiguration.enabled;
      this.metronomeConfiguration.metronomeId = cabinet.metronomeConfiguration.metronomeId;
    }
    if (!this.rehearsalState) {
      this.setRecord(cabinet);
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
        this.startRehearsal();
        this.updateTracksSubject.next(null);
        break;
      case 'STOPPED':
        if (this.rehearsalState !== 'STOPPED') {
          this.socketConnector.askForCabinet();
        }
        this.rehearsalState = 'STOPPED';
        this.recordService.stopRecording();
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

  private startRehearsal(): void {
    this.rehearsalState = 'STARTED';
    if (!this.recordService.initialized) {
      this.recordService.init(this.socketConnector);
    }
    const currentMember = this.members
      .filter(member => member.musician.id === this.musician.id)[0];
    if (currentMember.microphoneStatus !== 'MUTED') {
      this.recordService.startRecording(currentMember.microphoneStatus === 'OFF');
    }
  }

  private updateMicrophoneState(cabinet: CabinetDto): void {
    const currentMember = cabinet.members
      .filter(member => member.musician.id === this.musician.id)[0];
    if (currentMember.microphoneStatus === 'ON') {
      this.recordService.unmuteRecording();
    } else if (currentMember.microphoneStatus === 'OFF') {
      this.recordService.muteRecording();
    }
  }

  private setRecord(cabinet: CabinetDto): void {
    if (cabinet.rehearsal.record && cabinet.rehearsal.record.tracks) {
      cabinet.rehearsal.record.tracks.forEach(track => {
        track.musician = cabinet.members
          .filter(member => member.musician.id === track.musician.id)[0]
          .musician;
      });
    }
    this.record = cabinet.rehearsal.record;
    this.updateTracksSubject.next(this.record);
  }
}
