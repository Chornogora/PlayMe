import {Component, Input, Output, EventEmitter} from '@angular/core';
import {RehearsalMemberDto} from '../../../common/dto/rehearsal-member.dto';
import {MusicianDto} from '../../../common/dto/musician.dto';

@Component({
  selector: 'app-rehearsal-members-list',
  templateUrl: './member-list.component.html',
  styleUrls: ['./member-list.component.css']
})
export class MemberListComponent {

  @Input() musician: MusicianDto;

  @Input() members: RehearsalMemberDto[];

  @Output() switchMicrophoneEvent = new EventEmitter<string>();

  isMyself(member: RehearsalMemberDto): boolean {
    return member.musician.id === this.musician.id;
  }

  switchMicrophone(member: RehearsalMemberDto): void {
    if (this.isMyself(member)) {
      this.switchMicrophoneEvent.emit(member.sessionId);
    }
  }

  microphoneEnabled(member: RehearsalMemberDto): boolean {
    return member.microphoneStatus === 'ON';
  }
}
