import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './cardapio.reducer';
import { ICardapio } from 'app/shared/model/cardapio.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICardapioProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Cardapio = (props: ICardapioProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const handleSyncList = () => {
    props.getEntities();
  };

  const { cardapioList, match, loading } = props;
  return (
    <div>
      <h2 id="cardapio-heading" data-cy="CardapioHeading">
        <Translate contentKey="qrDapioApp.cardapio.home.title">Cardapios</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="qrDapioApp.cardapio.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="qrDapioApp.cardapio.home.createLabel">Create new Cardapio</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {cardapioList && cardapioList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="qrDapioApp.cardapio.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="qrDapioApp.cardapio.nome">Nome</Translate>
                </th>
                <th>
                  <Translate contentKey="qrDapioApp.cardapio.restaurante">Restaurante</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cardapioList.map((cardapio, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${cardapio.id}`} color="link" size="sm">
                      {cardapio.id}
                    </Button>
                  </td>
                  <td>{cardapio.nome}</td>
                  <td>
                    {cardapio.restaurante ? <Link to={`restaurante/${cardapio.restaurante.id}`}>{cardapio.restaurante.id}</Link> : ''}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${cardapio.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${cardapio.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${cardapio.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="qrDapioApp.cardapio.home.notFound">No Cardapios found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ cardapio }: IRootState) => ({
  cardapioList: cardapio.entities,
  loading: cardapio.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Cardapio);
