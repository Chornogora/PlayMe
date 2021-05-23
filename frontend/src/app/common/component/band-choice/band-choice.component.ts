import {Component, Input, Output, EventEmitter, OnInit} from '@angular/core';
import {MusicianDto} from '../../dto/musician.dto';
import {BandDto} from '../../dto/band.dto';
import {BandService} from '../../services/band.service';

@Component({
  selector: 'app-band-choice',
  templateUrl: './band-choice.component.html',
  styleUrls: ['./band-choice.component.css']
})
export class BandChoiceComponent implements OnInit {

  @Input() musician: MusicianDto;

  bands: BandDto[] = [];

  selectedBand: BandDto;

  @Output() bandSelectedEvent = new EventEmitter<BandDto>();

  constructor(private bandService: BandService) {
  }

  ngOnInit(): void {
    const memberships = this.musician.memberships
      .filter(membership => membership.status.name !== 'subscriber');
    switch (memberships.length) {
      case 0:
        this.bandSelectedEvent.emit(null);
        break;
      case 1:
        if (memberships[0].status.name === 'leader') {
          this.returnResult(memberships[0].id.bandId);
          return;
        }
      default:
        memberships.forEach(membership => {
          const bandId = membership.id.bandId;
          this.bandService.getBand(bandId)
            .subscribe(band => {
              this.bands.push(band);
              this.selectedBand = this.bands[0];
            });
        });
    }
  }

  returnResult(bandId: string): void {
    this.bandService.getBand(bandId)
      .subscribe(band => this.bandSelectedEvent.emit(band));
  }

  submit(): void {
    this.bandSelectedEvent.emit(this.selectedBand);
  }

  noChoice(): void {
    this.bandSelectedEvent.emit(null);
  }
}
