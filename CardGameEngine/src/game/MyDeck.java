/**
 * @author Omar Zitouni
 * Implementation of Deck interface
 */

package game;

import framework.Deck;
import framework.GameException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyDeck implements Deck {

    private final MyGame game;
    private final List<String> deckCards = new ArrayList<>();

    public MyDeck(MyGame game) {
        this.game = game;
    }

    /**
     * Adds a card to this deck
     *
     * @param cardName the name of the card to add
     * @throws GameException if the card name is invalid or not defined in the game
     */
    @Override
    public void addCard(String cardName) throws GameException {
        if (cardName == null || cardName.isEmpty()) {
            throw new GameException("Card name must not be empty or null");
        }
        if (!game.getCards().contains(cardName)) {
            throw new GameException("Card " + cardName + " is not defined in the game");
        }
        deckCards.add(cardName);
    }


    /**
     * Returns all cards currently in this deck, in the order they were added
     *
     * @return an array of card names
     */
    @Override
    public String[] getAllCards() {
        return deckCards.toArray(new String[0]);
    }

    /**
     * Returns all cards in this deck that have the given integer property
     * set to the specified value.
     *
     * @param propertyName the name of the property to check
     * @param value        the integer value to match
     * @return an array of matching card names (empty if none)
     * @throws GameException if the property is not defined or not of type integer
     */
    @Override
    public String[] getMatchingCards(String propertyName, int value) throws GameException {
        if (propertyName == null || propertyName.isEmpty()) {
            throw new GameException("Property name must not be empty or null");
        }
        String type = game.getProperties().get(propertyName);
        if (type == null) {
            throw new GameException("Property not defined");
        }
        if (!"integer".equals(type)) {
            throw new GameException("Property is not of type integer: " + propertyName);
        }
        List<String> matches = new ArrayList<>();
        for (String card : deckCards) {
            Map<String, Integer> props = game.getCardIntProperties().get(card);
            if (props != null) {
                Integer v = props.get(propertyName);
                if (v != null && v == value) {
                    matches.add(card);
                }
            }
        }
        return matches.toArray(new String[0]);
    }


    /**
     * Returns all cards in this deck that have the given string property
     * set to the specified value.
     *
     * @param propertyName the name of the property to check
     * @param value        the string value to match
     * @return an array of matching card names (empty if none)
     * @throws GameException if the property is not defined or not of type string
     */
    @Override
    public String[] getMatchingCards(String propertyName, String value) throws GameException {
        if (propertyName == null || propertyName.isEmpty()) {
            throw new GameException("Property name must not be empty or null");
        }
        if (value == null || value.isEmpty()) {
            throw new GameException("Value must not be empty or null");
        }
        String type = game.getProperties().get(propertyName);
        if (type == null) {
            throw new GameException("Property not defined");
        }
        if(!"string".equals(type)) {
            throw new GameException("Property is not of type string: " + propertyName);
        }
        List<String> matches = new ArrayList<>();
        for (String card : deckCards) {
            Map<String, String> props = game.getCardStringProperties().get(card);
            if (props != null) {
                String v = props.get(propertyName);
                if (v != null && v.equals(value)) {
                    matches.add(card);
                }
            }
        }
        return matches.toArray(new String [0]);
    }


    @Override
    public String[] selectBeatingCards(String opponentCard) throws GameException {
        if (opponentCard == null || opponentCard.isEmpty()) {
            throw new GameException("Opponent card name must not be null or empty");
        }
        if (!game.getCards().contains(opponentCard)) {
            throw new GameException("Opponent card is not defined in the game: " + opponentCard);
        }

        List<String> winners = new ArrayList<>();
        for (String card : deckCards) {
            if (card.equals(opponentCard)) {
                continue;
            }
            int wins = game.compareCards(card, opponentCard);
            if (wins > 0){
                winners.add(card);
            }
        }
        return winners.toArray(new String[0]);
    }
}

