import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ItemPedido from './item-pedido';
import ItemPedidoDetail from './item-pedido-detail';
import ItemPedidoUpdate from './item-pedido-update';
import ItemPedidoDeleteDialog from './item-pedido-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ItemPedidoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ItemPedidoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ItemPedidoDetail} />
      <ErrorBoundaryRoute path={match.url} component={ItemPedido} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ItemPedidoDeleteDialog} />
  </>
);

export default Routes;
