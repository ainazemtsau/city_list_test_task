export interface User {
  username: string;
  password: string;
  isAuthorized: boolean;
  authdata?: string;
}
