import {Injectable} from '@angular/core';
import {SocketConnector} from './socket-connector';
import {EncoderService} from './encoder.service';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class RecordService {

  private static readonly BITS_PER_SECOND = 128000;

  private socketConnector: SocketConnector;

  initialized = false;

  firstRecord = true;

  finished = false;

  mediaRecorder: MediaRecorder;

  constructor(private encoderService: EncoderService, private httpClient: HttpClient) {
  }

  init(socketConnector: SocketConnector): void {
    this.socketConnector = socketConnector;
    this.initialized = true;
  }

  async startRecording(muted = false): Promise<void> {
    if (!this.mediaRecorder || this.mediaRecorder.state !== 'recording') {
      const stream = await navigator.mediaDevices.getUserMedia({audio: true});
      this.mediaRecorder = new MediaRecorder(stream, {
        audioBitsPerSecond: RecordService.BITS_PER_SECOND,
        mimeType: 'audio/webm'
      });
      this.mediaRecorder.ondataavailable = ((event: BlobEvent) => {
        this.sendChunk(event.data);
      });
      if (muted) {
        this.muteRecording();
      }
      this.mediaRecorder.start(500);
    }
  }

  muteRecording(): void {
    if (this.mediaRecorder) {
      this.mediaRecorder.stream.getAudioTracks().forEach((track) => track.enabled = false);
    }
  }

  unmuteRecording(): void {
    if (this.mediaRecorder) {
      this.mediaRecorder.stream.getAudioTracks().forEach((track) => track.enabled = true);
    }
  }

  stopRecording(): void {
    if (this.mediaRecorder && this.mediaRecorder.state === 'recording') {
      this.finished = true;
      this.mediaRecorder.stop();
    }
  }

  private sendChunk(chunk: Blob): void {
    const reader = this.encoderService.encodeBase64(chunk);
    reader.onloadend = () => {
      const encodedSound = reader.result.toString();
      this.socketConnector.sendAudio(encodedSound, {
        firstRecord: this.firstRecord,
        bitsPerSecond: this.mediaRecorder.audioBitsPerSecond,
        lastRecord: this.finished
      });
      this.firstRecord = false;
      if (this.finished) {
        this.firstRecord = true;
        this.finished = false;
      }
    };
  }
}
