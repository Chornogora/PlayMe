import {EventEmitter, Injectable, Output} from '@angular/core';
import {Client} from '@stomp/stompjs';
import {CabinetMessage} from '../dto/socket/cabinet-message.dto';
import {ErrorMessage} from '../dto/socket/error-message.dto';
import * as SockJS from 'sockjs-client';
import {RehearsalMemberDto} from '../dto/rehearsal-member.dto';
import {MusicianDto} from '../dto/musician.dto';

@Injectable()
export class SocketConnector {

  private sessionId: string;

  readonly rehearsalId: string;

  readonly musicianId: string;

  private stompClient: Client;

  @Output() messageEvent = new EventEmitter<CabinetMessage>();

  @Output() errorEvent = new EventEmitter<ErrorMessage>();

  @Output() rehearsalStateChangedEvent = new EventEmitter<string>();

  @Output() metronomeEvent = new EventEmitter<Uint8Array>();

  @Output() personalCabinetUpdateEvent = new EventEmitter<CabinetMessage>();

  subscriptionFunction = (() => {
    this.subscribeOnMessageEvent();
    this.subscribeOnRehearsalStateChangeEvent();
    this.subscribeOnError();
    this.subscribeOnMetronomeEvent();
    this.stompClient.publish({
      destination: '/app/connect',
      body: JSON.stringify({rehearsalId: this.rehearsalId, musicianId: this.musicianId})
    });
  });

  constructor(address: string, rehearsalId: string, musicianId: string) {
    this.musicianId = musicianId;
    this.rehearsalId = rehearsalId;
    this.stompClient = new Client({
      onConnect: this.subscriptionFunction,
      webSocketFactory: () => new SockJS(address)
    });
    this.stompClient.activate();
  }

  switchMicrophone(sessionId: string): void {
    this.stompClient.publish({
      destination: '/app/switch-microphone',
      body: sessionId
    });
  }

  startCountdown(): void {
    this.stompClient.publish({
      destination: '/app/start-countdown',
      body: this.rehearsalId
    });
  }

  changeMetronome(newConfig): void {
    this.stompClient.publish({
      destination: '/app/update-metronome',
      body: JSON.stringify({rehearsalId: this.rehearsalId, metronomeConfiguration: newConfig})
    });
  }

  switchPinnedStatus(dto: { musician: MusicianDto, pinned: boolean }): void {
    this.stompClient.publish({
      destination: '/app/update-pinned-status',
      body: JSON.stringify({musicianId: dto.musician.id, pinnedStatus: dto.pinned})
    });
  }

  start(): void {
    this.stompClient.publish({
      destination: '/app/start',
      body: this.rehearsalId
    });
  }

  stopRehearsal(): void {
    this.stompClient.publish({
      destination: '/app/stop',
      body: this.rehearsalId
    });
  }

  sendAudio(recordEncoded: string, metadata: any): void {
    this.stompClient.publish({
      destination: '/app/record',
      body: recordEncoded,
      headers: metadata
    });
  }

  askForCabinet(): void {
    this.stompClient.publish({
      destination: '/app/get-cabinet'
    });
  }

  private subscribeOnMessageEvent(): void {
    this.stompClient.subscribe(`/cabinet/${this.rehearsalId}/state`, (message) => {
      const outMessage = new CabinetMessage();
      if (message.body) {
        outMessage.content = JSON.parse(message.body);
      }
      if (!this.sessionId) {
        this.initSessionId(outMessage);
        this.subscribeOnCabinetUpdate();
      }
      this.messageEvent.emit(outMessage);
    });
  }

  private subscribeOnRehearsalStateChangeEvent(): void {
    this.stompClient.subscribe(`/cabinet/rehearsal/${this.rehearsalId}/state`, message => {
      this.rehearsalStateChangedEvent.emit(message.body);
    });
  }

  private subscribeOnMetronomeEvent(): void {
    this.stompClient.subscribe(`/cabinet/${this.rehearsalId}/metronome`,
      (message) => {
        const signalAsString = message.body;
        const audioArray = Uint8Array.from(atob(signalAsString), c => c.charCodeAt(0));
        this.metronomeEvent.emit(audioArray);
      });
  }

  private subscribeOnCabinetUpdate(): void {
    this.stompClient.subscribe(`/cabinet/${this.sessionId}`,
      (message) => {
        const outMessage = new CabinetMessage();
        if (message.body) {
          outMessage.content = JSON.parse(message.body);
        }
        this.personalCabinetUpdateEvent.emit(outMessage);
      });
  }

  private subscribeOnError(): void {
    this.stompClient.subscribe(`/cabinet/${this.rehearsalId}/error/${this.musicianId}`,
      (message) => {
        const errorMessage = JSON.parse(message.body);
        this.errorEvent.emit(errorMessage);
      });
  }

  private initSessionId(cabinetMessage: CabinetMessage): void {
    this.sessionId = cabinetMessage.content
      .members
      .filter((member: RehearsalMemberDto) => member.musician.id === this.musicianId)[0]
      .sessionId;
  }
}
