import { ICardapio } from 'app/shared/model/cardapio.model';
import { Categoria } from 'app/shared/model/enumerations/categoria.model';

export interface IItemCardapio {
  id?: number;
  categoria?: Categoria;
  nome?: string;
  descricao?: string;
  valor?: number | null;
  cardapio?: ICardapio;
}

export const defaultValue: Readonly<IItemCardapio> = {};
