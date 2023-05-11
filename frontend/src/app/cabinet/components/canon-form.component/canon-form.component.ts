import {Component, Input} from '@angular/core';
import {RecordService} from '../../../common/services/record.service';

@Component({
    selector: 'app-canon-form',
    templateUrl: './canon-form.component.html',
    styleUrls: ['./canon-form.component.css']
})
export class CanonFormComponent {

    @Input() delay = 0;

    @Input() recordService: RecordService;

    updateDelay(): void {
        this.recordService.setDelay(this.delay);
    }
}
