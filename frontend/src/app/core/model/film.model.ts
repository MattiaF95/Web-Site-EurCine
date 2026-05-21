export interface Film {
  id: number;
  titolo: string;
  durataMin: number;
  lingua?: {
    id: number;
    nome: string;
  };
  generi?: Array<{
    id: number;
    nome: string;
  }>;
}
