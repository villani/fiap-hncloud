import { ICardapio } from 'app/shared/model/cardapio.model';
import { IPedido } from 'app/shared/model/pedido.model';

export interface IRestaurante {
  id?: number;
  nome?: string;
  cardapios?: ICardapio[] | null;
  pedidos?: IPedido[] | null;
}

export const defaultValue: Readonly<IRestaurante> = {};
