package machine;
import java.util.Scanner;
public class CoffeeMachine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] amountOfProductsAndMoney = {400, 540, 120, 9, 550}; //{water, milk, coffee, disposable cups, money}
        String action;

        do {
            System.out.println("Write action (buy, fill, take, remaining, exit):");
            action = scanner.nextLine();
            switch (action) {
                case "buy": amountOfProductsAndMoney = Buy(amountOfProductsAndMoney);
                    break;
                case "fill": amountOfProductsAndMoney = Fill(amountOfProductsAndMoney);
                    break;
                case "take": amountOfProductsAndMoney = Take(amountOfProductsAndMoney);
                    break;
                case "remaining": Remaining(amountOfProductsAndMoney);
                case "exit": break;
                default: break;
            }
        } while(action.compareTo("exit") != 0);


    }
    public static int[] Buy(int[] amountOfProductsAndMoney) {
        Scanner scanner = new Scanner(System.in);
        int[] costs = {4, 7, 6}; //{espresso, latte, cappuccino}
        int[][] productsPerCoffee = {{250, 0, 16, 1}, {350, 75, 20, 1}, {200, 100, 12, 1}};
        boolean enoughResources = true;

        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
        String choice = scanner.nextLine();

        if (choice.compareTo("back") == 0) {
            return amountOfProductsAndMoney;
        }

        for (int i = 0; i < 4; i++) {
            if (amountOfProductsAndMoney[i] - productsPerCoffee[Integer.parseInt(choice) - 1][i] > 0) {
                continue;
            } else {
                System.out.print("Sorry, not enough ");
                if (i == 0) {
                    System.out.print("water!\n");
                    enoughResources = false;
                } else if (i == 1) {
                    System.out.print("milk!\n");
                    enoughResources = false;
                } else if (i == 2) {
                    System.out.print("coffee beans!\n");
                    enoughResources = false;
                } else if (i == 3) {
                    System.out.print("disposable cups!\n");
                    enoughResources = false;
                }
                break;
            }
        }

        if (enoughResources) {
            System.out.print("I have enough resources, making you a coffee!\n");
        } else {
            return amountOfProductsAndMoney;
        }

        int i;
        for (i = 0; i < 4; i++) {
                amountOfProductsAndMoney[i] -= productsPerCoffee[Integer.parseInt(choice) - 1][i];
            }

        amountOfProductsAndMoney[i] += costs[Integer.parseInt(choice) - 1];

        return amountOfProductsAndMoney;
    }
    public static int[] Fill(int[] amountOfProductsAndMoney) {
        Scanner scanner = new Scanner(System.in);
        int[] amountOfProductsToAdd = new int[4];
        System.out.println("Write how many ml of water do you want to add:");
        amountOfProductsToAdd[0] = scanner.nextInt();
        System.out.println("Write how many ml of milk do you want to add:");
        amountOfProductsToAdd[1] = scanner.nextInt();
        System.out.println("Write how many grams of coffee beans do you want to add:");
        amountOfProductsToAdd[2] = scanner.nextInt();
        System.out.println("Write how many disposable cups of coffee do you want to add: ");
        amountOfProductsToAdd[3] = scanner.nextInt();

        for (int i = 0; i < 4; i++) {
            amountOfProductsAndMoney[i] += amountOfProductsToAdd[i];
        }
        return amountOfProductsAndMoney;
    }
    public static int[] Take(int[] amountOfProductsAndMoney) {
        System.out.printf("I gave you $%d%n", amountOfProductsAndMoney[4]);
        amountOfProductsAndMoney[4] = 0;
        return amountOfProductsAndMoney;
    }
    public static void Remaining(int[] amountOfProductsAndMoney) {
        System.out.printf("%nThe coffee machine has:%n%d of water%n%d of milk%n%d of coffee beans%n" +
                        "%d of disposable cups%n%d of money%n", amountOfProductsAndMoney[0], amountOfProductsAndMoney[1],
                amountOfProductsAndMoney[2], amountOfProductsAndMoney[3], amountOfProductsAndMoney[4]);
    }
}
