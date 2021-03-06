package ru.inwords.inwords.game.domain

import ru.inwords.inwords.game.domain.model.WordModel
import ru.inwords.inwords.translation.data.bean.WordTranslation

data class CardsData(val rawWordTranslations: List<WordTranslation>) { //TODO no need for val
    private val _words: List<WordModel>
    private val _wordsMapping: Map<WordModel, WordModel>

    init {
        val wordTranslations = cropWordTranslations(rawWordTranslations)

        _words = ArrayList(wordTranslations.size)
        val wordsMapping = HashMap<WordModel, WordModel>()

        for (wordTranslation in wordTranslations) {
            val wordForeign = WordModel(wordTranslation.serverId, true, wordTranslation.wordForeign)
            val wordNative = WordModel(wordTranslation.serverId, false, wordTranslation.wordNative)

            _words.add(wordForeign)
            _words.add(wordNative)

            wordsMapping[wordForeign] = wordNative
            wordsMapping[wordNative] = wordForeign
        }
        _words.shuffle()

        this._wordsMapping = wordsMapping
    }

    val words: List<WordModel> get() = _words

    fun getCorrespondingWord(word: WordModel): WordModel? = _wordsMapping[word]

    private fun cropWordTranslations(wordTranslations: List<WordTranslation>): List<WordTranslation> {
        return if (wordTranslations.size > 6) {
            wordTranslations.shuffled().subList(0, 6)
        } else {
            wordTranslations
        }
    }
}