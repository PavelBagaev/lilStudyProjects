package bullscows;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class BullsAndCowsGame {

    static private final StringBuilder digitsAndLetters = new StringBuilder("0123456789abcdefghijklmnopqrstuvwxyz");
    static private StringBuilder digitsAndLettersToTakeFrom = new StringBuilder("0123456789abcdefghijklmnopqrstuvwxyz");
    static private int charSetLength;

    public static void main(String[] args) {
        bullsAndCows(randomCodeGenerator());
    }

    static StringBuilder randomCodeGenerator() {
            Scanner scanner = new Scanner(System.in);
            StringBuilder randomCode = new StringBuilder();
            Random rand = new Random();
            int length = 0;

            try {
                System.out.println("Input the length of the secret code:");
                length = scanner.nextInt();
                if (length == 0) {
                    System.out.printf("Error: \"%d\" isn't a valid number.", length);
                    System.exit(0);
                }
            } catch (InputMismatchException e) {
                System.out.printf("Error: \"%s\" isn't a valid number.", Integer.toString(length));
                System.exit(0);
            }

            System.out.println("Input the number of possible symbols in the code:");
            charSetLength = scanner.nextInt();

            if (charSetLength > 36) {
               System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
               System.exit(0);
            }

            if (charSetLength < length) {
                System.out.printf("Error: it's not possible to generate a code " +
                        "with a length of %d with %d unique symbols.", length, charSetLength);
                System.exit(0);
            }

            int charSetLengthCounter = charSetLength;

            for (int i = 0; i < length; i++) {
                int randomIndex = rand.nextInt(charSetLengthCounter);
                randomCode.append(digitsAndLettersToTakeFrom.charAt(randomIndex));
                digitsAndLettersToTakeFrom.deleteCharAt(randomIndex);
                charSetLengthCounter--;
            }
            return randomCode;
    }

    static void bullsAndCows (StringBuilder randomCode) {
        Scanner scanner = new Scanner(System.in);
        String code = randomCode.toString();
        int turnCounter = 1;
        int bulls;
        int cows;
        System.out.printf("The secret is prepared: ");
        for (int i = 0; i < randomCode.length(); i++) {
            System.out.printf("*");
        }
        if (charSetLength == 1) {
            System.out.printf(" (%c).\n", digitsAndLetters.charAt(0));
        } else if (charSetLength > 1 && charSetLength <= 10) {
            System.out.printf(" (%c-%c).\n", digitsAndLetters.charAt(0),
                    digitsAndLetters.charAt(charSetLength - 1));
        } else if (charSetLength == 11) {
            System.out.printf(" (%c-%c, %c).\n", digitsAndLetters.charAt(0),
                    digitsAndLetters.charAt(9), digitsAndLetters.charAt(charSetLength - 1));
        } else if (charSetLength > 11) {
            System.out.printf(" (%c-%c, %c-%c).\n", digitsAndLetters.charAt(0),
                    digitsAndLetters.charAt(9), digitsAndLetters.charAt(10),
                    digitsAndLetters.charAt(charSetLength - 1));
        }

        System.out.println("Okay, let's start a game!");

        do {
            System.out.printf("Turn %d:\n", turnCounter);
            String guess = scanner.nextLine();
            bulls = 0;
            cows = 0;
            int j = 0;

            while (j < guess.length()) {
                for (int i = 0; i < code.length(); i++) {
                    if (guess.charAt(j) == code.charAt(i)) {
                        if (j == i) {
                            bulls++;
                        } else {
                            cows++;
                        }
                    }
                }
                j++;
            }

            if (cows == 0 && bulls == 0) {
                System.out.printf("Grade: None. \n");
            } else if (cows != 0 && bulls != 0) {
                System.out.printf("Grade: %d bull(s) and %d cow(s). \n", bulls, cows);
            } else if (bulls == 0) {
                System.out.printf("Grade: %d cow(s). \n", cows);
            } else if (cows == 0) {
                System.out.printf("Grade: %d bull(s). \n", bulls);
            }
            turnCounter++;
        } while (bulls != code.length());

        System.out.println("Congratulations! You guessed the secret code.");
    }
}
