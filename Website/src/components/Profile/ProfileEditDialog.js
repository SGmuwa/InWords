import React, { useState } from 'react';
import PropTypes from 'prop-types';
import withStyles from '@material-ui/core/styles/withStyles';
import Slide from '@material-ui/core/Slide';
import Dialog from '@material-ui/core/Dialog';
import Typography from '@material-ui/core/Typography';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import CloseIcon from '@material-ui/icons/Close';
import IconButton from '@material-ui/core/IconButton';

const styles = theme => ({
    appBar: {
        position: 'relative',
    },
    flex: {
        flex: 1,
    },
    content: {
        padding: theme.spacing.unit * 3,
        display: 'flex',
        justifyContent: 'center',
    },
});

function Transition(props) {
    return <Slide direction="up" {...props} />;
}

function ProfileEditDialog({ userInfo, open, handleClose, handleSubmit, classes }) {
    const [values, setValues] = useState({
        nickName: userInfo.nickName,
        avatarPath: userInfo.avatarPath ? userInfo.avatarPath : ''
    });

    const handleChange = prop => event => {
        setValues({ ...values, [prop]: event.target.value });
    };

    return (
        <Dialog fullScreen open={open} onClose={handleClose} TransitionComponent={Transition}>
            <AppBar className={classes.appBar}>
                <Toolbar>
                    <IconButton color="inherit" onClick={handleClose} aria-label="Close">
                        <CloseIcon />
                    </IconButton>
                    <Typography variant="h6" color="inherit" className={classes.flex}>
                        Редактирование
                    </Typography>
                    <Button type="submit" form="form" color="inherit" onClick={handleClose}>
                        Сохранить
                    </Button>
                </Toolbar>
            </AppBar>
            <main className={classes.content}>
                <form
                id="form"
                onSubmit={handleSubmit({
                    NickName: values.nickName,
                    AvatarPath: values.avatarPath
                })}>
                    <TextField
                        required
                        id="nickName"
                        label="Никнейм"
                        fullWidth
                        value={values.nickName}
                        onChange={handleChange('nickName')}
                        margin="normal"
                        variant="outlined"
                    />
                    <TextField
                        id="avatarPath"
                        label="URL-адрес аватара"
                        fullWidth
                        value={values.avatarPath}
                        onChange={handleChange('avatarPath')}
                        margin="normal"
                        variant="outlined"
                    />
                </form>
            </main>
        </Dialog>
    );
}

ProfileEditDialog.propTypes = {
    userInfo: PropTypes.object.isRequired,
    open: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    handleSubmit: PropTypes.func.isRequired,
    classes: PropTypes.object.isRequired
};

export default withStyles(styles)(ProfileEditDialog);
