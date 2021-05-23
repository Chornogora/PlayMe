import {AfterViewInit, Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-delete-post-modal',
  templateUrl: './delete-post.component.html'
})
export class DeletePostComponent implements AfterViewInit {

  @ViewChild('content', {static: false})
  content: ElementRef;

  @ViewChild('modal', {static: false})
  modal: ElementRef;

  @Output() cancelEvent = new EventEmitter<void>();

  @Output() deleteEvent = new EventEmitter<void>();

  constructor(private modalService: NgbModal) {
  }

  ngAfterViewInit(): void {
    this.modalService.open(this.content, {ariaLabelledBy: 'modal-basic-title'}).result.then(() => {
      this.deleteEvent.emit();
    }, () => {
      this.cancelEvent.emit();
    });
  }
}
