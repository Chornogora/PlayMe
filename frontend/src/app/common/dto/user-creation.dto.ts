export class UserCreationDto {
  login: string;
  password: string;
  email: string;
  firstName: string;
  lastName: string;
  birthdate: Date = new Date(Date.parse('1900-01-01'));
  captchaTokenId: string;
  captchaNumber: string;
}
