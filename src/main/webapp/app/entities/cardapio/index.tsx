import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Cardapio from './cardapio';
import CardapioDetail from './cardapio-detail';
import CardapioUpdate from './cardapio-update';
import CardapioDeleteDialog from './cardapio-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CardapioUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CardapioUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CardapioDetail} />
      <ErrorBoundaryRoute path={match.url} component={Cardapio} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CardapioDeleteDialog} />
  </>
);

export default Routes;
