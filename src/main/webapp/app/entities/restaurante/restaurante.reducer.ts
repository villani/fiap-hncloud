import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRestaurante, defaultValue } from 'app/shared/model/restaurante.model';

export const ACTION_TYPES = {
  FETCH_RESTAURANTE_LIST: 'restaurante/FETCH_RESTAURANTE_LIST',
  FETCH_RESTAURANTE: 'restaurante/FETCH_RESTAURANTE',
  CREATE_RESTAURANTE: 'restaurante/CREATE_RESTAURANTE',
  UPDATE_RESTAURANTE: 'restaurante/UPDATE_RESTAURANTE',
  PARTIAL_UPDATE_RESTAURANTE: 'restaurante/PARTIAL_UPDATE_RESTAURANTE',
  DELETE_RESTAURANTE: 'restaurante/DELETE_RESTAURANTE',
  RESET: 'restaurante/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRestaurante>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type RestauranteState = Readonly<typeof initialState>;

// Reducer

export default (state: RestauranteState = initialState, action): RestauranteState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_RESTAURANTE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RESTAURANTE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_RESTAURANTE):
    case REQUEST(ACTION_TYPES.UPDATE_RESTAURANTE):
    case REQUEST(ACTION_TYPES.DELETE_RESTAURANTE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_RESTAURANTE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_RESTAURANTE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RESTAURANTE):
    case FAILURE(ACTION_TYPES.CREATE_RESTAURANTE):
    case FAILURE(ACTION_TYPES.UPDATE_RESTAURANTE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_RESTAURANTE):
    case FAILURE(ACTION_TYPES.DELETE_RESTAURANTE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_RESTAURANTE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_RESTAURANTE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_RESTAURANTE):
    case SUCCESS(ACTION_TYPES.UPDATE_RESTAURANTE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_RESTAURANTE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_RESTAURANTE):
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

const apiUrl = 'api/restaurantes';

// Actions

export const getEntities: ICrudGetAllAction<IRestaurante> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_RESTAURANTE_LIST,
  payload: axios.get<IRestaurante>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IRestaurante> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RESTAURANTE,
    payload: axios.get<IRestaurante>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IRestaurante> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RESTAURANTE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRestaurante> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RESTAURANTE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IRestaurante> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_RESTAURANTE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRestaurante> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RESTAURANTE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
