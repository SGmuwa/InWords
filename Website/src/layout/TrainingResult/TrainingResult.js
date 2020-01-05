import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import Fade from '@material-ui/core/Fade';
import Paper from '@material-ui/core/Paper';
import IconButton from '@material-ui/core/IconButton';
import StarIcon from '@material-ui/icons/Star';
import ReplayIcon from '@material-ui/icons/Replay';
import FastForwardIcon from '@material-ui/icons/FastForward';
import Smiley from './Smiley';

const useStyles = makeStyles(theme => ({
  paper: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    width: 220,
    padding: theme.spacing(3, 0),
    margin: 'auto'
  },
  smiley: {
    marginBottom: theme.spacing(1)
  },
  stars: {
    marginBottom: theme.spacing(1)
  }
}));

function TrainingResult({ score, handleRedirectionToNextLevel, handleReplay }) {
  const classes = useStyles();

  return (
    <Fade in>
      <Paper className={classes.paper}>
        {score !== null && (
          <Fragment>
            <div className={classes.smiley}>
              <Smiley score={score} />
            </div>
            <div className={classes.stars}>
              <StarIcon
                fontSize="large"
                color={score > 0 ? 'secondary' : 'disabled'}
              />
              <StarIcon
                fontSize="large"
                color={score > 1 ? 'secondary' : 'disabled'}
              />
              <StarIcon
                fontSize="large"
                color={score > 2 ? 'secondary' : 'disabled'}
              />
            </div>
          </Fragment>
        )}
        <div>
          <IconButton aria-label="replay" onClick={handleReplay}>
            <ReplayIcon fontSize="large" />
          </IconButton>
          <IconButton
            aria-label="next level"
            onClick={handleRedirectionToNextLevel}
          >
            <FastForwardIcon fontSize="large" />
          </IconButton>
        </div>
      </Paper>
    </Fade>
  );
}

TrainingResult.propTypes = {
  score: PropTypes.number,
  handleRedirectionToNextLevel: PropTypes.func,
  handleReplay: PropTypes.func.isRequired
};

export default TrainingResult;
