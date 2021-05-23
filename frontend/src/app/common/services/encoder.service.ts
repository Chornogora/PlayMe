import {Injectable} from '@angular/core';

@Injectable()
export class EncoderService {

  encodeBase64(blob: Blob): FileReader {
    const reader = new FileReader();
    reader.readAsDataURL(blob);
    return reader;
  }
}
