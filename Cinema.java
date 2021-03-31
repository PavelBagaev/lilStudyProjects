package cinema;
import java.util.Scanner;
public class Cinema {

    private final int rows;
    private final int seatsInRow;
    private char[][] map;
    static int totalIncome;
    static int purchasedTickets = 0;
    static int income = 0;

    enum TicketPrices {
        EIGHT(8),
        TEN (10);

        int price;

        TicketPrices (int dollars) {
            this.price = dollars;
        }
        int getPrice () {
            return this.price;
        }
    }

    public Cinema (int rows, int seatsInRow, char[][] map, int totalIncome) {
        this.rows = rows;
        this.seatsInRow = seatsInRow;
        this.map = map;
        this.totalIncome = totalIncome;
    }

    static Cinema init() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of rows:");
        int rows = scanner.nextInt();
        System.out.println("Enter the number of seats in each row:");
        int seatsInRow = scanner.nextInt();
        char[][] map = new char[rows][seatsInRow];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < seatsInRow; j++) {
                map[i][j] = 'S';
            }
        }
        if (rows * seatsInRow <= 60) {
            Cinema.totalIncome = rows * seatsInRow * 10;
        } else {
            int front = seatsInRow * (rows / 2) * 10;
            int back = seatsInRow * (rows - (rows / 2)) * 8;
            Cinema.totalIncome = front + back;
        }

        return new Cinema(rows, seatsInRow, map, totalIncome);
    }

    public static void main(String[] args) {
        Cinema hall = init();
        menu(hall);
    }
    static void printSeats(Cinema hall) {
        System.out.println("\nCinema:");
        System.out.print("  ");

        for (int j = 1; j <= hall.seatsInRow; j++) {
            System.out.printf("%d ", j);
        }

        System.out.printf("\n");
        for (int i = 0; i < hall.rows; i++) {
            System.out.printf("%d ", i + 1);
            for (int j = 0; j < hall.seatsInRow; j++) {
                System.out.printf("%c ", hall.map[i][j]);
            }
            System.out.print("\n");
        }
    }
    static void sellTicket(Cinema hall) {
        Scanner scanner = new Scanner(System.in);
        int row;
        int seat;
        boolean correctInput = true;

        do {
            System.out.println("\nEnter a row number:");
            row = scanner.nextInt();
            System.out.println("Enter a seat number in that row:");
            seat = scanner.nextInt();

            if (row < 0 || row > hall.rows || seat < 0 || seat > hall.seatsInRow) {
                System.out.println("\nWrong input!");
                correctInput = false;
            } else {
                correctInput = true;
            }

            if(correctInput) {
                if (hall.map[row - 1][seat - 1] == 'B') {
                    System.out.println("\nThat ticket has already been purchased!");
                    correctInput = false;
                } else {
                    correctInput = true;
                }
            }

        } while (!correctInput);


        if (hall.seatsInRow * hall.rows <= 60 || row >= 1 && row <= hall.rows / 2) {
                System.out.printf("\nTicket price: $%d\n", TicketPrices.TEN.getPrice());
                income += TicketPrices.TEN.getPrice();
        } else if (row > hall.rows / 2) {
                System.out.printf("\nTicket price: $%d\n", TicketPrices.EIGHT.getPrice());
                income += TicketPrices.EIGHT.getPrice();
        }

        hall.map[row - 1][seat - 1] = 'B';
        purchasedTickets++;
    }
    static void menu (Cinema hall) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n1. Show the seats");
            System.out.println("2. Buy a ticket");
            System.out.println("3. Statistics");
            System.out.println("0. Exit");
            choice = scanner.nextInt();

            switch (choice) {
                case 1: printSeats(hall); break;
                case 2: sellTicket(hall); break;
                case 3: statistics(hall); break;
                case 0: break;
            }
        } while (choice != 0);
    }
    static void statistics (Cinema hall) {
        double percentage = (purchasedTickets * 100.0) / (hall.rows * hall.seatsInRow);
        System.out.printf("\nNumber of purchased tickets: %d", purchasedTickets);
        System.out.printf("\nPercentage: %.2f%%", percentage);
        System.out.printf("\nCurrent income: $%d", income);
        System.out.printf("\nTotal income: $%d\n", totalIncome);
    }
}