import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Pedido from './pedido';
import PedidoDetail from './pedido-detail';
import PedidoUpdate from './pedido-update';
import PedidoDeleteDialog from './pedido-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PedidoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PedidoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PedidoDetail} />
      <ErrorBoundaryRoute path={match.url} component={Pedido} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PedidoDeleteDialog} />
  </>
);

export default Routes;
