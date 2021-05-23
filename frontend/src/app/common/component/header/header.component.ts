import {Component, Input, Output, EventEmitter} from '@angular/core';
import {UserDto} from '../../dto/user.dto';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  @Input() user: UserDto;

  @Input() tab: string;

  @Output() signOutEvent = new EventEmitter<boolean>();

  signOut(): void {
    this.signOutEvent.emit();
  }
}
