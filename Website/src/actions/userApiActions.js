import apiAction from './apiAction';
import * as commonActions from './commonActions';
import * as userActions from './userActions';
import history from '../history';

export function receiveUserInfo(userId) {
  return apiAction({
    endpoint: `users/${userId}`,
    actionsOnSuccess: [
      (dispatch, data) => dispatch(userActions.initializeUserInfo(data)),
    ],
    actionsOnFailure: [
      dispatch =>
        dispatch(
          commonActions.setSnackbarMessage('Не удалось загрузить профиль')
        ),
    ],
  });
}

export function updateUserInfo(userInfo) {
  return apiAction({
    endpoint: 'users',
    method: 'PUT',
    data: JSON.stringify(userInfo),
    actionsOnSuccess: [
      dispatch => dispatch(userActions.updateUserInfo(userInfo)),
      () => history.push('/profile'),
    ],
    actionsOnFailure: [
      dispatch =>
        dispatch(
          commonActions.setSnackbarMessage('Не удалось сохранить профиль')
        ),
    ],
  });
}
