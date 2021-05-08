import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICardapio, defaultValue } from 'app/shared/model/cardapio.model';

export const ACTION_TYPES = {
  FETCH_CARDAPIO_LIST: 'cardapio/FETCH_CARDAPIO_LIST',
  FETCH_CARDAPIO: 'cardapio/FETCH_CARDAPIO',
  CREATE_CARDAPIO: 'cardapio/CREATE_CARDAPIO',
  UPDATE_CARDAPIO: 'cardapio/UPDATE_CARDAPIO',
  PARTIAL_UPDATE_CARDAPIO: 'cardapio/PARTIAL_UPDATE_CARDAPIO',
  DELETE_CARDAPIO: 'cardapio/DELETE_CARDAPIO',
  RESET: 'cardapio/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICardapio>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type CardapioState = Readonly<typeof initialState>;

// Reducer

export default (state: CardapioState = initialState, action): CardapioState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CARDAPIO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CARDAPIO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_CARDAPIO):
    case REQUEST(ACTION_TYPES.UPDATE_CARDAPIO):
    case REQUEST(ACTION_TYPES.DELETE_CARDAPIO):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_CARDAPIO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_CARDAPIO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CARDAPIO):
    case FAILURE(ACTION_TYPES.CREATE_CARDAPIO):
    case FAILURE(ACTION_TYPES.UPDATE_CARDAPIO):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_CARDAPIO):
    case FAILURE(ACTION_TYPES.DELETE_CARDAPIO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_CARDAPIO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_CARDAPIO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_CARDAPIO):
    case SUCCESS(ACTION_TYPES.UPDATE_CARDAPIO):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_CARDAPIO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_CARDAPIO):
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

const apiUrl = 'api/cardapios';

// Actions

export const getEntities: ICrudGetAllAction<ICardapio> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CARDAPIO_LIST,
  payload: axios.get<ICardapio>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<ICardapio> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CARDAPIO,
    payload: axios.get<ICardapio>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICardapio> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CARDAPIO,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICardapio> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CARDAPIO,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ICardapio> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_CARDAPIO,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICardapio> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CARDAPIO,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
