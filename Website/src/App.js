import React, { Suspense, lazy } from 'react';
import { Redirect, Route, Router, Switch } from 'react-router-dom';
import { createBrowserHistory } from 'history';
import { useSelector } from 'react-redux';
import { makeStyles } from '@material-ui/core/styles';
import CircularProgress from '@material-ui/core/CircularProgress';
import SmartSnackbar from 'src/components/SmartSnackbar';
import PageWrapper from 'src/layout/PageWrapper';
import ErrorBoundary from 'src/layout/ErrorBoundary';

const SignIn = lazy(() => import('./routes/SignIn'));
const SignUp = lazy(() => import('./routes/SignUp'));
const Profile = lazy(() => import('./routes/Profile'));
const ProfileSettings = lazy(() => import('./routes/ProfileSettings'));
const Account = lazy(() => import('./routes/Account'));
const Dictionary = lazy(() => import('./routes/Dictionary'));
const TrainingCategories = lazy(() => import('./routes/TrainingCategories'));
const TrainingTypes = lazy(() => import('./routes/TrainingTypes'));
const TrainingLevels = lazy(() => import('./routes/TrainingLevels'));
const Game = lazy(() => import('./routes/Game'));
const SelectTranslateTraining = lazy(() =>
  import('./routes/SelectTranslateTraining')
);

const history = createBrowserHistory();

const useStyles = makeStyles(theme => ({
  progress: {
    display: 'block',
    margin: 'auto'
  }
}));

function App() {
  const classes = useStyles();

  const userId = useSelector(store => store.access.userId);

  return (
    <Router history={history}>
      <SmartSnackbar />
      <ErrorBoundary>
        <Suspense fallback={<CircularProgress className={classes.progress} />}>
          <PageWrapper authorized={Boolean(userId)}>
            <Switch>
              <Route
                exact
                path="/"
                render={() =>
                  !userId ? (
                    <Redirect to="/signIn" />
                  ) : (
                    <Redirect to="/dictionary" />
                  )
                }
              />
              <Route
                exact
                path="/profile"
                render={() => <Redirect to={`/profile/${userId}`} />}
              />
              <Route path="/signIn">
                <SignIn />
              </Route>
              <Route path="/signUp">
                <SignUp />
              </Route>
              <Route path="/profile/:userId">
                <Profile />
              </Route>
              <Route path="/profileSettings">
                <ProfileSettings />
              </Route>
              <Route path="/account">
                <Account />
              </Route>
              <Route path="/dictionary">
                <Dictionary />
              </Route>
              <Route exact path="/trainings">
                <TrainingCategories />
              </Route>
              <Route exact path="/trainings/:categoryId">
                <TrainingTypes />
              </Route>
              <Route exact path="/trainings/:categoryId/:trainingId">
                <TrainingLevels />
              </Route>
              <Route
                path="/trainings/:categoryId/:trainingId/:levelId"
                render={({ match, ...rest }) => {
                  switch (match.params.trainingId) {
                    case '0':
                      return <Game match={match} {...rest} />;
                    case '1':
                      return (
                        <SelectTranslateTraining match={match} {...rest} />
                      );
                    default:
                      return null;
                  }
                }}
              />
            </Switch>
          </PageWrapper>
        </Suspense>
      </ErrorBoundary>
    </Router>
  );
}

export { history };
export default App;
