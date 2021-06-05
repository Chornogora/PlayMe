import {AfterViewInit, Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html'
})
export class ModalComponent implements AfterViewInit {

  @ViewChild('content', {static: false})
  content: ElementRef;

  @ViewChild('modal', {static: false})
  modal: ElementRef;

  @Input() title: string;

  @Input() text: string;

  @Output() cancelEvent = new EventEmitter<void>();

  @Output() acceptEvent = new EventEmitter<void>();

  constructor(private modalService: NgbModal) {
  }

  ngAfterViewInit(): void {
    this.modalService.open(this.content, {ariaLabelledBy: 'modal-basic-title'}).result.then(() => {
      this.acceptEvent.emit();
    }, () => {
      this.cancelEvent.emit();
    });
  }
}
