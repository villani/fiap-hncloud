import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IItemPedido, defaultValue } from 'app/shared/model/item-pedido.model';

export const ACTION_TYPES = {
  FETCH_ITEMPEDIDO_LIST: 'itemPedido/FETCH_ITEMPEDIDO_LIST',
  FETCH_ITEMPEDIDO: 'itemPedido/FETCH_ITEMPEDIDO',
  CREATE_ITEMPEDIDO: 'itemPedido/CREATE_ITEMPEDIDO',
  UPDATE_ITEMPEDIDO: 'itemPedido/UPDATE_ITEMPEDIDO',
  PARTIAL_UPDATE_ITEMPEDIDO: 'itemPedido/PARTIAL_UPDATE_ITEMPEDIDO',
  DELETE_ITEMPEDIDO: 'itemPedido/DELETE_ITEMPEDIDO',
  RESET: 'itemPedido/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IItemPedido>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type ItemPedidoState = Readonly<typeof initialState>;

// Reducer

export default (state: ItemPedidoState = initialState, action): ItemPedidoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ITEMPEDIDO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ITEMPEDIDO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ITEMPEDIDO):
    case REQUEST(ACTION_TYPES.UPDATE_ITEMPEDIDO):
    case REQUEST(ACTION_TYPES.DELETE_ITEMPEDIDO):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_ITEMPEDIDO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_ITEMPEDIDO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ITEMPEDIDO):
    case FAILURE(ACTION_TYPES.CREATE_ITEMPEDIDO):
    case FAILURE(ACTION_TYPES.UPDATE_ITEMPEDIDO):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_ITEMPEDIDO):
    case FAILURE(ACTION_TYPES.DELETE_ITEMPEDIDO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ITEMPEDIDO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ITEMPEDIDO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ITEMPEDIDO):
    case SUCCESS(ACTION_TYPES.UPDATE_ITEMPEDIDO):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_ITEMPEDIDO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ITEMPEDIDO):
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

const apiUrl = 'api/item-pedidos';

// Actions

export const getEntities: ICrudGetAllAction<IItemPedido> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ITEMPEDIDO_LIST,
  payload: axios.get<IItemPedido>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IItemPedido> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ITEMPEDIDO,
    payload: axios.get<IItemPedido>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IItemPedido> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ITEMPEDIDO,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IItemPedido> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ITEMPEDIDO,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IItemPedido> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_ITEMPEDIDO,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IItemPedido> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ITEMPEDIDO,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
