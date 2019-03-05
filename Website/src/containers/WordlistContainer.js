import React, { Component } from 'react';
import { connect } from 'react-redux';
import { WordlistActions } from '../actions/WordlistActions';
import { ErrorActions } from '../actions/ErrorActions';
import { WrapperWithErrorHandling } from '../components/WrapperWithErrorHandling';
import { WordlistTools } from '../components/WordlistTools';
import { Wordlist } from '../components/Wordlist';
import { WordPair } from '../components/WordPair';

class WordlistContainer extends Component {
    render() {
        const { accessToken, wordPairs, pullWordPairsAction,
            deleteWordPairAction, addWordPairAction, editWordPairAction,
            errorMessage, resetErrorMessageAction } = this.props;

        const SmartWordPair = ({ id, wordForeign, wordNative }) =>
            <WordPair
                id={id}
                wordForeign={wordForeign}
                wordNative={wordNative}
                accessToken={accessToken}
                deleteWordPair={deleteWordPairAction}
                editWordPair={editWordPairAction} />;

        return (
            <WrapperWithErrorHandling
                errorMessage={errorMessage}
                resetErrorMessage={resetErrorMessageAction}>

                <WordlistTools
                    accessToken={accessToken}
                    addWordPair={addWordPairAction} />

                <Wordlist
                    smartWordPair={SmartWordPair}
                    accessToken={accessToken}
                    wordPairs={wordPairs}
                    pullWordPairs={pullWordPairsAction} />

            </WrapperWithErrorHandling>
        );
    }
}

const mapStateToProps = store => {
    return {
        accessToken: store.accessToken,
        wordPairs: store.wordlist.wordPairs,
        errorMessage: store.errorMessage
    };
};

const mapDispatchToProps = dispatch => {
    return {
        pullWordPairsAction: (token) => dispatch(WordlistActions.pullWordPairs(token)),
        deleteWordPairAction: (token, pairId) => dispatch(WordlistActions.deleteWordPair(token, pairId)),
        addWordPairAction: (token, wordPair) => dispatch(WordlistActions.addWordPair(token, wordPair)),
        editWordPairAction: (token, pairId, wordPair) =>
            dispatch(WordlistActions.editWordPair(token, pairId, wordPair)),
        resetErrorMessageAction: () => dispatch(ErrorActions.resetErrorMessage())
    };
};

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(WordlistContainer);