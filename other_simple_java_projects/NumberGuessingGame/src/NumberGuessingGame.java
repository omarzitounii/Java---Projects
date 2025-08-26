import java.sql.SQLOutput;
import java.util.Scanner;

public class NumberGuessingGame {
    public static void main(String[] args) {
        int numberToGuess;
        if (args.length >= 2) {
            System.out.println("Please enter only one number!");
        } else if (args.length == 1) {
            try {
                numberToGuess = Integer.parseInt(args[0]);
                if (numberToGuess > 100 || numberToGuess < 1) {
                    System.out.println("Please enter a number between 1 and 100!");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter an integer!");
                return;
            }
            computerGuess(numberToGuess);
        } else {
            playerGuess();
        }
    }

    public static void computerGuess(int numberToGuess) {
        int attempts = 0;
        int guess;
        do {
            guess = (int) (Math.random() * 100) + 1;
            attempts++;
        } while (guess != numberToGuess);
        System.out.println("Computer guessed the number " + numberToGuess + " in " + attempts + " attempts.");
    }

    public static void playerGuess() {
        int numberToGuess = (int) (Math.random() * 100) + 1;
        Scanner input = new Scanner(System.in);
        System.out.println("I picked a number between 1 and 100. Try to guess it!");
        int guess = 0;
        while (guess != numberToGuess) {
            System.out.println("Enter your guess:");
            if (input.hasNextInt()) {
                guess = input.nextInt();
                if (guess < numberToGuess) {
                    System.out.println("too small!");
                } else if (guess > numberToGuess) {
                    System.out.println("too big!");
                } else {
                    System.out.println("Correct! the number was: " + numberToGuess);
                }
            } else {
                System.out.println("Please enter a valid number!");
                input.next();
            }
        }
        input.close();
    }

}
