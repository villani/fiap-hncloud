import { IItemCardapio } from 'app/shared/model/item-cardapio.model';
import { IPedido } from 'app/shared/model/pedido.model';

export interface IItemPedido {
  id?: number;
  quantidade?: number | null;
  item?: IItemCardapio;
  pedido?: IPedido;
}

export const defaultValue: Readonly<IItemPedido> = {};
