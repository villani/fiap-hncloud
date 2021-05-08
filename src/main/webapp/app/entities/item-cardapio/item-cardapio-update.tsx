import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICardapio } from 'app/shared/model/cardapio.model';
import { getEntities as getCardapios } from 'app/entities/cardapio/cardapio.reducer';
import { getEntity, updateEntity, createEntity, reset } from './item-cardapio.reducer';
import { IItemCardapio } from 'app/shared/model/item-cardapio.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IItemCardapioUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ItemCardapioUpdate = (props: IItemCardapioUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { itemCardapioEntity, cardapios, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/item-cardapio');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getCardapios();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...itemCardapioEntity,
        ...values,
        cardapio: cardapios.find(it => it.id.toString() === values.cardapioId.toString()),
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
          <h2 id="qrDapioApp.itemCardapio.home.createOrEditLabel" data-cy="ItemCardapioCreateUpdateHeading">
            <Translate contentKey="qrDapioApp.itemCardapio.home.createOrEditLabel">Create or edit a ItemCardapio</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : itemCardapioEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="item-cardapio-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="item-cardapio-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="categoriaLabel" for="item-cardapio-categoria">
                  <Translate contentKey="qrDapioApp.itemCardapio.categoria">Categoria</Translate>
                </Label>
                <AvInput
                  id="item-cardapio-categoria"
                  data-cy="categoria"
                  type="select"
                  className="form-control"
                  name="categoria"
                  value={(!isNew && itemCardapioEntity.categoria) || 'PRATO'}
                >
                  <option value="PRATO">{translate('qrDapioApp.Categoria.PRATO')}</option>
                  <option value="BEBIDA">{translate('qrDapioApp.Categoria.BEBIDA')}</option>
                  <option value="SOBREMESA">{translate('qrDapioApp.Categoria.SOBREMESA')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="nomeLabel" for="item-cardapio-nome">
                  <Translate contentKey="qrDapioApp.itemCardapio.nome">Nome</Translate>
                </Label>
                <AvField
                  id="item-cardapio-nome"
                  data-cy="nome"
                  type="text"
                  name="nome"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="descricaoLabel" for="item-cardapio-descricao">
                  <Translate contentKey="qrDapioApp.itemCardapio.descricao">Descricao</Translate>
                </Label>
                <AvField
                  id="item-cardapio-descricao"
                  data-cy="descricao"
                  type="text"
                  name="descricao"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="valorLabel" for="item-cardapio-valor">
                  <Translate contentKey="qrDapioApp.itemCardapio.valor">Valor</Translate>
                </Label>
                <AvField id="item-cardapio-valor" data-cy="valor" type="text" name="valor" />
              </AvGroup>
              <AvGroup>
                <Label for="item-cardapio-cardapio">
                  <Translate contentKey="qrDapioApp.itemCardapio.cardapio">Cardapio</Translate>
                </Label>
                <AvInput id="item-cardapio-cardapio" data-cy="cardapio" type="select" className="form-control" name="cardapioId" required>
                  <option value="" key="0" />
                  {cardapios
                    ? cardapios.map(otherEntity => (
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
              <Button tag={Link} id="cancel-save" to="/item-cardapio" replace color="info">
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
  cardapios: storeState.cardapio.entities,
  itemCardapioEntity: storeState.itemCardapio.entity,
  loading: storeState.itemCardapio.loading,
  updating: storeState.itemCardapio.updating,
  updateSuccess: storeState.itemCardapio.updateSuccess,
});

const mapDispatchToProps = {
  getCardapios,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ItemCardapioUpdate);
