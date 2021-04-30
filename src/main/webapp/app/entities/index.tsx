import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Restaurante from './restaurante';
import Cardapio from './cardapio';
import ItemCardapio from './item-cardapio';
import Pedido from './pedido';
import ItemPedido from './item-pedido';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}restaurante`} component={Restaurante} />
      <ErrorBoundaryRoute path={`${match.url}cardapio`} component={Cardapio} />
      <ErrorBoundaryRoute path={`${match.url}item-cardapio`} component={ItemCardapio} />
      <ErrorBoundaryRoute path={`${match.url}pedido`} component={Pedido} />
      <ErrorBoundaryRoute path={`${match.url}item-pedido`} component={ItemPedido} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
