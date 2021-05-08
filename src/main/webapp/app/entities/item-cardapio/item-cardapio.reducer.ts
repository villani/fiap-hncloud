import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IItemCardapio, defaultValue } from 'app/shared/model/item-cardapio.model';

export const ACTION_TYPES = {
  FETCH_ITEMCARDAPIO_LIST: 'itemCardapio/FETCH_ITEMCARDAPIO_LIST',
  FETCH_ITEMCARDAPIO: 'itemCardapio/FETCH_ITEMCARDAPIO',
  CREATE_ITEMCARDAPIO: 'itemCardapio/CREATE_ITEMCARDAPIO',
  UPDATE_ITEMCARDAPIO: 'itemCardapio/UPDATE_ITEMCARDAPIO',
  PARTIAL_UPDATE_ITEMCARDAPIO: 'itemCardapio/PARTIAL_UPDATE_ITEMCARDAPIO',
  DELETE_ITEMCARDAPIO: 'itemCardapio/DELETE_ITEMCARDAPIO',
  RESET: 'itemCardapio/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IItemCardapio>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type ItemCardapioState = Readonly<typeof initialState>;

// Reducer

export default (state: ItemCardapioState = initialState, action): ItemCardapioState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ITEMCARDAPIO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ITEMCARDAPIO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ITEMCARDAPIO):
    case REQUEST(ACTION_TYPES.UPDATE_ITEMCARDAPIO):
    case REQUEST(ACTION_TYPES.DELETE_ITEMCARDAPIO):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_ITEMCARDAPIO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_ITEMCARDAPIO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ITEMCARDAPIO):
    case FAILURE(ACTION_TYPES.CREATE_ITEMCARDAPIO):
    case FAILURE(ACTION_TYPES.UPDATE_ITEMCARDAPIO):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_ITEMCARDAPIO):
    case FAILURE(ACTION_TYPES.DELETE_ITEMCARDAPIO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ITEMCARDAPIO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ITEMCARDAPIO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ITEMCARDAPIO):
    case SUCCESS(ACTION_TYPES.UPDATE_ITEMCARDAPIO):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_ITEMCARDAPIO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ITEMCARDAPIO):
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

const apiUrl = 'api/item-cardapios';

// Actions

export const getEntities: ICrudGetAllAction<IItemCardapio> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ITEMCARDAPIO_LIST,
  payload: axios.get<IItemCardapio>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IItemCardapio> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ITEMCARDAPIO,
    payload: axios.get<IItemCardapio>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IItemCardapio> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ITEMCARDAPIO,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IItemCardapio> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ITEMCARDAPIO,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IItemCardapio> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_ITEMCARDAPIO,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IItemCardapio> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ITEMCARDAPIO,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
