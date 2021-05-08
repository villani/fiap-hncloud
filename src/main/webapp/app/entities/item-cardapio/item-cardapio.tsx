import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './item-cardapio.reducer';
import { IItemCardapio } from 'app/shared/model/item-cardapio.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IItemCardapioProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ItemCardapio = (props: IItemCardapioProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const handleSyncList = () => {
    props.getEntities();
  };

  const { itemCardapioList, match, loading } = props;
  return (
    <div>
      <h2 id="item-cardapio-heading" data-cy="ItemCardapioHeading">
        <Translate contentKey="qrDapioApp.itemCardapio.home.title">Item Cardapios</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="qrDapioApp.itemCardapio.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="qrDapioApp.itemCardapio.home.createLabel">Create new Item Cardapio</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {itemCardapioList && itemCardapioList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="qrDapioApp.itemCardapio.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="qrDapioApp.itemCardapio.categoria">Categoria</Translate>
                </th>
                <th>
                  <Translate contentKey="qrDapioApp.itemCardapio.nome">Nome</Translate>
                </th>
                <th>
                  <Translate contentKey="qrDapioApp.itemCardapio.descricao">Descricao</Translate>
                </th>
                <th>
                  <Translate contentKey="qrDapioApp.itemCardapio.valor">Valor</Translate>
                </th>
                <th>
                  <Translate contentKey="qrDapioApp.itemCardapio.cardapio">Cardapio</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {itemCardapioList.map((itemCardapio, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${itemCardapio.id}`} color="link" size="sm">
                      {itemCardapio.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`qrDapioApp.Categoria.${itemCardapio.categoria}`} />
                  </td>
                  <td>{itemCardapio.nome}</td>
                  <td>{itemCardapio.descricao}</td>
                  <td>{itemCardapio.valor}</td>
                  <td>
                    {itemCardapio.cardapio ? <Link to={`cardapio/${itemCardapio.cardapio.id}`}>{itemCardapio.cardapio.id}</Link> : ''}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${itemCardapio.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${itemCardapio.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${itemCardapio.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="qrDapioApp.itemCardapio.home.notFound">No Item Cardapios found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ itemCardapio }: IRootState) => ({
  itemCardapioList: itemCardapio.entities,
  loading: itemCardapio.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ItemCardapio);
