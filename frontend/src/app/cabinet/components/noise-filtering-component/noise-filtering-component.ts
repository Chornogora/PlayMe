import {Component, Input} from '@angular/core';
import {RecordService} from '../../../common/services/record.service';

@Component({
    selector: 'app-noise-filtering-form',
    templateUrl: './noise-filtering-component.html',
    styleUrls: ['./noise-filtering-component.css']
})
export class NoiseFilteringComponent {

    entries = ['no', 'frequencies', 'biquad'];

    @Input() recordService: RecordService;

    updateFilteringMethod(filteringMethod: string): void {
        this.recordService.setFilteringMethod(filteringMethod);
    }
}
