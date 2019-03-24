import React from 'react';
import PropTypes from 'prop-types';

function WordPairView({ wordForeign, wordNative, handleSwitchEditMode, handleDelWordPair }) {
    return (
        <div className="row">
            <div className="col">
                <strong>{wordForeign}</strong>
            </div>
            <div className="col">
                {wordNative}
            </div>
            <div className="col-md-auto">
                <div className="btn-group btn-group-sm" role="group">
                    <button type="button" className="btn btn-outline-primary"
                        onClick={handleSwitchEditMode}>Редактировать</button>
                    <button type="button" className="btn btn-outline-danger"
                        onClick={handleDelWordPair}>Удалить</button>
                </div>
            </div>
        </div>
    );
}

WordPairView.propTypes = {
    wordForeign: PropTypes.string.isRequired,
    wordNative: PropTypes.string.isRequired,
    handleSwitchEditMode: PropTypes.func.isRequired,
    handleDelWordPair: PropTypes.func.isRequired
};

export default WordPairView;
