import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IItemCardapio } from 'app/shared/model/item-cardapio.model';
import { getEntities as getItemCardapios } from 'app/entities/item-cardapio/item-cardapio.reducer';
import { IPedido } from 'app/shared/model/pedido.model';
import { getEntities as getPedidos } from 'app/entities/pedido/pedido.reducer';
import { getEntity, updateEntity, createEntity, reset } from './item-pedido.reducer';
import { IItemPedido } from 'app/shared/model/item-pedido.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IItemPedidoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ItemPedidoUpdate = (props: IItemPedidoUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { itemPedidoEntity, itemCardapios, pedidos, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/item-pedido');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getItemCardapios();
    props.getPedidos();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...itemPedidoEntity,
        ...values,
        item: itemCardapios.find(it => it.id.toString() === values.itemId.toString()),
        pedido: pedidos.find(it => it.id.toString() === values.pedidoId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="qrDapioApp.itemPedido.home.createOrEditLabel" data-cy="ItemPedidoCreateUpdateHeading">
            <Translate contentKey="qrDapioApp.itemPedido.home.createOrEditLabel">Create or edit a ItemPedido</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : itemPedidoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="item-pedido-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="item-pedido-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="quantidadeLabel" for="item-pedido-quantidade">
                  <Translate contentKey="qrDapioApp.itemPedido.quantidade">Quantidade</Translate>
                </Label>
                <AvField
                  id="item-pedido-quantidade"
                  data-cy="quantidade"
                  type="string"
                  className="form-control"
                  name="quantidade"
                  validate={{
                    min: { value: 1, errorMessage: translate('entity.validation.min', { min: 1 }) },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="item-pedido-item">
                  <Translate contentKey="qrDapioApp.itemPedido.item">Item</Translate>
                </Label>
                <AvInput id="item-pedido-item" data-cy="item" type="select" className="form-control" name="itemId" required>
                  <option value="" key="0" />
                  {itemCardapios
                    ? itemCardapios.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <AvGroup>
                <Label for="item-pedido-pedido">
                  <Translate contentKey="qrDapioApp.itemPedido.pedido">Pedido</Translate>
                </Label>
                <AvInput id="item-pedido-pedido" data-cy="pedido" type="select" className="form-control" name="pedidoId" required>
                  <option value="" key="0" />
                  {pedidos
                    ? pedidos.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/item-pedido" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  itemCardapios: storeState.itemCardapio.entities,
  pedidos: storeState.pedido.entities,
  itemPedidoEntity: storeState.itemPedido.entity,
  loading: storeState.itemPedido.loading,
  updating: storeState.itemPedido.updating,
  updateSuccess: storeState.itemPedido.updateSuccess,
});

const mapDispatchToProps = {
  getItemCardapios,
  getPedidos,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ItemPedidoUpdate);
