import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './item-pedido.reducer';
import { IItemPedido } from 'app/shared/model/item-pedido.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IItemPedidoProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ItemPedido = (props: IItemPedidoProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const handleSyncList = () => {
    props.getEntities();
  };

  const { itemPedidoList, match, loading } = props;
  return (
    <div>
      <h2 id="item-pedido-heading" data-cy="ItemPedidoHeading">
        <Translate contentKey="qrDapioApp.itemPedido.home.title">Item Pedidos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="qrDapioApp.itemPedido.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="qrDapioApp.itemPedido.home.createLabel">Create new Item Pedido</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {itemPedidoList && itemPedidoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="qrDapioApp.itemPedido.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="qrDapioApp.itemPedido.quantidade">Quantidade</Translate>
                </th>
                <th>
                  <Translate contentKey="qrDapioApp.itemPedido.item">Item</Translate>
                </th>
                <th>
                  <Translate contentKey="qrDapioApp.itemPedido.pedido">Pedido</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {itemPedidoList.map((itemPedido, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${itemPedido.id}`} color="link" size="sm">
                      {itemPedido.id}
                    </Button>
                  </td>
                  <td>{itemPedido.quantidade}</td>
                  <td>{itemPedido.item ? <Link to={`item-cardapio/${itemPedido.item.id}`}>{itemPedido.item.id}</Link> : ''}</td>
                  <td>{itemPedido.pedido ? <Link to={`pedido/${itemPedido.pedido.id}`}>{itemPedido.pedido.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${itemPedido.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${itemPedido.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${itemPedido.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="qrDapioApp.itemPedido.home.notFound">No Item Pedidos found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ itemPedido }: IRootState) => ({
  itemPedidoList: itemPedido.entities,
  loading: itemPedido.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ItemPedido);
