import React from 'react';
import PropTypes from 'prop-types';
import { Link as RouterLink } from 'react-router-dom';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import Paper from '@material-ui/core/Paper';
import Avatar from '@material-ui/core/Avatar';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import Grid from '@material-ui/core/Grid';
import Link from '@material-ui/core/Link';
import TextField from 'src/components/TextField';
import Button from 'src/components/Button';

const useStyles = makeStyles(theme => ({
  paper: {
    marginTop: theme.spacing(4),
    padding: theme.spacing(2, 3, 3),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center'
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main
  },
  form: {
    marginTop: theme.spacing(1),
    width: "100%",
  },
  formField: {
    marginTop: theme.spacing(2),
    marginBottom: theme.spacing(1)
  },
  submit: {
    margin: theme.spacing(2, 0)
  },
  links: {
    marginTop: theme.spacing(2)
  }
}));

function SignIn({ inputs, handleChange, handleSubmit }) {
  const classes = useStyles();

  return (
    <Container component="div" maxWidth="xs">
      <Paper className={classes.paper}>
        <Avatar className={classes.avatar}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Вход
        </Typography>
        <form onSubmit={handleSubmit} className={classes.form}>
          <TextField
            id="email"
            label="Email"
            type="email"
            autoComplete="email"
            name="email"
            value={inputs.email}
            onChange={handleChange}
            required
            fullWidth
            className={classes.formField}
          />
          <TextField
            id="password"
            label="Пароль"
            type="password"
            autoComplete="current-password"
            name="password"
            value={inputs.password}
            onChange={handleChange}
            required
            fullWidth
            className={classes.formField}
          />
          <Button
            type="submit"
            color="primary"
            fullWidth
            className={classes.submit}
          >
            Войти
          </Button>
          <Divider />
          <Grid container justify="flex-end" className={classes.links}>
            <Grid item>
              <Link component={RouterLink} to="/signUp" variant="body2">
                Нет аккаунта? Зарегистрироваться
              </Link>
            </Grid>
          </Grid>
        </form>
      </Paper>
    </Container>
  );
}

SignIn.propTypes = {
  inputs: PropTypes.exact({
    email: PropTypes.string.isRequired,
    password: PropTypes.string.isRequired
  }).isRequired,
  handleChange: PropTypes.func.isRequired,
  handleSubmit: PropTypes.func.isRequired
};

export default SignIn;
