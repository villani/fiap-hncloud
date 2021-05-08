import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './pedido.reducer';
import { IPedido } from 'app/shared/model/pedido.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPedidoProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Pedido = (props: IPedidoProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const handleSyncList = () => {
    props.getEntities();
  };

  const { pedidoList, match, loading } = props;
  return (
    <div>
      <h2 id="pedido-heading" data-cy="PedidoHeading">
        <Translate contentKey="qrDapioApp.pedido.home.title">Pedidos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="qrDapioApp.pedido.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="qrDapioApp.pedido.home.createLabel">Create new Pedido</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {pedidoList && pedidoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="qrDapioApp.pedido.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="qrDapioApp.pedido.formaPagamento">Forma Pagamento</Translate>
                </th>
                <th>
                  <Translate contentKey="qrDapioApp.pedido.dataHora">Data Hora</Translate>
                </th>
                <th>
                  <Translate contentKey="qrDapioApp.pedido.senha">Senha</Translate>
                </th>
                <th>
                  <Translate contentKey="qrDapioApp.pedido.restaurante">Restaurante</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {pedidoList.map((pedido, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${pedido.id}`} color="link" size="sm">
                      {pedido.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`qrDapioApp.FormaPagamento.${pedido.formaPagamento}`} />
                  </td>
                  <td>{pedido.dataHora ? <TextFormat type="date" value={pedido.dataHora} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{pedido.senha}</td>
                  <td>{pedido.restaurante ? <Link to={`restaurante/${pedido.restaurante.id}`}>{pedido.restaurante.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${pedido.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${pedido.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${pedido.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="qrDapioApp.pedido.home.notFound">No Pedidos found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ pedido }: IRootState) => ({
  pedidoList: pedido.entities,
  loading: pedido.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Pedido);
