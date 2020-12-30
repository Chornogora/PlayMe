import {Component, OnInit} from '@angular/core';
import {UserRolePageComponent} from '../../../common/pages/user-role-page.component';
import {UserService} from '../../../common/services/user.service';
import {CookieService} from 'ngx-cookie-service';
import {BandDto} from '../../../common/dto/band.dto';
import {BandService} from '../../../common/services/band.service';
import {MusicianDto} from '../../../common/dto/musician.dto';
import {DateService} from '../../../common/services/date.service';
import {ActivatedRoute} from '@angular/router';
import {MembershipDto} from '../../../common/dto/membership.dto';

@Component({
  selector: 'app-band-page',
  templateUrl: './band.page.component.html',
  styleUrls: ['./band.page.component.css']
})
export class BandPageComponent extends UserRolePageComponent implements OnInit {

  id: string;

  band: BandDto;

  members: MembershipDto[];

  musician: MusicianDto;

  dateService: DateService;

  bandService: BandService;

  constructor(cookieService: CookieService, userService: UserService,
              bandService: BandService, dateService: DateService, private route: ActivatedRoute) {
    super(cookieService, userService);
    this.dateService = dateService;
    this.bandService = bandService;
  }

  ngOnInit(): void {
    super.ngOnInit();
    this.id = this.route.snapshot.paramMap.get('id');
    this.bandService.getBand(this.id)
      .subscribe((band: BandDto) => {
        this.band = band;
        this.members = band.members
          .filter(member => member.status.name !== 'subscriber');
      });
    this.userService.getMusician()
      .subscribe((musician: MusicianDto) => {
        this.musician = musician;
      });
  }
}
