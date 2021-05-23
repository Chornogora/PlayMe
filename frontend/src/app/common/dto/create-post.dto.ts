import {FileDto} from './file.dto';

export class CreatePostDto {
  text: string;
  files: FileDto[];
  photos: FileDto[];
}
