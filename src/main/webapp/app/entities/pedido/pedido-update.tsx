import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRestaurante } from 'app/shared/model/restaurante.model';
import { getEntities as getRestaurantes } from 'app/entities/restaurante/restaurante.reducer';
import { getEntity, updateEntity, createEntity, reset } from './pedido.reducer';
import { IPedido } from 'app/shared/model/pedido.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPedidoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PedidoUpdate = (props: IPedidoUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { pedidoEntity, restaurantes, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/pedido');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getRestaurantes();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.dataHora = convertDateTimeToServer(values.dataHora);

    if (errors.length === 0) {
      const entity = {
        ...pedidoEntity,
        ...values,
        restaurante: restaurantes.find(it => it.id.toString() === values.restauranteId.toString()),
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
          <h2 id="qrDapioApp.pedido.home.createOrEditLabel" data-cy="PedidoCreateUpdateHeading">
            <Translate contentKey="qrDapioApp.pedido.home.createOrEditLabel">Create or edit a Pedido</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : pedidoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="pedido-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="pedido-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="formaPagamentoLabel" for="pedido-formaPagamento">
                  <Translate contentKey="qrDapioApp.pedido.formaPagamento">Forma Pagamento</Translate>
                </Label>
                <AvInput
                  id="pedido-formaPagamento"
                  data-cy="formaPagamento"
                  type="select"
                  className="form-control"
                  name="formaPagamento"
                  value={(!isNew && pedidoEntity.formaPagamento) || 'CREDITO'}
                >
                  <option value="CREDITO">{translate('qrDapioApp.FormaPagamento.CREDITO')}</option>
                  <option value="DEBITO">{translate('qrDapioApp.FormaPagamento.DEBITO')}</option>
                  <option value="PIX">{translate('qrDapioApp.FormaPagamento.PIX')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="dataHoraLabel" for="pedido-dataHora">
                  <Translate contentKey="qrDapioApp.pedido.dataHora">Data Hora</Translate>
                </Label>
                <AvInput
                  id="pedido-dataHora"
                  data-cy="dataHora"
                  type="datetime-local"
                  className="form-control"
                  name="dataHora"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.pedidoEntity.dataHora)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="senhaLabel" for="pedido-senha">
                  <Translate contentKey="qrDapioApp.pedido.senha">Senha</Translate>
                </Label>
                <AvField
                  id="pedido-senha"
                  data-cy="senha"
                  type="string"
                  className="form-control"
                  name="senha"
                  validate={{
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="pedido-restaurante">
                  <Translate contentKey="qrDapioApp.pedido.restaurante">Restaurante</Translate>
                </Label>
                <AvInput id="pedido-restaurante" data-cy="restaurante" type="select" className="form-control" name="restauranteId" required>
                  <option value="" key="0" />
                  {restaurantes
                    ? restaurantes.map(otherEntity => (
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
              <Button tag={Link} id="cancel-save" to="/pedido" replace color="info">
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
  restaurantes: storeState.restaurante.entities,
  pedidoEntity: storeState.pedido.entity,
  loading: storeState.pedido.loading,
  updating: storeState.pedido.updating,
  updateSuccess: storeState.pedido.updateSuccess,
});

const mapDispatchToProps = {
  getRestaurantes,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PedidoUpdate);
