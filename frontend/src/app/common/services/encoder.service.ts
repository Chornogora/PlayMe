import {Injectable} from '@angular/core';

@Injectable()
export class EncoderService {

  encodeBase64(file: File): any {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    return reader;
  }
}
