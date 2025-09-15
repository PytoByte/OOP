package ru.nsu.vmarkidonov;

import org.junit.jupiter.api.Test;

class GameCardTest {

    @Test
    void initPrimaryValue() {
        GameCard gameCard = new GameCard(CardValues.TWO, CardSuits.CLUBS);
        gameCard.hidden = false;

        assert gameCard.getValue() == 0;
        gameCard.initValue(0);
        assert gameCard.getValue() == CardValues.TWO.primaryValue;
    }

    @Test
    void initAlternativeValue() {
        GameCard gameCard = new GameCard(CardValues.ACE, CardSuits.CLUBS);
        gameCard.hidden = false;

        assert gameCard.getValue() == 0;
        gameCard.initValue(20);
        assert gameCard.getValue() == 1;
    }

    @Test
    void initPrimaryButHasAlternativeValue() {
        GameCard gameCard = new GameCard(CardValues.ACE, CardSuits.CLUBS);
        gameCard.hidden = false;

        assert gameCard.getValue() == 0;
        gameCard.initValue(0);
        assert gameCard.getValue() == 11;
    }

    @Test
    void initAlternativeButHasOnlyPrimaryValue() {
        GameCard gameCard = new GameCard(CardValues.JACK, CardSuits.CLUBS);
        gameCard.hidden = false;

        assert gameCard.getValue() == 0;
        gameCard.initValue(20);
        assert gameCard.getValue() == 10;
    }

    @Test
    void initValueButCardHidden() {
        GameCard gameCard = new GameCard(CardValues.JACK, CardSuits.CLUBS);

        assert gameCard.getValue() == 0;
        gameCard.initValue(0);
        assert gameCard.getValue() == 0;
    }

    @Test
    void restore() {
        GameCard gameCard = new GameCard(CardValues.JACK, CardSuits.CLUBS);
        gameCard.hidden = false;
        gameCard.initValue(0);

        gameCard.restore();

        assert gameCard.hidden;
        assert gameCard.getValue() == 0;
    }
}