package tictactoe;

import java.util.Scanner;
public class TicTacToe {
    public static void main(String[] args) {
        int counter = 1;
        int size = 3;
        int i;
        int j;
        boolean isEmptyCells;
        char[][] array = new char[size][size];
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                array[i][j] = '_';
            }
        }
        printGrid(array, size);
        do {
            counter = userInput(array, size, counter);
            isEmptyCells = isEmptyCells(array, size);
            impossible(array, size);
            xOrOWins(array, size);
            gameNotFinishedOrDraw(array, size, isEmptyCells);
        } while(isEmptyCells);
    }
    public static boolean isEmptyCells(char[][] array, int size) {
        boolean isEmptyCells = false;
        int i;
        int j;
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                if (array[i][j] == '_') {
                    isEmptyCells = true;
                    return isEmptyCells;
                }
            }
        }
        return isEmptyCells;
    }
    public static void printGrid(char[][] array, int size) {
        int i;
        int j;
        System.out.println("---------");
        for (i = 0; i < size; i++) {
            System.out.print("| ");
            for (j = 0; j < size; j++) {
                System.out.printf("%c ", array[i][j]);
            }
            System.out.print("|\n");
        }
        System.out.print("---------\n");
    }
    public static int userInput(char[][] array, int size, int counter) {
        Scanner scanner = new Scanner(System.in);
        boolean correctInput;
        int xCoordinate;
        int yCoordinate;

        do {
            System.out.print("Enter the coordinates: ");
            if(scanner.hasNextInt()) {
                xCoordinate = scanner.nextInt();
            } else {
                System.out.print("You should enter numbers!\n");
                correctInput = false;
                scanner.nextLine();
                scanner.nextLine();
                continue;
            }

            if(scanner.hasNextInt()) {
                yCoordinate = scanner.nextInt();
            } else {
                System.out.print("You should enter numbers!\n");
                scanner.nextLine();
                scanner.nextLine();
                correctInput = false;
                continue;
            }

            if (!(xCoordinate >= 1 && xCoordinate <= 3) || !(yCoordinate >= 1 && yCoordinate <= 3)) {
                System.out.print("Coordinates should be from 1 to 3!\n");
                correctInput = false;
                continue;
            }

            if (array[xCoordinate - 1][yCoordinate - 1] != '_') {
                System.out.print("This cell is occupied! Choose another one!\n");
                correctInput = false;
                continue;
            }

            if (counter % 2 == 0) {
                array[xCoordinate - 1][yCoordinate - 1] = 'O';
            } else {
                array[xCoordinate - 1][yCoordinate - 1] = 'X';
            }
            printGrid(array, size);
            counter++;
            correctInput = true;
        } while (!correctInput);
        return counter;
    }
    public static void gameNotFinishedOrDraw(char[][] array, int size, boolean isEmptyCells ) {
        boolean threeInRow = false;
        boolean threeInColumn = false;
        boolean threeInMainDiagonal = false;
        boolean threeInMinorDiagonal = false;
        int[] sum = new int[size];
        int sumForDiagonals = 0;
        int i;
        int j;

        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                sum[i] += array[i][j];
            }
        }
        for (i = 0; i < size; i++) {
            if (sum[i] == 237) {
                threeInRow = true;
            } else if (sum[i] == 264) {
                threeInRow = true;
            }
        }

        for (j = 0; j < size; j++) {
            sum[j] = 0;
            for (i = 0; i < size; i++) {
                sum[j] += array[i][j];
            }
        }
        for (i = 0; i < size; i++) {
            if (sum[i] == 237) {
                threeInColumn = true;
            } else if (sum[i] == 264) {
                threeInColumn = true;
            }
        }

        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                if (i == j) {
                    sumForDiagonals += array[i][j];
                }
            }
        }
        if (sumForDiagonals == 237) {
            threeInMainDiagonal = true;
        } else if (sumForDiagonals == 264) {
            threeInMainDiagonal = true;
        }

        sumForDiagonals = 0;
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                if (i + j == size - 1) {
                    sumForDiagonals += array[i][j];
                }
            }
        }
        if (sumForDiagonals == 237) {
            threeInMinorDiagonal = true;
        } else if (sumForDiagonals == 264) {
            threeInMinorDiagonal = true;
        }

        if (!threeInRow && !threeInColumn && !threeInMainDiagonal && !threeInMinorDiagonal && isEmptyCells) {
            return;
        } else if (!threeInRow && !threeInColumn && !threeInMainDiagonal && !threeInMinorDiagonal && !isEmptyCells) {
            System.out.println("Draw");
            System.exit(0);
        }
    }
    public static void xOrOWins(char[][] array, int size) {
        int[] sum = new int[size];
        int sumForDiagonals = 0;
        int i;
        int j;

        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                sum[i] += array[i][j];
            }
        }
        for (i = 0; i < size; i++) {
            if (sum[i] == 264) {
                System.out.println("X wins");
                System.exit(0);
            } else if (sum[i] == 237) {
                System.out.println("O wins");
                System.exit(0);
            }
        }

        for (j = 0; j < size; j++) {
            sum[j] = 0;
            for (i = 0; i < size; i++) {
                sum[j] += array[i][j];
            }
        }
        for (i = 0; i < size; i++) {
            if (sum[i] == 264) {
                System.out.println("X wins");
                System.exit(0);
            } else if (sum[i] == 237) {
                System.out.println("O wins");
                System.exit(0);
            }
        }

        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                if (i == j) {
                    sumForDiagonals += array[i][j];
                }
            }
        }
        if (sumForDiagonals == 264) {
            System.out.println("X wins");
            System.exit(0);
        } else if (sumForDiagonals == 237) {
            System.out.println("O wins");
            System.exit(0);
        }

        sumForDiagonals = 0;
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                if (i + j == size - 1) {
                    sumForDiagonals += array[i][j];
                }
            }
        }
        if (sumForDiagonals == 264) {
            System.out.println("X wins");
            System.exit(0);
        } else if (sumForDiagonals == 237) {
            System.out.println("O wins");
            System.exit(0);
        }
    }
    public static void impossible(char[][] array, int size) {
        boolean threeXInRow = false;
        boolean threeOInRow = false;
        boolean threeXInColumn = false;
        boolean threeOInColumn = false;
        int[] sum = new int[size];
        int xCounter = 0;
        int oCounter = 0;
        int i;
        int j;

        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                if(array[i][j] == 'O') {
                    oCounter++;
                } else if (array[i][j] == 'X') {
                    xCounter++;
                }
            }
        }

        if (Math.abs(oCounter - xCounter) >= 2) {
            System.out.println("Impossible");
            System.exit(0);
        }


        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                sum[i] += array[i][j];
            }
        }

        for (i = 0; i < size; i++) {
            if (sum[i] == 264) {
                threeXInRow = true;
            } else if (sum[i] == 237) {
                threeOInRow = true;
            }
        }

        for (j = 0; j < size; j++) {
            sum[j] = 0;
            for (i = 0; i < size; i++) {
                sum[j] += array[i][j];
            }
        }

        for (i = 0; i < size; i++) {
            if (sum[i] == 264) {
                threeXInColumn = true;
            } else if (sum[i] == 237) {
                threeOInColumn = true;
            }
        }

        if (threeXInRow && threeOInRow || threeXInColumn && threeOInColumn) {
            System.out.println("Impossible");
            System.exit(0);
        }
    }
}
