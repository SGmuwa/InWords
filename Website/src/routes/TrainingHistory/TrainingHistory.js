import React from 'react';
import PropTypes from 'prop-types';
import { Link as RouterLink, useRouteMatch } from 'react-router-dom';
import Icon from 'src/components/Icon';
import Grid from 'src/components/Grid';
import GridItem from 'src/components/GridItem';
import Card from 'src/components/Card';
import CardHeader from 'src/components/CardHeader';
import CardContent from 'src/components/CardContent';
import CardActions from 'src/components/CardActions';
import LinkButton from 'src/components/LinkButton';

function TrainingHistory({ recentTrainings }) {
  const match = useRouteMatch();

  return (
    <Grid spacing={3}>
      {recentTrainings.map(({ levelId, playerStars }) => (
        <GridItem key={levelId} xs={12} sm={6} md={4} lg={3}>
          <Card>
            <CardHeader title={`Тренировка ${levelId}`} />
            <CardContent>
              <div>
                {Array(playerStars).fill(<Icon color="gold">star</Icon>)}
                {Array(3 - playerStars).fill(
                  <Icon color="disabled">star</Icon>
                )}
              </div>
            </CardContent>
            <CardActions>
              <LinkButton
                component={RouterLink}
                to={`${match.url}/${levelId}/0`}
                variant="text"
                color="primary"
              >
                Поплыли
              </LinkButton>
            </CardActions>
          </Card>
        </GridItem>
      ))}
    </Grid>
  );
}

TrainingHistory.propTypes = {
  recentTrainings: PropTypes.arrayOf(
    PropTypes.shape({
      levelId: PropTypes.number.isRequired,
      playerStars: PropTypes.number.isRequired,
      isAvailable: PropTypes.bool.isRequired,
      level: PropTypes.number.isRequired
    }).isRequired
  ).isRequired
};

export default TrainingHistory;
