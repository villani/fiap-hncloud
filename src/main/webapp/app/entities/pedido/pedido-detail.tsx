import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './pedido.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPedidoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PedidoDetail = (props: IPedidoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { pedidoEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pedidoDetailsHeading">
          <Translate contentKey="qrDapioApp.pedido.detail.title">Pedido</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pedidoEntity.id}</dd>
          <dt>
            <span id="formaPagamento">
              <Translate contentKey="qrDapioApp.pedido.formaPagamento">Forma Pagamento</Translate>
            </span>
          </dt>
          <dd>{pedidoEntity.formaPagamento}</dd>
          <dt>
            <span id="dataHora">
              <Translate contentKey="qrDapioApp.pedido.dataHora">Data Hora</Translate>
            </span>
          </dt>
          <dd>{pedidoEntity.dataHora ? <TextFormat value={pedidoEntity.dataHora} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="senha">
              <Translate contentKey="qrDapioApp.pedido.senha">Senha</Translate>
            </span>
          </dt>
          <dd>{pedidoEntity.senha}</dd>
          <dt>
            <Translate contentKey="qrDapioApp.pedido.restaurante">Restaurante</Translate>
          </dt>
          <dd>{pedidoEntity.restaurante ? pedidoEntity.restaurante.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/pedido" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pedido/${pedidoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ pedido }: IRootState) => ({
  pedidoEntity: pedido.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PedidoDetail);
