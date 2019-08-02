import React, { useCallback, useEffect, memo } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { receiveWordPairs as receiveWordPairsAction } from 'actions/wordPairsApiActions';
import useCheckboxList from 'hooks/useCheckboxList';
import Wordlist from './Wordlist';

function WordlistContainer() {
  const wordPairs = useSelector(store => store.wordPairs);

  const dispatch = useDispatch();
  const receiveWordPairs = useCallback(
    () => dispatch(receiveWordPairsAction()),
    [dispatch]
  );

  useEffect(() => {
    if (!wordPairs.length) {
      receiveWordPairs();
    }
  }, [wordPairs.length, receiveWordPairs]);

  const { checked, handleToggle, handleReset } = useCheckboxList();

  useEffect(() => {
    handleReset();
  }, [handleReset, wordPairs]);

  return (
    <Wordlist
      wordPairs={wordPairs}
      checked={checked}
      handleToggle={handleToggle}
      handleReset={handleReset}
    />
  );
}

export default memo(WordlistContainer);
