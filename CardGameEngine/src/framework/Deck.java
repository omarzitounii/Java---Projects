package framework;

public interface Deck {
    void addCard(String cardName) throws GameException;

    String[] getAllCards();

    String[] getMatchingCards(String propertyName, int value) throws GameException;
    String[] getMatchingCards(String propertyName, String value) throws GameException;

    String[] selectBeatingCards(String opponentCard) throws GameException;
}
