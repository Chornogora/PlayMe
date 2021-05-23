import {Injectable} from '@angular/core';

@Injectable()
export class AudioService {

  audioContext;

  play(signal: Uint8Array): void {
    if (!this.audioContext) {
      this.audioContext = new AudioContext();
    }
    const arrayBuffer = new ArrayBuffer(signal.length);
    const bufferView = new Uint8Array(arrayBuffer);
    for (let i = 0; i < signal.length; i++) {
      bufferView[i] = signal[i];
    }

    this.audioContext.decodeAudioData(arrayBuffer)
      .then((buffer) => {
        this.playAudio(buffer);
      })
      .catch(error => {
        console.log(error);
      });
  }

  private playAudio(buffer: AudioBuffer): void {
    const source = this.audioContext.createBufferSource();
    source.buffer = buffer;
    source.connect(this.audioContext.destination);
    source.start(0);
  }
}
