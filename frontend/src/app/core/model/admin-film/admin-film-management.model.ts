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
  genereIds: number[];
}

export interface AdminFilmSaveRequest {
  titolo: string;
  durataMin: number;
  linguaId: number;
  genereIds: number[];
}
