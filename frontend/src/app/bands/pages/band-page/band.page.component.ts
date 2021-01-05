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

  constructor(cookieService: CookieService, userService: UserService,
              bandService: BandService, dateService: DateService, route: ActivatedRoute) {
    super(cookieService, userService, bandService, dateService, route);
  }

  ngOnInit(): void {
    super.ngOnInit();
  }
}
