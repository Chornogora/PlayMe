import {Component, Input} from '@angular/core';
import {BandDto} from '../../../common/dto/band.dto';
import {PostService} from '../../../common/services/post.service';
import {CreatePostDto} from '../../../common/dto/create-post.dto';
import {EncoderService} from '../../../common/services/encoder.service';

@Component({
  selector: 'app-post-creator',
  templateUrl: './post-creator.component.html',
  styleUrls: ['./post-creator.component.css']
})
export class PostCreatorComponent {

  @Input() band: BandDto;

  isCreationActive = false;

  postText = '';

  postImage: string;

  postFile: string;

  constructor(private postService: PostService, private encoderService: EncoderService) {
  }

  activateCreator(): void {
    this.isCreationActive = true;
  }

  deactivateCreator(): void {
    this.isCreationActive = false;
  }

  setPostImage(files: FileList): void {
    const image = files.item(0);
    const reader = new FileReader();
    reader.onload = () => this.postImage = reader.result.toString();
    reader.readAsDataURL(image);
  }

  setPostFile(files: FileList): void {
    const file = files.item(0);
    const reader = new FileReader();
    reader.onload = () => this.postFile = reader.result.toString();
    reader.readAsDataURL(file);
  }

  createPost(): void {
    const dto: CreatePostDto = {
      text: this.postText,
      photo: this.postImage,
      file: this.postFile
    };
    this.postService.createPost(dto, this.band.id)
      .subscribe(() => {
        location.reload();
      });
  }
}
