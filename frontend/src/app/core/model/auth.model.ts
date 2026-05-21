export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  adminId: number;
  nome: string;
  cognome: string;
  email: string;
  ruolo: string;
  message: string;
}

export interface AuthMeResponse {
  adminId: number;
  nome: string;
  cognome: string;
  email: string;
  ruolo: string;
  message: string;
}

export type AuthSession = AuthMeResponse;
