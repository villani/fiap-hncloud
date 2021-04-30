import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './restaurante.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRestauranteDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RestauranteDetail = (props: IRestauranteDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { restauranteEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="restauranteDetailsHeading">
          <Translate contentKey="qrDapioApp.restaurante.detail.title">Restaurante</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{restauranteEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="qrDapioApp.restaurante.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{restauranteEntity.nome}</dd>
        </dl>
        <Button tag={Link} to="/restaurante" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/restaurante/${restauranteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ restaurante }: IRootState) => ({
  restauranteEntity: restaurante.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RestauranteDetail);
