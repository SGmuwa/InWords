import React from 'react';
import PropTypes from 'prop-types';
import CardSettingsContext from 'src/components/routes/common/CardSettingsContext';
import FadeAnimation from 'src/components/core/FadeAnimation';
import Fade from 'src/components/core/Fade';
import GameCard from 'src/components/routes/common/GameCard';

import './Game.scss';

function Game({
  wordPairs,
  selectedWordPairs,
  completedPairIdsMap,
  selectedCompletedPairId,
  isGameCompleted,
  handleClick
}) {
  const cols = React.useMemo(() => {
    const colsNum = Math.ceil(Math.sqrt(wordPairs.length));

    if (colsNum <= 2) {
      return 2;
    } else if (colsNum === 3) {
      return 3;
    }
    return colsNum;
  }, [wordPairs.length]);

  const gameCardSettings = React.useContext(CardSettingsContext);

  return (
    <div
      className="game-field"
      style={{
        maxWidth: gameCardSettings.cardDimension * cols + 8 * cols
      }}
    >
      {wordPairs.map(({ id, pairId, word, onSpeech }) => (
        <FadeAnimation key={id}>
          <Fade in={!isGameCompleted}>
            <div>
              <GameCard
                open={
                  completedPairIdsMap[pairId] ||
                  Boolean(
                    selectedWordPairs.find(
                      selectedWordInfo => selectedWordInfo.id === id
                    )
                  )
                }
                dimension={gameCardSettings.cardDimension}
                textSize={gameCardSettings.cardTextSize}
                onClick={handleClick(pairId, id, onSpeech)}
                depthShadow={selectedCompletedPairId === pairId ? 16 : 4}
              >
                {word}
              </GameCard>
            </div>
          </Fade>
        </FadeAnimation>
      ))}
    </div>
  );
}

Game.propTypes = {
  wordPairs: PropTypes.arrayOf(
    PropTypes.exact({
      id: PropTypes.string.isRequired,
      pairId: PropTypes.number.isRequired,
      word: PropTypes.string.isRequired,
      onSpeech: PropTypes.func
    }).isRequired
  ).isRequired,
  selectedWordPairs: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired
    }).isRequired
  ).isRequired,
  completedPairIdsMap: PropTypes.objectOf(PropTypes.bool.isRequired).isRequired,
  selectedCompletedPairId: PropTypes.number.isRequired,
  isGameCompleted: PropTypes.bool.isRequired,
  handleClick: PropTypes.func.isRequired
};

export default Game;
