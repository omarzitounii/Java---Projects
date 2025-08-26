/**
 * @author Omar Zitouni
 * Implementation of Game interface
 */

package game;

import framework.Game;
import framework.Deck;
import framework.GameException;

import java.util.*;
import java.io.IOException;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyGame implements Game {

    private final String gameName;
    private final Set<String> cards = new LinkedHashSet<>();
    private final Map<String, String> properties = new HashMap<>();
    private final Map<String, Map<String, String>> cardStringProperties = new HashMap<>();
    private final Map<String, Map<String, Integer>> cardIntProperties = new HashMap<>();
    private final Map<String, String> intPropertyRules = new HashMap<>();
    private final Map<String, Map<String, Set<String>>> stringPropertyRules = new HashMap<>();

    /**
     * Fields Getters
     */
    public String getGameName() {
        return gameName;
    }

    public Set<String> getCards() {
        return cards;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public Map<String, Map<String, String>> getCardStringProperties() {
        return cardStringProperties;
    }

    public Map<String, Map<String, Integer>> getCardIntProperties() {
        return cardIntProperties;
    }

    public Map<String, String> getIntPropertyRules() {
        return intPropertyRules;
    }

    public Map<String, Map<String, Set<String>>> getStringPropertyRules() {
        return stringPropertyRules;
    }

    public MyGame(String name) throws GameException {
        if (name == null || name.isEmpty()) {
            throw new GameException("Game name must not be null or empty");
        }
        this.gameName = name;
    }

    /**
     * Loads a game definition from a .game file and returns a fully initialized MyGame instance
     *
     * @param path the file to read
     * @return the reconstructed game
     * @throws GameException if the file is malformed or I/O fails
     */
    public static MyGame loadGame(String path) throws GameException {
        if (path == null || path.isEmpty()) {
            throw new GameException("path must not be null or empty");
        }
        Path p = Paths.get(path);
        final List<String> lines;
        try {
            lines = Files.readAllLines(p, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new GameException("Failed to read file: " + e.getMessage());
        }
        if (lines.isEmpty()) {
            throw new GameException("Game file is empty");
        }
        String first = lines.get(0);
        if (!first.startsWith("Game: ")) {
            throw new GameException("First line must start with 'Game: '");
        }
        String gameName = first.substring("Game: ".length());
        if (gameName.isEmpty()) {
            throw new GameException("Game name must not be empty");
        }

        MyGame game = new MyGame(gameName);

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null) continue;
            if (line.isBlank()) continue;

            if (line.startsWith("Card: ")) {
                String card = line.substring("Card: ".length());
                if (card.isEmpty()) {
                    throw new GameException("Empty card name at line: " + (i+1));
                }
                game.defineCard(card);
            } else if (line.startsWith("Property: ")) {
                String rest = line.substring("Property: ".length());
                String[] parts = rest.split(" \\| ", -1);
                if (parts.length != 2) {
                    throw new GameException("Malformed property at line: " + (i+1));
                }
                String prop = parts[0];
                String type = parts[1];
                if (prop.isEmpty() || type.isEmpty()) {
                    throw new GameException("Empty property name/type at line: " + (i+1));
                }
                game.defineProperty(prop, type);
            } else if (line.startsWith("CardProperty: ")) {
                String rest = line.substring("CardProperty: ".length());
                String[] parts = rest.split(" \\| ", -1);
                if (parts.length != 3) {
                    throw new GameException("Malformed CardProperty at line: " + (i+1));
                }
                String card = parts[0];
                String prop = parts[1];
                String value = parts[2];
                if (card.isEmpty() || prop.isEmpty() || value.isEmpty()) {
                    throw new GameException("Empty CardProperty token at line " + (i + 1));
                }
                String propType = game.properties.get(prop);
                if (propType == null) {
                    throw new GameException("Property not defined before at line " + (i + 1));
                }
                if ("integer".equals(propType)) {
                    final int intVal;
                    try {
                        intVal = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        throw new GameException("Invalid integer value '" + value + "' at line " + (i + 1));
                    }
                    game.setProperty(card, prop, intVal);
                } else if("string".equals(propType)) {
                    game.setProperty(card, prop, value);
                } else {
                    throw new GameException("Unknown property type for '" + prop + "' at line " + (i + 1));
                }
            } else if (line.startsWith("GameRuleInteger: ")) {
                String rest = line.substring("GameRuleInteger: ".length());
                String[] parts =  rest.split(" \\| ", -1);
                if (parts.length != 2) {
                    throw new GameException("Malformed GameRuleInteger at line: " + (i+1));
                }
                String prop = parts[0];
                String op = parts[1];
                if (prop.isEmpty() || op.isEmpty()) {
                    throw new GameException("Empty integer rule token at line " + (i + 1));
                }
                game.defineRule(prop, op);
            } else if (line.startsWith("GameRuleString: ")) {
                String rest = line.substring("GameRuleString: ".length());
                String[] parts = rest.split(" \\| ", -1);
                if (parts.length != 3) throw new GameException("Malformed GameRuleString line at " + (i + 1));
                String prop   = parts[0];
                String winner = parts[1];
                String loser  = parts[2];
                if (prop.isEmpty() || winner.isEmpty() || loser.isEmpty()) {
                    throw new GameException("Empty string rule token at line " + (i + 1));
                }
                game.defineRule(prop, winner, loser);
            } else {
                throw new GameException("Unknown line prefix at " + (i + 1) + ": " + line);
            }
        }
        return game;
    }

    /**
     * Defines a new card in the game
     *
     * @param name the name of the card to define
     * @throws GameException if the name is invalid or the card already exists
     */
    @Override
    public void defineCard(String name) throws GameException {
        if (name == null || name.isEmpty()) {
            throw new GameException("Card name must not be null or empty!");
        }
        if (!cards.add(name)) {
            throw new GameException("Card name already exists!");
        }
    }

    /**
     * Defines a new property that cards can have
     *
     * @param name the name of the property
     * @param type the type of the property, must be "integer" or "string"
     * @throws GameException if arguments are invalid or property already exists
     */
    @Override
    public void defineProperty(String name, String type) throws GameException {
        if (name == null || name.isEmpty()) {
            throw new GameException("Property name must not be null or empty!");
        }
        if (type == null || type.isEmpty()) {
            throw new GameException("Property type must not be null or empty!");
        }
        if (!type.equals("integer") && !type.equals("string")) {
            throw new GameException("Invalid Property type: " + type);
        }
        if (properties.containsKey(name)) {
            throw new GameException("Property already defined: " + name);
        }
        properties.put(name, type);
    }

    /**
     * Sets a string property value for a given card
     *
     * @param cardName     the name of the card
     * @param propertyName the property to assign
     * @param value        the string value for the property
     * @throws GameException if the card/property do not exist, types mismatch,
     *                       or the property is already set for this card
     */
    @Override
    public void setProperty(String cardName, String propertyName, String value) throws GameException {
        if (cardName == null || cardName.isEmpty()) {
            throw new GameException("Card name must not be null or empty!");
        }
        if (propertyName == null || propertyName.isEmpty()) {
            throw new GameException("Property name must not be null or empty!");
        }
        if (value == null || value.isEmpty()) {
            throw new GameException("Value must not be null or empty!");
        }
        if (!cards.contains(cardName)) {
            throw new GameException("Card does not exist!");
        }
        String type = properties.get(propertyName);
        if (type == null) {
            throw new GameException("Property not defined: " + propertyName);
        }
        if (!"string".equals(type)) {
            throw new GameException("Property is not of type string: " + type);
        }
        Map<String, String> map = cardStringProperties.get(cardName);
        if (map == null) {
            map = new HashMap<>();
            cardStringProperties.put(cardName, map);
        }
        if (map.containsKey(propertyName)) {
            throw new GameException("Property already set for card: " + cardName + "." + propertyName);
        }
        map.put(propertyName, value);
    }

    /**
     * Sets an integer property value for a given card
     *
     * @param cardName     the name of the card
     * @param propertyName the property to assign
     * @param value        the integer value for the property
     * @throws GameException if the card/property do not exist, types mismatch,
     *                       or the property is already set for this card
     */
    @Override
    public void setProperty(String cardName, String propertyName, int value) throws GameException {
        if (cardName == null || cardName.isEmpty()) {
            throw new GameException("Card name must not be null or empty");
        }
        if (propertyName == null || propertyName.isEmpty()) {
            throw new GameException("Property name must not be null or empty");
        }
        if (!cards.contains(cardName)) {
            throw new GameException("Card not defined: " + cardName);
        }
        String type = properties.get(propertyName);
        if (type == null) {
            throw new GameException("Property not defined: " + propertyName);
        }
        if (!"integer".equals(type)) {
            throw new GameException("Property is not of type integer: " + propertyName);
        }
        Map<String, Integer> map = cardIntProperties.get(cardName);
        if (map == null) {
            map = new HashMap<>();
            cardIntProperties.put(cardName, map);
        }
        if (map.containsKey(propertyName)) {
            throw new GameException("Property already set for card: " + cardName + "." + propertyName);
        }
        map.put(propertyName, value);
    }

    /**
     * Defines a comparison rule for an integer property
     * The operation must be ">" (greater wins) or "<" (smaller wins)
     * Only one integer rule may be defined per property (no duplicates or contradictions)
     *
     * @param propertyName the name of the property (must exist and be of type "integer")
     * @param operation    the operation, either ">" or "<"
     * @throws GameException if inputs are null/empty, the property does not exist,
     *                       the property is not of type integer, the operation is invalid,
     *                       or a rule for this property already exists
     */
    @Override
    public void defineRule(String propertyName, String operation) throws GameException {
        if (propertyName == null || propertyName.isEmpty()) {
            throw new GameException("Property name must not be null or empty!");
        }
        if (operation == null || operation.isEmpty()) {
            throw new GameException("Operation must not be null or empty!");
        }
        if (!operation.equals(">") && !operation.equals("<")) {
            throw new GameException("Invalid operation for integer rule: " + operation);
        }
        String type = properties.get(propertyName);
        if (type == null) {
            throw new GameException("Property not defined: " + propertyName);
        }
        if (!"integer".equals(type)) {
            throw new GameException("Property is not of type integer: " + propertyName);
        }
        intPropertyRules.put(propertyName, operation);
    }

    /**
     * Defines a dominance rule for a string property: winningName beats losingName
     * Opposite rules (A beats B and B beats A) are allowed; exact duplicates are not
     *
     * @param propertyName the name of the property (must exist and be of type "string")
     * @param winningName  the winning value for the property
     * @param losingName   the losing value for the property (must differ from winningName)
     * @throws GameException if inputs are null/empty, the property does not exist,
     *                       the property is not of type string, winningName equals losingName,
     *                       or the identical rule already exists
     */
    @Override
    public void defineRule(String propertyName, String winningName, String losingName) throws GameException {
        if (propertyName == null || propertyName.isEmpty()) {
            throw new GameException("Property name must not be null or empty");
        }
        if (winningName == null || winningName.isEmpty() || losingName == null || losingName.isEmpty()) {
            throw new GameException("Winning/losing names must not be null or empty");
        }
        if (winningName.equals(losingName)) {
            throw new GameException("A value cannot beat itself for property: " + propertyName);
        }
        String type = properties.get(propertyName);
        if (type == null) {
            throw new GameException("Property not defined: " + propertyName);
        }
        if (!"string".equals(type)) {
            throw new GameException("Property is not of type string: " + propertyName);
        }
        Map<String, Set<String>> winnersMap = stringPropertyRules.get(propertyName);
        if (winnersMap == null) {
            winnersMap = new HashMap<>();
            stringPropertyRules.put(propertyName, winnersMap);
        }
        Set<String> losers = winnersMap.get(winningName);
        if (losers == null) {
            losers = new HashSet<>();
            winnersMap.put(winningName, losers);
        }
        if (losers.contains(losingName)) {
            throw new GameException("Duplicate string rule for property: " + propertyName + " (" + winningName + " > " + losingName + ")");
        }
        losers.add(losingName);
    }

    /**
     * Returns information from the game definition
     *
     * @param type the category to query ("game", "card", "property", or "rule")
     * @param name the specific name or "*" for all
     * @return matching entries as an array (empty if none)
     * @throws GameException if arguments are invalid
     */
    @Override
    public String[] get(String type, String name) throws GameException {
        if  (type == null || type.isEmpty()) {
            throw new GameException("Type must not be null or empty!");
        }
        if (name == null || name.isEmpty()) {
            throw new GameException("Name must not be null or empty!");
        }
        if (!(type.equals("game") || type.equals("card") || type.equals("property") || type.equals("rule"))) {
            throw new GameException("Type must be game, card, property, or rule!");
        }
        if ("game".equals(type)) {
            return new String[] { gameName };
        }
        if ("card".equals(type)) {
            if ("*".equals(name)) {
                return cards.toArray(new String[0]);
            }
            return cards.contains(name) ? new String[] { name } : new String[0];
        }
        if ("property".equals(type)) {
            if ("*".equals(name)) {
                return properties.keySet().toArray(new String[0]);
            }
            return properties.containsKey(name) ? new String[] { name } : new String[0];
        }
        if ("rule".equals(type)) {
            ArrayList<String> rules = new ArrayList<>();

            // integer rules: "<property><op>"
            for (Map.Entry<String, String> e : intPropertyRules.entrySet()) {
                rules.add(e.getKey() + e.getValue());
            }

            // string rules: "<property>:<winner>><loser>"
            for (Map.Entry<String, Map<String, Set<String>>> e : stringPropertyRules.entrySet()) {
                String prop = e.getKey();
                Map<String, Set<String>> winners = e.getValue();
                if (winners != null) {
                    for (Map.Entry<String, Set<String>> w : winners.entrySet()) {
                        String winner = w.getKey();
                        Set<String> losers = w.getValue();
                        if (losers != null) {
                            for (String loser : losers) {
                                rules.add(prop + ":" + winner + ">" + loser);
                            }
                        }
                    }
                }
            }

            if ("*".equals(name)) {
                return rules.toArray(new String[0]);
            } else {
                // exact match
                for (String r : rules) {
                    if (r.equals(name)) {
                        return new String[] { r };
                    }
                }
                return new String[0];
            }
        }
        return new String[0];
    }

    /**
     * Saves the current game definition to a text file
     *
     * Example format:
     * Game: Magic
     * Card: Emrakul
     * Property: power | integer
     * Property: type | string
     * CardProperty: Emrakul | power | 15
     * CardProperty: Emrakul | type | Eldrazi
     * GameRuleInteger: power | >
     * GameRuleString: type | Eldrazi | Human
     *
     * @throws GameException if the path is invalid or writing fails
     */
    @Override
    public void saveToFile() throws GameException {
        // Default directory "games" at project root
        Path dir = Paths.get("games");
        try {
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            Path p = dir.resolve(gameName + ".game");
            try(BufferedWriter out = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
                String nl = System.lineSeparator();
                // 1) Game header
                out.write("Game: " + gameName + nl);
                // 2) Cards
                for (String card : cards) {
                    out.write("Card: " + card + nl);
                }
                // 3) Properties
                for (Map.Entry<String, String> e : properties.entrySet()) {
                    out.write("Property: " + e.getKey() + " | " + e.getValue() + nl);
                }
                // 4) CardProperty
                // Integers
                for (String card : cards) {
                    Map<String, Integer> map = cardIntProperties.get(card);
                    if (map != null) {
                        for (Map.Entry<String, Integer> e : map.entrySet()) {
                            out.write("CardProperty: " + card + " | " + e.getKey() + " | " + e.getValue() + nl);
                        }
                    }
                }
                // Strings
                for (String card : cards) {
                    Map<String, String> map = cardStringProperties.get(card);
                    if (map != null) {
                        for (Map.Entry<String, String> e : map.entrySet()) {
                            out.write("CardProperty: " + card + " | " + e.getKey() + " | " + e.getValue() + nl);
                        }
                    }
                }
                // 5) Integer rules
                for (Map.Entry<String, String> e : intPropertyRules.entrySet()) {
                    out.write("GameRuleInteger: " + e.getKey() + " | " + e.getValue() + nl);
                }

                // 6) String rules
                for (Map.Entry<String, Map<String, Set<String>>> e : stringPropertyRules.entrySet()) {
                    String prop = e.getKey();
                    Map<String, Set<String>> winners = e.getValue();
                    if (winners != null) {
                        for (Map.Entry<String, Set<String>> w : winners.entrySet()) {
                            String winner = w.getKey();
                            Set<String> losers = w.getValue();
                            if (losers != null) {
                                for (String loser : losers) {
                                    out.write("GameRuleString: " + prop + " | " + winner + " | " + loser + nl);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                throw new GameException("Failed to save file: " + e.getMessage());
            }
        } catch (IOException e) {
            throw new GameException("Failed to save file: " + e.getMessage());
        }

    }

    /**
     * Creates a new, empty deck instance associated with this game
     *
     * @return a new Deck linked to this Game
     */
    @Override
    public Deck createDeck() {
        return new MyDeck(this);
    }

    /**
     * Compares two cards using all defined rules.
     * > 0  → cardA beats cardB
     * = 0  → draw
     * < 0  → cardB beats cardA
     *
     * @throws GameException if either card is unknown
     */
    public int compareCards(String cardA, String cardB) throws GameException {
        if (cardA == null || cardA.isEmpty() || cardB == null || cardB.isEmpty()) {
            throw new GameException("Card name must not be empty or null");
        }
        if (!cards.contains(cardA)) {
            throw new GameException("Card not defined: " + cardA);
        }
        if (!cards.contains(cardB)) {
            throw new GameException("Card not defined: " + cardB);
        }
        if (cardA.equals(cardB)) {
            return 0;
        }

        int winsA = 0;
        int winsB = 0;

        // Integer Rules
        for (Map.Entry<String, String> e : intPropertyRules.entrySet()) {
            String prop = e.getKey();
            String op = e.getValue();

            Map<String, Integer> propValA = cardIntProperties.get(cardA);
            Map<String, Integer> propValB = cardIntProperties.get(cardB);
            if (propValA == null || propValB == null) continue;

            Integer ValA = propValA.get(prop);
            Integer ValB = propValB.get(prop);
            if (ValA == null || ValB == null) continue;

            if (ValA.intValue() == ValB.intValue()) {
                continue;
            } else if (">".equals(op)) {
                if (ValA > ValB) winsA++; else winsB++;
            } else if ("<".equals(op)) {
                if (ValA < ValB) winsA++; else winsB++;
            }
        }

        // String Rules
        for (Map.Entry<String, Map<String, Set<String>>> e : stringPropertyRules.entrySet()) {
            String prop = e.getKey();
            Map<String, Set<String>> winnersMap = e.getValue();

            Map<String, String> propValA = cardStringProperties.get(cardA);
            Map<String, String> propValB = cardStringProperties.get(cardB);

            if (propValA == null || propValB == null) continue;

            String ValA = propValA.get(prop);
            String ValB = propValB.get(prop);
            if (ValA == null || ValB == null) continue;

            Set<String> aWinsAgainst = winnersMap == null ? null : winnersMap.get(ValA);
            Set<String> bWinsAgainst = winnersMap == null ? null : winnersMap.get(ValB);

            boolean aBeatsB = (aWinsAgainst != null && aWinsAgainst.contains(ValB));
            boolean bBeatsA = (bWinsAgainst != null && bWinsAgainst.contains(ValA));

            if (aBeatsB && !bBeatsA) winsA++;
            if (bBeatsA && !aBeatsB) winsB++;
        }

        return winsA - winsB;
    }

}
