import {Component, Input} from '@angular/core';
import {BandService} from '../../../common/services/band.service';
import {MemberDto} from '../../../common/dto/member.dto';
import {BandDto} from '../../../common/dto/band.dto';
import {MusicianDto} from '../../../common/dto/musician.dto';

class NewMember {
  id: string;
  firstName: string;
  lastName: string;
  login: string;
  statusName = 'player';
}

@Component({
  selector: 'app-bands-creator',
  templateUrl: './band-creator.component.html',
  styleUrls: ['./band-creator.component.css']
})
export class BandsCreatorComponent {

  @Input() musician: MusicianDto;

  name: string;

  musicians: NewMember[] = [];

  isMemberChoice = false;

  band: BandDto;

  membersAdded = -1;

  constructor(private bandService: BandService) {
  }

  addMusician(): void {
    this.isMemberChoice = true;
  }

  deleteMusician(musician: NewMember): void {
    this.musicians = this.musicians
      .filter(current => current !== musician);
  }

  createBand(): void {
    this.bandService.createBand(this.name)
      .subscribe((band: BandDto) => {
        this.band = band;
        this.musicians
          .map(musician => new MemberDto(musician.id, musician.statusName))
          .forEach(member => this.bandService.addMember(band, member)
            .subscribe(() => this.memberAdded()));
        this.memberAdded();
      });
  }

  addMember($event: MusicianDto): void {
    this.isMemberChoice = false;
    if ($event != null) {
      const newMember = new NewMember();
      newMember.id = $event.id;
      newMember.firstName = $event.user.firstName;
      newMember.lastName = $event.user.lastName;
      newMember.login = $event.user.login;
      this.musicians.push(newMember);
    }
  }

  memberAdded(): void {
    ++this.membersAdded;
    if (this.membersAdded === this.musicians.length) {
      location.href = `band/${this.band.id}`;
    }
  }
}
