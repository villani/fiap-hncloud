import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from './user-management';
// prettier-ignore
import restaurante, {
  RestauranteState
} from 'app/entities/restaurante/restaurante.reducer';
// prettier-ignore
import cardapio, {
  CardapioState
} from 'app/entities/cardapio/cardapio.reducer';
// prettier-ignore
import itemCardapio, {
  ItemCardapioState
} from 'app/entities/item-cardapio/item-cardapio.reducer';
// prettier-ignore
import pedido, {
  PedidoState
} from 'app/entities/pedido/pedido.reducer';
// prettier-ignore
import itemPedido, {
  ItemPedidoState
} from 'app/entities/item-pedido/item-pedido.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly restaurante: RestauranteState;
  readonly cardapio: CardapioState;
  readonly itemCardapio: ItemCardapioState;
  readonly pedido: PedidoState;
  readonly itemPedido: ItemPedidoState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  restaurante,
  cardapio,
  itemCardapio,
  pedido,
  itemPedido,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
});

export default rootReducer;
