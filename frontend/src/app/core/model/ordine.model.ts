export interface Biglietto {
  bigliettoId: number;
  programmazioneId: number;
  filmTitolo: string;
  salaNome: string;
  startAt: string;
  fila: string;
  postoNumero: number;
  prezzo: number;
}

export interface Ordine {
  ordineId: number;
  numeroOrdine: string;
  nomeCliente: string;
  totale: number;
  createdAt: string;
  biglietti: Biglietto[];
}

export interface CreateOrdineRequest {
  nomeCliente: string;
  programmazioneId: number;
  postoIds: number[];
}
