import {Component, OnInit} from '@angular/core';
import {UserRolePageComponent} from '../../../common/pages/user-role-page.component';
import {UserService} from '../../../common/services/user.service';
import {CookieService} from 'ngx-cookie-service';
import {BandDto} from '../../../common/dto/band.dto';
import {BandService} from '../../../common/services/band.service';
import {MusicianDto} from '../../../common/dto/musician.dto';
import {SubscriptionService} from '../../../common/services/subscription.service';

@Component({
  selector: 'app-bands-page',
  templateUrl: './bands.page.component.html',
  styleUrls: ['./bands.page.component.css']
})
export class BandsPageComponent extends UserRolePageComponent implements OnInit {

  musician: MusicianDto;

  bands: BandDto[] = [];

  mode = 'memberships';

  namePattern = '';

  constructor(cookieService: CookieService, userService: UserService,
              private bandService: BandService, private subscriptionService: SubscriptionService) {
    super(cookieService, userService);
    this.cookieService = cookieService;
  }

  ngOnInit(): void {
    super.ngOnInit();
    this.setMemberships();
  }

  setSubscriptions(): void {
    this.userService.getSubscriptions()
      .subscribe((subscriptions: BandDto[]) => {
        this.bands = subscriptions
          .filter(band => this.subscriptionService.isSubscriberOf(this.musician, band));
      }, error => console.log(error));
  }

  setBands(): void {
    this.bandService.getBands(this.namePattern)
      .subscribe((bands: BandDto[]) => {
          this.bands = bands;
          this.mode = 'all';
        },
        error => console.log(error));
  }

  setMemberships(): void {
    this.updateMusician(() => {
      this.userService.getSubscriptions()
        .subscribe((subscriptions: BandDto[]) => {
          this.bands = subscriptions
            .filter(band => !this.subscriptionService.isSubscriberOf(this.musician, band));
        });
    });
  }

  setCreationMode(): void {
    this.mode = 'creator';
  }

  setMode(mode: string): void {
    this.mode = mode;
    this.reload();
  }

  reload(): void {
    this.updateMusician(() => {
    });

    switch (this.mode) {
      case 'memberships':
        this.setMemberships();
        break;
      case 'subscriptions':
        this.setSubscriptions();
        break;
      case 'all':
        this.setBands();
    }
  }

  updateMusician(afterFunction: any): void {
    this.userService.getMusician()
      .subscribe((musician: MusicianDto) => {
        this.musician = musician;
        afterFunction();
      });
  }
}
