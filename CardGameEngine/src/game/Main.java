package game;

import framework.Deck;
import framework.GameException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    /**
     * Main entry point of the program.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

        System.out.println("Welcome to the card game interactive shell!");
        System.out.println("Type \"help\" for a list of available commands.");

        MyGame game;
        try {
             game = MyGame.loadGame("./games/Magic.game");
            //game = new MyGame("Magic");
        } catch (GameException e) {
            System.err.printf("Failed to create a game: %s%n", e.getMessage());
            return;
        }
        Scanner scanner = new Scanner(System.in);
        Deck deck = null;
        while (true) {
            try {
                System.out.printf("> ");
                String command = scanner.nextLine();
                if (command.equals("help")) {
                    System.out.println("Available commands: definecard, defineproperty, "
                            + "setpropertyinteger, setpropertystring, defineruleinteger, definerulestring, savetofile, get, quit, "
                            + "createdeck, deckaddcard, decklistcards, deckmatchinginteger, "
                            + "deckmatchingstring, deckselectbeatingcards");
                } else if (command.equals("definecard")) {
                    System.out.printf("Name: ");
                    game.defineCard(scanner.nextLine());
                } else if (command.equals("defineproperty")) {
                    System.out.printf("Name: ");
                    String propertyName = scanner.nextLine();
                    System.out.printf("Type ('string' or 'integer'): ");
                    String propertyType = scanner.nextLine();
                    game.defineProperty(propertyName, propertyType);
                } else if (command.equals("setpropertyinteger")) {
                    System.out.printf("Card name: ");
                    String cardName = scanner.nextLine();
                    System.out.printf("Property name: ");
                    String propertyName = scanner.nextLine();

                    int propertyValue = 0;
                    boolean validInput = false;
                    while (!validInput) {
                        System.out.printf("Value: ");
                        try {
                            propertyValue = scanner.nextInt();
                            validInput = true;
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a valid integer!");
                        } finally {
                            scanner.nextLine();
                        }
                    }

                    game.setProperty(cardName, propertyName, propertyValue);
                } else if (command.equals("setpropertystring")) {
                    System.out.printf("Card name: ");
                    String cardName = scanner.nextLine();
                    System.out.printf("Property name: ");
                    String propertyName = scanner.nextLine();
                    System.out.printf("Value: ");
                    String propertyValue = scanner.nextLine();
                    game.setProperty(cardName, propertyName, propertyValue);
                } else if (command.equals("defineruleinteger")) {
                    System.out.printf("Property name: ");
                    String propertyName = scanner.nextLine();
                    System.out.printf("Operation ('>' or '<'): ");
                    String operation = scanner.nextLine();
                    game.defineRule(propertyName, operation);
                } else if (command.equals("definerulestring")) {
                    System.out.printf("Property name: ");
                    String propertyName = scanner.nextLine();
                    System.out.printf("Winning value: ");
                    String winningName = scanner.nextLine();
                    System.out.printf("Losing value: ");
                    String losingName = scanner.nextLine();
                    game.defineRule(propertyName, winningName, losingName);
                } else if (command.equals("savetofile")) {
                    System.out.println("File saved in game directory");
                    game.saveToFile();
                } else if (command.equals("get")) {
                    System.out.printf("Get type ('card', 'property' or 'rule'): ");
                    String getType = scanner.nextLine();
                    System.out.printf("Filter name (* for all): ");
                    String getName = scanner.nextLine();
                    String[] resultArray = game.get(getType, getName);
                    for (String result : resultArray) {
                        System.out.println(result);
                    }
                } else if (command.equals("createdeck")) {
                    if (deck != null) {
                        System.out.println("Replacing previous deck instance.");
                    }
                    deck = game.createDeck();
                } else if (command.equals("deckaddcard")) {
                    if (deck != null) {
                        System.out.printf("Card name: ");
                        String cardName = scanner.nextLine();
                        deck.addCard(cardName);
                    } else {
                        System.err.println("You need to create a deck first.");
                    }
                } else if (command.equals("decklistcards")) {
                    if (deck != null) {
                        String[] cards = deck.getAllCards();
                        for (String card : cards) {
                            System.out.println(card);
                        }
                    } else {
                        System.err.println("You need to create a deck first.");
                    }
                } else if (command.equals("deckmatchinginteger")) {
                    if (deck != null) {
                        System.out.printf("Property name: ");
                        String propertyName = scanner.nextLine();

                        int propertyValue = 0;
                        boolean validInput = false;
                        while (!validInput) {
                            System.out.printf("Value: ");
                            try {
                                propertyValue = scanner.nextInt();
                                validInput = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid integer!");
                            } finally {
                                scanner.nextLine(); // newline
                            }
                        }

                        String[] cards = deck.getMatchingCards(propertyName, propertyValue);
                        for (String card : cards) {
                            System.out.println(card);
                        }
                    } else {
                        System.err.println("You need to create a deck first.");
                    }
                } else if (command.equals("deckmatchingstring")) {
                    if (deck != null) {
                        System.out.printf("Property name: ");
                        String propertyName = scanner.nextLine();
                        System.out.printf("Value: ");
                        String propertyValue = scanner.nextLine();
                        String[] cards = deck.getMatchingCards(propertyName, propertyValue);
                        for (String card : cards) {
                            System.out.println(card);
                        }
                    } else {
                        System.err.println("You need to create a deck first.");
                    }
                } else if (command.equals("deckselectbeatingcards")) {
                    if (deck != null) {
                        System.out.printf("Opponent card name: ");
                        String cardName = scanner.nextLine();
                        String[] cards = deck.selectBeatingCards(cardName);
                        for (String card : cards) {
                            System.out.println(card);
                        }
                    } else {
                        System.err.println("You need to create a deck first.");
                    }
                } else if (command.equals("quit")) {
                    break;
                } else {
                    System.err.printf("Unknown command: %s%n", command);
                }

            } catch (GameException e) {
                System.err.printf("Game error: %s%n", e.getMessage());
            }
        }
        scanner.close();
    }
}