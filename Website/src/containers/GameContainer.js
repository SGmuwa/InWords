import React, { Component } from 'react';
import { connect } from 'react-redux';
import { GameActions } from '../actions/GameActions';
import { ErrorActions } from '../actions/ErrorActions';
import WrapperWithErrorHandling from '../components/WrapperWithErrorHandling';
import Game from '../components/Game';
import GameInfoCard from '../components/GameInfoCard';
import GameLevelCard from '../components/GameLevelCard';
import GameField from '../components/GameField';

class GameContainer extends Component {
    render() {
        const {
            errorMessage,
            gamesInfo,
            gameInfo,
            gameLevel,
            pullGamesInfoAction,
            pullGameInfoAction,
            pullGameLevelAction,
            completeGameAction,
            resetErrorMessageAction
        } = this.props;

        function SmartGameInfoCard({ gameInfo }) {
            return <GameInfoCard
                gameInfo={gameInfo}
                pullGameInfo={pullGameInfoAction}
            />;
        }

        function SmartGameLevelCard({ levelInfo }) {
            return <GameLevelCard
                levelInfo={levelInfo}
                pullGameLevel={pullGameLevelAction}
            />;
        }

        function SmartGameField({ gameLevel }) {
            return <GameField
                gameLevel={gameLevel}
                completeGame={completeGameAction}
            />;
        }

        return (
            <WrapperWithErrorHandling
                errorMessage={errorMessage}
                resetErrorMessage={resetErrorMessageAction} >
                <Game
                    smartGameInfoCard={SmartGameInfoCard}
                    smartGameLevelCard={SmartGameLevelCard}
                    smartGameField={SmartGameField}
                    gamesInfo={gamesInfo}
                    gameInfo={gameInfo}
                    gameLevel={gameLevel}
                    pullGamesInfo={pullGamesInfoAction}
                />
            </WrapperWithErrorHandling>
        );
    }
}

const mapStateToProps = (store) => {
    return {
        gamesInfo: store.game.gamesInfo,
        gameInfo: store.game.gameInfo,
        gameLevel: store.game.gameLevel,
        errorMessage: store.errorMessage
    };
};

const mapDispatchToProps = (dispatch) => {
    return {
        pullGamesInfoAction: () => dispatch(GameActions.pullGamesInfo()),
        pullGameInfoAction: (gameId) => dispatch(GameActions.pullGameInfo(gameId)),
        pullGameLevelAction: (levelId) => dispatch(GameActions.pullGameLevel(levelId)),
        completeGameAction: () => dispatch(GameActions.completeGame()),
        resetErrorMessageAction: () => dispatch(ErrorActions.resetErrorMessage())
    };
};

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(GameContainer);