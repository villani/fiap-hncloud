import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Restaurante from './restaurante';
import RestauranteDetail from './restaurante-detail';
import RestauranteUpdate from './restaurante-update';
import RestauranteDeleteDialog from './restaurante-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RestauranteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RestauranteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RestauranteDetail} />
      <ErrorBoundaryRoute path={match.url} component={Restaurante} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RestauranteDeleteDialog} />
  </>
);

export default Routes;
