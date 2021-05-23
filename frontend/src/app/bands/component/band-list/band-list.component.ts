import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BandDto} from '../../../common/dto/band.dto';
import {DateService} from '../../../common/services/date.service';
import {MusicianDto} from '../../../common/dto/musician.dto';
import {BandService} from '../../../common/services/band.service';
import {SubscriptionService} from '../../../common/services/subscription.service';

@Component({
  selector: 'app-bands-list',
  templateUrl: './band-list.component.html',
  styleUrls: ['./band-list.component.css']
})
export class BandsListComponent {

  @Input() bands: BandDto[];

  @Input() musician: MusicianDto;

  dateService: DateService;

  bandService: BandService;

  subscriptionService: SubscriptionService;

  @Output() updateEvent = new EventEmitter<void>();

  constructor(dateService: DateService, bandService: BandService,
              subscriptionService: SubscriptionService) {
    this.dateService = dateService;
    this.bandService = bandService;
    this.subscriptionService = subscriptionService;
  }

  changeSubscription(band: BandDto): void {
    if (this.subscriptionService.isSubscriberOf(this.musician, band)) {
      this.unsubscribe(band);
    } else {
      this.subscribe(band);
    }
  }

  subscribe(band: BandDto): void {
    this.subscriptionService.subscribe(band)
      .subscribe(() => this.updateEvent.emit());
  }

  unsubscribe(band: BandDto): void {
    this.subscriptionService.unsubscribe(band)
      .subscribe(() => this.updateEvent.emit());
  }
}
