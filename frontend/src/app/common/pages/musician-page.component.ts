import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';
import {UserService} from '../services/user.service';
import {UserRolePageComponent} from './user-role-page.component';
import {MusicianDto} from '../dto/musician.dto';

@Component({template: ''})
export class MusicianPageComponent extends UserRolePageComponent implements OnInit {

  musician: MusicianDto;

  @Output() musicianInitializedEvent = new EventEmitter<void>();

  constructor(protected cookieService: CookieService, protected userService: UserService) {
    super(cookieService, userService);
  }

  ngOnInit(): void {
    super.ngOnInit();
    this.userService.getMusician()
      .subscribe((musician: MusicianDto) => {
        this.musician = musician;
        this.musicianInitializedEvent.emit();
      });
  }
}
