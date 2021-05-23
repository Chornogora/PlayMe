import {Component, EventEmitter, Input, Output} from '@angular/core';
import {UserDto} from '../../dto/user.dto';
import {UserService} from '../../services/user.service';
import {MusicianDto} from '../../dto/musician.dto';

@Component({
  selector: 'app-member-choose-component',
  templateUrl: './member-choose.component.html',
  styleUrls: ['./member-choose.component.css']
})
export class MemberChooseComponent {

  @Input() selectedMusicians: MusicianDto[];

  @Input() musician: MusicianDto;

  userData: UserDto = new UserDto();

  users: MusicianDto[] = [];

  selectedMusician: MusicianDto;

  @Output() addMemberEvent = new EventEmitter<MusicianDto>();

  constructor(private userService: UserService) {
  }

  addMember(): void {
    this.addMemberEvent.emit(this.selectedMusician);
  }

  findMusicians(): void {
    this.userService.getMusicians(this.userData)
      .subscribe((users: MusicianDto[]) => {
        this.users = users
          .filter(found => found.id !== this.musician.id)
          .filter(found => this.notSelected(found));
        this.selectedMusician = null;
      });
  }

  getSize(): number {
    if (this.users.length < 2) {
      return 2;
    } else if (this.users.length > 5) {
      return 5;
    }
    return this.users.length;
  }

  private notSelected(musician: MusicianDto): boolean {
    return this.selectedMusicians
      .filter(selected => selected.id === musician.id)
      .length === 0;
  }
}
