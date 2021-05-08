import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ItemCardapio from './item-cardapio';
import ItemCardapioDetail from './item-cardapio-detail';
import ItemCardapioUpdate from './item-cardapio-update';
import ItemCardapioDeleteDialog from './item-cardapio-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ItemCardapioUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ItemCardapioUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ItemCardapioDetail} />
      <ErrorBoundaryRoute path={match.url} component={ItemCardapio} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ItemCardapioDeleteDialog} />
  </>
);

export default Routes;
