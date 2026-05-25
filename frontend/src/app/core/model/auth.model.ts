export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  utenteId: number;
  nome: string;
  cognome: string;
  email: string;
  ruolo: string;
  message: string;
  token: string;
}

export interface AuthMeResponse {
  utenteId: number;
  nome: string;
  cognome: string;
  email: string;
  ruolo: string;
  message: string;
}

export type AuthSession = AuthMeResponse;

export interface RegisterRequest {
  nome: string;
  cognome: string;
  email: string;
  password: string;
}
