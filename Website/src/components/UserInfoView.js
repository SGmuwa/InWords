import React, { Fragment } from 'react';
import PropTypes from 'prop-types';

const UserInfoView = ({ userInfo, handleClickSwitchEditMode }) => (
    <div className="text-center">
        <div className="container mb-4">
            {userInfo.avatarPath ?
                <div className="col-md-4 offset-md-4">
                    <img className="img-fluid" src={userInfo.avatarPath} alt="&#128565;" />
                </div> :
                <Fragment />}
            <h2>{userInfo.nickName}</h2>
            <h6>{userInfo.experience} опыта</h6>
        </div>
        <button type="button" className="btn btn-outline-primary" onClick={handleClickSwitchEditMode}>Редактировать</button>
    </div>
);

UserInfoView.propTypes = {
    userInfo: PropTypes.object.isRequired,
    handleClickSwitchEditMode: PropTypes.func.isRequired
};

export default UserInfoView;