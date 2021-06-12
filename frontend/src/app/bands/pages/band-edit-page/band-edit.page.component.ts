import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';
import {UserService} from '../../../common/services/user.service';
import {BandService} from '../../../common/services/band.service';
import {DateService} from '../../../common/services/date.service';
import {ActivatedRoute} from '@angular/router';
import {AbstractBandPageComponent} from '../abstract-band-page/abstract-band.page.component';
import {MusicianDto} from '../../../common/dto/musician.dto';
import {MembershipDto} from '../../../common/dto/membership.dto';
import {ToastService} from '../../../common/services/toast.service';

@Component({
  selector: 'app-band-edit-page',
  templateUrl: './band-edit.page.component.html',
  styleUrls: ['./band-edit.page.component.css']
})
export class BandEditPageComponent extends AbstractBandPageComponent implements OnInit {

  @ViewChild('toast', {static: false})
  toast: ElementRef;

  isMemberChoice = false;

  memberToDelete: MembershipDto = null;

  memberToBecomeLeader: MembershipDto = null;

  toastService: ToastService;

  constructor(cookieService: CookieService, userService: UserService,
              bandService: BandService, dateService: DateService, route: ActivatedRoute,
              toastService: ToastService) {
    super(cookieService, userService, bandService, dateService, route);
    this.toastService = toastService;
  }

  ngOnInit(): void {
    super.ngOnInit();
  }

  activateMemberChoiceMode(): void {
    this.isMemberChoice = true;
  }

  addMember($event: MusicianDto): void {
    this.isMemberChoice = false;
    if ($event != null) {
      const newMember = {
        musicianId: $event.id,
        statusName: 'player'
      };
      this.bandService.addMember(this.band, newMember)
        .subscribe(() => {
          this.ngOnInit();
          this.showToast();
          console.log('Done');
        });
    }
  }

  getMusicians(): MusicianDto[] {
    return this.getMembers()
      .map(membership => membership.musician);
  }

  getMembers(): MembershipDto[] {
    return this.members
      .filter(membership => membership.status.name !== 'leader');
  }

  changeMemberStatus(member: MembershipDto, $event: any): void {
    const newStatus = $event.target.value;
    if (newStatus === 'leader') {
      this.memberToBecomeLeader = member;
    } else {
      this.updateMember(member, newStatus);
    }
  }

  updateMember(member: MembershipDto, newStatus: string): void {
    const memberDto = {
      musicianId: member.musician.id,
      statusName: newStatus
    };
    this.bandService.updateMember(this.band, memberDto)
      .subscribe(() => {
        if (newStatus === 'leader') {
          location.href = `band/${this.band.id}`;
        }
        this.ngOnInit();
        this.showToast();
      });
  }

  deleteMusician(): void {
    this.bandService.deleteMember(this.band, this.memberToDelete)
      .subscribe(() => {
        this.ngOnInit();
        this.showToast();
      });
  }

  setAvatar(files: any): void {
    const image = files.item(0);
    const reader = new FileReader();
    reader.onload = () => {
      this.band.logo = reader.result.toString();
      this.updateBand();
    };
    reader.readAsDataURL(image);
  }

  updateBand(): void {
    this.bandService.updateBand(this.band)
      .subscribe(band => {
        this.band = band;
        this.showToast();
      });
  }

  openDeleteConfirmation(member: MembershipDto): void {
    this.memberToDelete = member;
  }

  cancelDeleting(): void {
    this.memberToDelete = null;
    this.memberToBecomeLeader = null;
    this.ngOnInit();
  }

  showToast(): void {
    this.toastService.show('Done', {
      classname: 'bg-success text-light',
      delay: 2000,
      autohide: true,
      headertext: 'Operation completed successfully'
    });
  }
}
