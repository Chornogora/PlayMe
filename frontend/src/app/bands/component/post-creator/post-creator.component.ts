import {Component, Input} from '@angular/core';
import {BandDto} from '../../../common/dto/band.dto';
import {PostService} from '../../../common/services/post.service';
import {CreatePostDto} from '../../../common/dto/create-post.dto';
import {FileDto} from '../../../common/dto/file.dto';

@Component({
  selector: 'app-post-creator',
  templateUrl: './post-creator.component.html',
  styleUrls: ['./post-creator.component.css']
})
export class PostCreatorComponent {

  @Input() band: BandDto;

  isCreationActive = false;

  postText = '';

  postImages: FileDto[] = [];

  postFiles: FileDto[] = [];

  constructor(private postService: PostService) {
  }

  activateCreator(): void {
    this.isCreationActive = true;
  }

  deactivateCreator(): void {
    this.isCreationActive = false;
  }

  setPostImage(files: FileList): void {
    this.setFile(files, this.postImages);
  }

  setPostFile(files: FileList): void {
    this.setFile(files, this.postFiles);
  }

  setFile(files: FileList, collection: any): void {
    const file = files.item(0);
    const reader = new FileReader();
    reader.onload = () => {
      const dto = new FileDto();
      dto.fileContent = reader.result.toString();
      dto.fileName = file.name;
      collection.push(dto);
    };
    reader.readAsDataURL(file);
  }

  createPost(): void {
    const dto: CreatePostDto = {
      text: this.postText,
      photos: this.postImages,
      files: this.postFiles
    };
    this.postService.createPost(dto, this.band.id)
      .subscribe(() => {
        location.reload();
      });
  }
}
