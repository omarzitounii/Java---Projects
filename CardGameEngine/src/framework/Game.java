package framework;

public interface Game {
    void defineCard(String name) throws GameException;

    void defineProperty(String name, String type) throws GameException;
    void setProperty(String cardName, String propertyName, String value) throws GameException;
    void setProperty(String cardName, String propertyName, int value) throws GameException;

    void defineRule(String propertyName, String operation) throws GameException;
    void defineRule(String propertyName, String winningName, String losingName) throws GameException;

    String[] get(String type, String name) throws GameException;

    void saveToFile() throws GameException;
    Deck createDeck();
}

