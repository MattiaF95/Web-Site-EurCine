import { Film } from './film.model';
import { Sala } from './sala.model';

export interface Programmazione {
  id: number;
  film?: Film;
  sala?: Sala;
  startAt: string;
  prezzoBasePre18: number;
  prezzoBasePost18: number;
}
