package ru.nsu.vmarkidonov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameCardTest {

    @Test
    void initPrimaryValue() {
        GameCard gameCard = new GameCard(CardValues.TWO, CardSuits.CLUBS);
        gameCard.hidden = false;

        assertEquals(0, gameCard.getValue());
        gameCard.initValue(0);
        assertEquals(CardValues.TWO.primaryValue, gameCard.getValue());
    }

    @Test
    void initAlternativeValue() {
        GameCard gameCard = new GameCard(CardValues.ACE, CardSuits.CLUBS);
        gameCard.hidden = false;

        assertEquals(0, gameCard.getValue());
        gameCard.initValue(20);
        assertEquals(CardValues.ACE.alternativeValue, gameCard.getValue());
    }

    @Test
    void initPrimaryButHasAlternativeValue() {
        GameCard gameCard = new GameCard(CardValues.ACE, CardSuits.CLUBS);
        gameCard.hidden = false;

        assertEquals(0, gameCard.getValue());
        gameCard.initValue(0);
        assertEquals(CardValues.ACE.primaryValue, gameCard.getValue());
    }

    @Test
    void initAlternativeButHasOnlyPrimaryValue() {
        GameCard gameCard = new GameCard(CardValues.JACK, CardSuits.CLUBS);
        gameCard.hidden = false;

        assertEquals(0, gameCard.getValue());
        gameCard.initValue(20);
        assertEquals(CardValues.JACK.primaryValue, gameCard.getValue());
    }

    @Test
    void getValueButCardHidden() {
        GameCard gameCard = new GameCard(CardValues.JACK, CardSuits.CLUBS);

        assertEquals(0, gameCard.getValue());
        gameCard.initValue(0);
        assertEquals(0, gameCard.getValue());
    }

    @Test
    void restore() {
        GameCard gameCard = new GameCard(CardValues.JACK, CardSuits.CLUBS);
        gameCard.hidden = false;
        gameCard.initValue(0);

        gameCard.restore();
        assertTrue(gameCard.hidden);
        assertEquals(0, gameCard.getValue());
    }
}