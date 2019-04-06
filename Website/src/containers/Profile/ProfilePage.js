import React, { useEffect } from 'react';
import { withRouter } from 'react-router-dom';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import userApiActions from '../../actions/userApiActions';
import Profile from '../../components/Profile/Profile';

function ProfilePage({ userId, userInfo, receiveUserInfo, match }) {
    useEffect(() => {
        receiveUserInfo(parseInt(match.params.id));
    }, []);

    return (
        <Profile
            editingAvailable={userId === userInfo.userId}
            userInfo={userInfo}
        />
    );
}

ProfilePage.propTypes = {
    userId: PropTypes.string.isRequired,
    userInfo: PropTypes.object.isRequired,
    receiveUserInfo: PropTypes.func.isRequired,
    match: PropTypes.object.isRequired,
};

const mapStateToProps = store => {
    return {
        userId: store.access.userId,
        userInfo: store.userInfo
    };
};

const mapDispatchToProps = dispatch => {
    return {
        receiveUserInfo: userId => dispatch(userApiActions.receiveUserInfo(userId))
    };
};

export default withRouter(connect(
    mapStateToProps,
    mapDispatchToProps
)(ProfilePage));
