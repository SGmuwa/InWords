import { createStore, applyMiddleware } from 'redux';
import { loadState } from 'src/localStorage';
import rootReducer from 'src/reducers';
import apiMiddleware from 'src/middleware/apiMiddleware';
import persistDataMiddleware from 'src/middleware/persistDataMiddleware';

const persistedState = loadState();

let middleware = [apiMiddleware, persistDataMiddleware];

if (process.env.NODE_ENV !== 'production') {
  const { logger } = require('redux-logger');
  middleware = [...middleware, logger];
}

const store = createStore(
  rootReducer,
  persistedState,
  applyMiddleware(...middleware)
);

export default store;
