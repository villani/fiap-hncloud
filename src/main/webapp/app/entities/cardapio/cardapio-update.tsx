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
import { getEntity, updateEntity, createEntity, reset } from './cardapio.reducer';
import { ICardapio } from 'app/shared/model/cardapio.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICardapioUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CardapioUpdate = (props: ICardapioUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { cardapioEntity, restaurantes, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/cardapio');
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
    if (errors.length === 0) {
      const entity = {
        ...cardapioEntity,
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
          <h2 id="qrDapioApp.cardapio.home.createOrEditLabel" data-cy="CardapioCreateUpdateHeading">
            <Translate contentKey="qrDapioApp.cardapio.home.createOrEditLabel">Create or edit a Cardapio</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : cardapioEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="cardapio-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="cardapio-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nomeLabel" for="cardapio-nome">
                  <Translate contentKey="qrDapioApp.cardapio.nome">Nome</Translate>
                </Label>
                <AvField
                  id="cardapio-nome"
                  data-cy="nome"
                  type="text"
                  name="nome"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="cardapio-restaurante">
                  <Translate contentKey="qrDapioApp.cardapio.restaurante">Restaurante</Translate>
                </Label>
                <AvInput
                  id="cardapio-restaurante"
                  data-cy="restaurante"
                  type="select"
                  className="form-control"
                  name="restauranteId"
                  required
                >
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
              <Button tag={Link} id="cancel-save" to="/cardapio" replace color="info">
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
  cardapioEntity: storeState.cardapio.entity,
  loading: storeState.cardapio.loading,
  updating: storeState.cardapio.updating,
  updateSuccess: storeState.cardapio.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(CardapioUpdate);
