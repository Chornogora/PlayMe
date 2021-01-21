import {Output, EventEmitter, Injectable} from '@angular/core';
import {CompatClient, Stomp} from '@stomp/stompjs';
import {CabinetMessage} from '../dto/socket-message/cabinet-message.dto';
import {ErrorMessage} from '../dto/socket-message/error-message.dto';
import * as SockJS from 'sockjs-client';
import {RehearsalMemberDto} from '../dto/rehearsal-member.dto';

@Injectable()
export class SocketConnector {

  private sessionId: string;

  readonly rehearsalId: string;

  readonly musicianId: string;

  private stompClient: CompatClient;

  @Output() messageEvent = new EventEmitter<CabinetMessage>();

  @Output() errorEvent = new EventEmitter<ErrorMessage>();

  @Output() rehearsalStateChangedEvent = new EventEmitter<string>();

  subscriptionFunction = (() => {
    this.subscribeOnMessageEvent();
    this.subscribeOnRehearsalStateChangeEvent();
    this.subscribeOnError();
    this.stompClient.send('/app/connect', {},
      JSON.stringify({rehearsalId: this.rehearsalId, musicianId: this.musicianId}));
  });

  constructor(address: string, rehearsalId: string, musicianId: string) {
    this.musicianId = musicianId;
    this.rehearsalId = rehearsalId;
    const ws = new SockJS(address);
    this.stompClient = Stomp.over(ws);
    this.stompClient.connect({address}, this.subscriptionFunction);
  }

  switchMicrophone(sessionId: string): void {
    this.stompClient.send('/app/switch-microphone', {},
      sessionId);
  }

  startCountdown(): void {
    this.stompClient.send('/app/start-countdown', {},
      this.rehearsalId);
  }

  private subscribeOnMessageEvent(): void {
    this.stompClient.subscribe(`/cabinet/${this.rehearsalId}/state`, (message) => {
      const outMessage = new CabinetMessage();
      outMessage.title = message.command;
      if (message.body) {
        outMessage.content = JSON.parse(message.body);
      }
      if (!this.sessionId) {
        this.initSessionId(outMessage);
      }
      this.messageEvent.emit(outMessage);
    });
  }

  private subscribeOnRehearsalStateChangeEvent(): void {
    this.stompClient.subscribe(`/cabinet/rehearsal/${this.rehearsalId}/state`, message => {
      this.rehearsalStateChangedEvent.emit(message.body);
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
