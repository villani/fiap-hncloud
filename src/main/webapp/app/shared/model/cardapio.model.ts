import { IItemCardapio } from 'app/shared/model/item-cardapio.model';
import { IRestaurante } from 'app/shared/model/restaurante.model';

export interface ICardapio {
  id?: number;
  nome?: string;
  itemCardapios?: IItemCardapio[] | null;
  restaurante?: IRestaurante;
}

export const defaultValue: Readonly<ICardapio> = {};
