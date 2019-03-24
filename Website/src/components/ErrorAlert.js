import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import CloseIcon from '@material-ui/icons/Close';
import IconButton from '@material-ui/core/IconButton';
import Snackbar from '@material-ui/core/Snackbar';
import SnackbarContent from '@material-ui/core/SnackbarContent';

const styles = theme => ({
    error: {
        backgroundColor: theme.palette.error.dark,
    },
    icon: {
        fontSize: 20,
    },
    iconVariant: {
        opacity: 0.9,
        marginRight: theme.spacing.unit,
    },
    message: {
        display: 'flex',
        alignItems: 'center',
    },
});

function ErrorAlert({ errorMessage = null, handleClose, classes }) {
    return (
        <Snackbar
            anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left',
            }}
            open={!!errorMessage}
            autoHideDuration={3000}
            onClose={handleClose}
        >
            <SnackbarContent
                className={classes.error}
                aria-describedby="client-snackbar"
                message={
                    <span id="client-snackbar" className={classes.message}>
                        {errorMessage}
                    </span>
                }
                action={[
                    <IconButton
                        key="close"
                        aria-label="Close"
                        color="inherit"
                        className={classes.close}
                        onClick={handleClose}
                    >
                        <CloseIcon className={classes.icon} />
                    </IconButton>
                ]}
            />
        </Snackbar>
    );
}

ErrorAlert.propTypes = {
    errorMessage: PropTypes.string,
    classes: PropTypes.object.isRequired
};

export default withStyles(styles)(ErrorAlert);
