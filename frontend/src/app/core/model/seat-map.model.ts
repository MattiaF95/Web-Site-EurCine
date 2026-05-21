export type SeatState = 'AVAILABLE' | 'OCCUPIED' | 'DISABLED';

export interface SeatItem {
  postoId: number;
  numero: number;
  stato: SeatState;
}

export interface SeatRow {
  fila: string;
  posti: SeatItem[];
}

export interface SeatMap {
  programmazioneId: number;
  filmTitolo: string;
  salaNome: string;
  startAt: string;
  righe: SeatRow[];
}
