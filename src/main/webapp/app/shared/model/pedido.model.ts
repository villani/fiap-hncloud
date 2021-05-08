import dayjs from 'dayjs';
import { IItemPedido } from 'app/shared/model/item-pedido.model';
import { IRestaurante } from 'app/shared/model/restaurante.model';
import { FormaPagamento } from 'app/shared/model/enumerations/forma-pagamento.model';

export interface IPedido {
  id?: number;
  formaPagamento?: FormaPagamento;
  dataHora?: string;
  senha?: number | null;
  itemPedidos?: IItemPedido[] | null;
  restaurante?: IRestaurante;
}

export const defaultValue: Readonly<IPedido> = {};
