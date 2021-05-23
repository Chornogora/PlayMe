import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../common/services/user.service';
import {CookieService} from 'ngx-cookie-service';
import {BandService} from '../../../common/services/band.service';
import {DateService} from '../../../common/services/date.service';
import {ActivatedRoute} from '@angular/router';
import {AbstractBandPageComponent} from '../abstract-band-page/abstract-band.page.component';

@Component({
  selector: 'app-band-page',
  templateUrl: './band.page.component.html',
  styleUrls: ['./band.page.component.css']
})
export class BandPageComponent extends AbstractBandPageComponent implements OnInit {

  shouldLeaveBand = false;

  showMessage = false;

  constructor(cookieService: CookieService, userService: UserService,
              bandService: BandService, dateService: DateService, route: ActivatedRoute) {
    super(cookieService, userService, bandService, dateService, route);
  }

  ngOnInit(): void {
    super.ngOnInit();
  }

  leaveBand(): void {
    const member = this.members
      .filter(currentMember => currentMember.musician.id === this.musician.id)[0];
    this.bandService.deleteMember(this.band, member)
      .subscribe(() => location.reload());
  }

  cancelLeaving(): void {
    this.shouldLeaveBand = false;
    this.showMessage = false;
  }

  tryLeaveBand(): void {
    const member = this.members
      .filter(currentMember => currentMember.musician.id === this.musician.id)[0];
    if (member.status.name !== 'leader' || this.members.length === 1) {
      this.shouldLeaveBand = true;
    } else {
      this.showMessage = true;
    }
  }
}
