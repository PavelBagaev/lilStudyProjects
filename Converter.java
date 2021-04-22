package converter;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Converter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
        int sourceRadix;
        try {
            sourceRadix = scanner.nextInt();
            if (!scanner.hasNext()) {
                System.out.println("error");
                return;
            }
            if (sourceRadix < 1 || sourceRadix > 36) {
                System.out.println("Error: radix can't be less than 1 or bigger than 36!");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("error");
            return;
        }
        scanner.nextLine();
        String sourceNumber = scanner.nextLine();
        if (!scanner.hasNextInt()) {
            System.out.println("error");
            return;
        }
        int targetRadix;
        try {
            targetRadix = scanner.nextInt();
            if (targetRadix < 1 || targetRadix > 36) {
                System.out.println("Error: radix can't be less than 1 or bigger than 36!");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("error");
            return;
        }

        String[] splitNumber = numberSplitter(sourceNumber);
        String integerPart = new String();
        String fractionalPart = new String();
        if (splitNumber.length == 2) {
            integerPart = splitNumber[0];
            fractionalPart = splitNumber[1];
        } else {
            integerPart = splitNumber[0];
            fractionalPart = "0";
        }


        if (sourceRadix != 10) {
            if ("0".equals(fractionalPart)) {
                printer(toRadixConverter(decimalConverter(integerPart, sourceRadix), targetRadix));
                return;
            } else {
                int integerPartDecimal = decimalConverter(integerPart, sourceRadix);
                double fractionalPartDecimal = fractionalPartToDecimal(fractionalPart, sourceRadix);
                String integerPartInTargetRadix = toRadixConverter(integerPartDecimal, targetRadix);
                String fractionalPartInTargetRadix = fractionalPartToRadixConverter(fractionalPartDecimal, targetRadix);
                printer(integerPartInTargetRadix.concat(".").concat(fractionalPartInTargetRadix));
            }
        } else {
            if ("0".equals(fractionalPart)) {
                printer(toRadixConverter(Integer.parseInt(integerPart), targetRadix));
                return;
            } else {
                String integerPartInTargetRadix = toRadixConverter(Integer.parseInt(integerPart), targetRadix);
                String fractionalPartInTargetRadix = fractionalPartToRadixConverter(
                        Double.parseDouble(sourceNumber) - Integer.parseInt(integerPart), targetRadix);
                printer(integerPartInTargetRadix.concat(".").concat(fractionalPartInTargetRadix));
            }
        }
    }

    static private int decimalConverter (String number, int radix) {
        if (radix == 1) {
            return number.length();
        } else {
            return Integer.parseInt(number, radix);
        }
    }

    static private double fractionalPartToDecimal (String fractionalPart, int radix) {
        double decimalValue = 0;
        int powers = 1;
        for (int i = 0; i < fractionalPart.length(); i++) {
            decimalValue += (double)decimalConverter(String.valueOf(fractionalPart.charAt(i)), radix) / Math.pow(radix, powers);
            powers++;
        }
        return decimalValue;
    }

    static private String toRadixConverter(int number, int radix) {
        String result = new String();
        if (radix == 1) {
            for (int i = 0; i < number; i++) {
                result = result.concat("1");
            }
            return result;
        }
        return Integer.toString(number, radix);
    }

    static private String fractionalPartToRadixConverter(double number, int radix) {
        String result = new String();
        double fractionalPart = number;
        int integerPartOfProduct;
        if (radix == 1) {
            for (int i = 0; i < 5; i++) {
                result = result.concat("1");
            }
            return result;
        } else {
            for (int i = 0; i < 5; i++) {
                integerPartOfProduct = (int) (fractionalPart * radix);
                result = result.concat(toRadixConverter(integerPartOfProduct, radix));
                fractionalPart = fractionalPart * radix - integerPartOfProduct;
            }
            return result;
        }
    }

    static private String[] numberSplitter(String number) {
        return number.split("\\.");
    }

    static private void printer (String output) {
        System.out.println(output);
    }
}
