import {
  beginLoading,
  endLoading,
  setSnackbarMessage,
} from 'actions/commonActions';
import { denyAccess } from 'actions/accessActions';
import { history } from 'App';

export const CALL_API = 'CALL_API';

const API_ROOT = 'https://api.inwords.ru';

const apiMiddleware = ({ dispatch, getState }) => next => action => {
  next(action);

  if (action.type !== CALL_API) return;

  const {
    apiVersion,
    endpoint,
    method,
    data,
    actionsOnSuccess,
    actionsOnFailure,
  } = action.payload;

  dispatch(beginLoading());

  fetch(`${API_ROOT}/${apiVersion}/${endpoint}`, {
    method,
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${getState().access.token}`,
    },
    body: data,
  })
    .then(response => {
      if (!response.ok) {
        throw Error(response.status);
      }

      const contentType = response.headers.get('content-type');
      if (!contentType || !contentType.includes('application/json')) {
        return null;
      }

      return response.json();
    })
    .then(data => {
      dispatch(endLoading());

      actionsOnSuccess.forEach(action => {
        action(dispatch, data);
      });
    })
    .catch(error => {
      dispatch(endLoading());

      if (Number.isInteger(+error.message)) {
        const errorCode = +error.message;

        switch (errorCode) {
          case 401: {
            dispatch(denyAccess());
            history.push('/signIn');
            break;
          }
          default:
            actionsOnFailure.forEach(action => {
              action(dispatch, errorCode);
            });
        }

        console.error(`Request failed with status code ${errorCode}`);
        return;
      }

      dispatch(setSnackbarMessage('Не удалось соединиться с сервером'));

      console.error(error.message);
    });
};

export default apiMiddleware;
