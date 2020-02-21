import React, { Suspense, lazy } from 'react';
import { Redirect, Route, Router, Switch } from 'react-router-dom';
import { createBrowserHistory } from 'history';
import { useSelector } from 'react-redux';
import Container from 'src/components/Container';
import ScrollToTop from 'src/components/ScrollToTop';
import PageProgress from 'src/templates/PageProgress';
import SmartSnackbar from 'src/templates/SmartSnackbar';
import PageContainer from 'src/templates/PageContainer';
import ProfileMenuButton from 'src/templates/ProfileMenuButton';
import ErrorBoundary from 'src/templates/ErrorBoundary';
import TrainingRouter from 'src/routers/TrainingRouter';
import DictionaryRouter from 'src/routers/DictionaryRouter';

const SignIn = lazy(() => import('src/routes/SignIn'));
const SignUp = lazy(() => import('src/routes/SignUp'));
const Profile = lazy(() => import('src/routes/Profile'));

const history = createBrowserHistory();

const routes = [
  {
    to: '/dictionary',
    text: 'Словарь',
    nestedRoutes: [
      {
        to: '/dictionary/my',
        text: 'Мой словарь'
      },
      {
        to: '/dictionary/sets',
        text: 'Наборы слов'
      }
    ]
  },
  {
    to: '/training',
    text: 'Обучение',
    nestedRoutes: [
      {
        to: '/training/main/0',
        text: 'Тренировки'
      },
      {
        to: '/training/history',
        text: 'История'
      }
    ]
  }
];

function App() {
  const userId = useSelector(store => store.access.userId);

  return (
    <Router history={history}>
      <ScrollToTop />
      <PageContainer
        routes={userId ? routes : undefined}
        rightNodes={userId ? [<ProfileMenuButton key={0} />] : undefined}
      >
        <ErrorBoundary>
          <Suspense fallback={<PageProgress />}>
            <Switch>
              <Route exact path="/">
                {!userId ? (
                  <Redirect to="/signIn" />
                ) : (
                  <Redirect to="/training" />
                )}
              </Route>
              <Route path="/signIn">
                <Container maxWidth="xs">
                  <SignIn />
                </Container>
              </Route>
              <Route path="/signUp">
                <Container maxWidth="xs">
                  <SignUp />
                </Container>
              </Route>
              <Route path="/profile">
                <Container maxWidth="md">
                  <Profile />
                </Container>
              </Route>
              <Route path="/dictionary">
                <DictionaryRouter />
              </Route>
              <Route path="/training">
                <TrainingRouter />
              </Route>
            </Switch>
          </Suspense>
        </ErrorBoundary>
      </PageContainer>
      <SmartSnackbar />
    </Router>
  );
}

export { history };
export default App;
