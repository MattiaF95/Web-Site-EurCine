export interface AdminFilmTitleOption {
  id: number;
  titolo: string;
}

export interface AdminCatalogOption {
  id: number;
  nome: string;
}

export interface AdminFilmMetaResponse {
  lingue: AdminCatalogOption[];
  generi: AdminCatalogOption[];
}

export interface AdminFilmFormData {
  id: number;
  titolo: string;
  durataMin: number;
  linguaId: number;
  trama: string;
  genereIds: number[];
}

export interface AdminFilmSaveRequest {
  titolo: string;
  durataMin: number;
  linguaId: number;
  trama: string;
  genereIds: number[];
}

export interface AdminProgrammazioneRowRequest {
  salaId: number;
  orario: string;
}

export interface AdminProgrammazioneBatchCreateRequest {
  giorno: string;
  filmId: number;
  items: AdminProgrammazioneRowRequest[];
}

export interface AdminProgrammazioneCatalogResponse {
  film: AdminFilmTitleOption[];
  sale: AdminCatalogOption[];
}

export interface AdminProgrammazioneCreatedItem {
  programmazioneId: number;
  filmId: number;
  filmTitolo: string;
  salaId: number;
  salaNome: string;
  startAt: string;
  prezzoBasePre18: number;
  prezzoBasePost18: number;
}

export interface AdminProgrammazioneBatchCreateResponse {
  message: string;
  createdCount: number;
  created: AdminProgrammazioneCreatedItem[];
}
