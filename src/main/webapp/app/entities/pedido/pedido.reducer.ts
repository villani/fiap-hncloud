import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPedido, defaultValue } from 'app/shared/model/pedido.model';

export const ACTION_TYPES = {
  FETCH_PEDIDO_LIST: 'pedido/FETCH_PEDIDO_LIST',
  FETCH_PEDIDO: 'pedido/FETCH_PEDIDO',
  CREATE_PEDIDO: 'pedido/CREATE_PEDIDO',
  UPDATE_PEDIDO: 'pedido/UPDATE_PEDIDO',
  PARTIAL_UPDATE_PEDIDO: 'pedido/PARTIAL_UPDATE_PEDIDO',
  DELETE_PEDIDO: 'pedido/DELETE_PEDIDO',
  RESET: 'pedido/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPedido>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type PedidoState = Readonly<typeof initialState>;

// Reducer

export default (state: PedidoState = initialState, action): PedidoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PEDIDO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PEDIDO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PEDIDO):
    case REQUEST(ACTION_TYPES.UPDATE_PEDIDO):
    case REQUEST(ACTION_TYPES.DELETE_PEDIDO):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_PEDIDO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PEDIDO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PEDIDO):
    case FAILURE(ACTION_TYPES.CREATE_PEDIDO):
    case FAILURE(ACTION_TYPES.UPDATE_PEDIDO):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_PEDIDO):
    case FAILURE(ACTION_TYPES.DELETE_PEDIDO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PEDIDO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PEDIDO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PEDIDO):
    case SUCCESS(ACTION_TYPES.UPDATE_PEDIDO):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_PEDIDO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PEDIDO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/pedidos';

// Actions

export const getEntities: ICrudGetAllAction<IPedido> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PEDIDO_LIST,
  payload: axios.get<IPedido>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IPedido> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PEDIDO,
    payload: axios.get<IPedido>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IPedido> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PEDIDO,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPedido> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PEDIDO,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IPedido> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_PEDIDO,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPedido> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PEDIDO,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
