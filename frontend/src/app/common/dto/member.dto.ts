export class MemberDto {
  musicianId: string;
  statusName: string;

  constructor(musicianId: string, statusName: string) {
    this.musicianId = musicianId;
    this.statusName = statusName;
  }
}
