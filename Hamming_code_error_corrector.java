package correcter;

import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.lang.StringBuilder;
/*import java.lang.Byte;*/
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String mode = scanner.nextLine();
        switch (mode) {
            case "encode": encoder();
                break;
            case "send": encoder();
                randomBitErrorGenerator();
                break;
            case "decode": encoder();
                randomBitErrorGenerator();
                decoder();
                break;
            default: break;
        }
    }

    private static void encoder () {
        int byteFromStream;
        int beginInd;
        int endInd;
        int onesCounter;
        String byteAsString;
        String fourBits;
        StringBuilder encodedByte = new StringBuilder();
        try (FileInputStream fileInputStream = new FileInputStream("send.txt")) {
            try (FileOutputStream fileOutputStream = new FileOutputStream("encoded.txt", false)) {
                while ((byteFromStream = fileInputStream.read()) != -1) {
                    byteAsString = String.format("%8s", Integer.toBinaryString(byteFromStream)).
                            replaceAll(" ","0");
                    beginInd = 0;
                    endInd = 4;
                    onesCounter = 0;
                    for (int j = 0; j < 2; j++) {
                        fourBits = byteAsString.substring(beginInd, endInd);
                        encodedByte.append("  ").append(fourBits.charAt(0)).append(" ").append(fourBits, 1, 4)
                                .append("0");

                        for (int i = 2; i < encodedByte.length(); i += 2) {
                            if (encodedByte.charAt(i) == 49) {
                                onesCounter++;
                            }
                        }
                        if (onesCounter % 2 == 0) {
                            encodedByte.setCharAt(0, '0');
                        } else {
                            encodedByte.setCharAt(0, '1');
                        }
                        onesCounter = 0;

                        if (encodedByte.charAt(2) == 49) {
                            onesCounter++;
                        }
                        for (int i = 5; i < encodedByte.length() - 1; i ++) {
                            if (encodedByte.charAt(i) == 49) {
                                onesCounter++;
                            }
                        }
                        if (onesCounter % 2 == 0) {
                            encodedByte.setCharAt(1, '0');
                        } else {
                            encodedByte.setCharAt(1, '1');
                        }
                        onesCounter = 0;

                        for (int i = 4; i < encodedByte.length() - 1; i++) {
                            if (encodedByte.charAt(i) == 49) {
                                onesCounter++;
                            }
                        }
                        if (onesCounter % 2 == 0) {
                            encodedByte.setCharAt(3, '0');
                        } else {
                            encodedByte.setCharAt(3, '1');
                        }
                        onesCounter = 0;

                        if (Integer.parseInt(encodedByte.toString(), 2) > 127) {
                            fileOutputStream.write(Integer.parseInt(encodedByte.toString(), 2) - 256);
                        } else {
                            fileOutputStream.write(Integer.parseInt(encodedByte.toString(), 2));
                        }

                        encodedByte.delete(0, encodedByte.length());

                        beginInd = endInd;
                        endInd += 4;
                    }
                }
            } catch (IOException e) {
                System.out.printf("An error has occurred: %s", e.getMessage());
            }
        } catch (IOException e) {
            System.out.printf("An error has occurred: %s", e.getMessage());
        }
    }

    private static void randomBitErrorGenerator () {
        Random random = new Random();
        int charFromSend;
        int intRep;
        StringBuilder initialString = new StringBuilder();
        try (FileInputStream fileInputStream = new FileInputStream("encoded.txt")) {
            try (FileOutputStream fileOutputStream = new FileOutputStream("received.txt", false)) {
                charFromSend = fileInputStream.read();
                while (charFromSend != -1) {
                    initialString.append(String.format("%8s", Integer.toBinaryString(charFromSend)).
                            replaceAll(" ","0")).append(" ");
                    charFromSend = fileInputStream.read();
                }

                String[] bytes = initialString.toString().split(" ");
                for (String aByte : bytes) {
                    intRep = Integer.parseInt(aByte, 2);
                    intRep ^= (1 << random.nextInt(6) + 1);
                    if (intRep > 127) {
                        fileOutputStream.write(intRep - 256);
                    } else {
                        fileOutputStream.write(intRep);
                    }
                }
            } catch (IOException e) {
                System.out.printf("An error has occurred: %s", e.getMessage());
            }
        } catch (IOException e) {
            System.out.printf("An error has occurred: %s", e.getMessage());
        }
    }

    private static void decoder () {
        int byteFromStream;
        int onesCounter;
        int corruptedBitIndex;
        int flipper;
        String corruptedByte;
        StringBuilder correctedByte = new StringBuilder();
        try (FileInputStream fileInputStream = new FileInputStream("received.txt")) {
            try (Writer writer = new FileWriter("decoded.txt", false)) {
                while ((byteFromStream = fileInputStream.read()) != -1) {
                    corruptedByte = String.format("%8s", Integer.toBinaryString(byteFromStream)).
                            replaceAll(" ","0");

                    onesCounter = 0;
                    corruptedBitIndex = 0;

                    for (int i = 2; i < corruptedByte.length(); i += 2) {
                        if (corruptedByte.charAt(i) == 49) {
                            onesCounter++;
                        }
                    }
                    if (onesCounter % 2 == 0) {
                        if (corruptedByte.charAt(0) != 48) {
                            corruptedBitIndex += 1;
                        }
                    } else {
                        if (corruptedByte.charAt(0) != 49) {
                            corruptedBitIndex += 1;
                        }
                    }
                    onesCounter = 0;

                    if (corruptedByte.charAt(2) == 49) {
                        onesCounter++;
                    }
                    for (int i = 5; i < corruptedByte.length() - 1; i++) {
                        if (corruptedByte.charAt(i) == 49) {
                            onesCounter++;
                        }
                    }
                    if (onesCounter % 2 == 0) {
                        if (corruptedByte.charAt(1) != 48) {
                            corruptedBitIndex += 2;
                        }
                    } else {
                        if (corruptedByte.charAt(1) != 49) {
                            corruptedBitIndex += 2;
                        }
                    }
                    onesCounter = 0;

                    for (int i = 4; i < corruptedByte.length() - 1; i++) {
                        if (corruptedByte.charAt(i) == 49) {
                            onesCounter++;
                        }
                    }
                    if (onesCounter % 2 == 0) {
                        if (corruptedByte.charAt(3) != 48) {
                            corruptedBitIndex += 4;
                        }
                    } else {
                        if (corruptedByte.charAt(3) != 49) {
                            corruptedBitIndex += 4;
                        }
                    }

                    flipper = Integer.parseInt(corruptedByte, 2);
                    flipper ^= (1 << (corruptedByte.length() - 1) - (corruptedBitIndex - 1));
                    corruptedByte = String.format("%8s", Integer.toBinaryString(flipper)).
                            replaceAll(" ","0");

                    correctedByte.append(corruptedByte.charAt(2)).append(corruptedByte.charAt(4)).
                            append(corruptedByte.charAt(5)).append(corruptedByte.charAt(6));
                    if (correctedByte.length() == 8) {
                        writer.write(Integer.parseInt(correctedByte.toString(), 2));
                        correctedByte.delete(0, correctedByte.length());
                    }
                }
            } catch (IOException e) {
                System.out.printf("An error has occurred: %s", e.getMessage());
            }
        } catch (IOException e) {
            System.out.printf("An error has occurred: %s", e.getMessage());
        }
    }

    /*private static void encoder () {
        int byteFromStream;
        int iterationCounter = 0;
        byte parity = 0;
        String byteAsString = new String();
        String lastByte = new String();
        StringBuilder bytesEncoded = new StringBuilder();
        try (FileInputStream fileInputStream = new FileInputStream("send.txt")) {
            try (FileOutputStream fileOutputStream = new FileOutputStream("encoded.txt", false)) {
                while ((byteFromStream = fileInputStream.read()) != -1) {
                    byteAsString = String.format("%8s", Integer.toBinaryString(byteFromStream)).
                            replaceAll(" ","0");

                    for (int i = 0; i < 8; i++) {
                        bytesEncoded.append(byteAsString.charAt(i));
                        bytesEncoded.append(byteAsString.charAt(i));
                        bytesEncoded.append("|");
                        iterationCounter++;
                        if (iterationCounter == 3) {
                            String[] bytes = bytesEncoded.toString().split("\\s");
                            String[] terms = bytes[bytes.length - 1].split("\\|");
                            for (int j = 0; j < 3; j++) {
                                parity ^= Byte.parseByte(terms[j], 2);
                            }
                            bytesEncoded.append(String.format("%2s", Integer.toBinaryString(parity)).
                                    replaceAll(" ",String.format("%1s", Integer.toBinaryString(parity))));
                            parity = 0;
                            bytesEncoded.append(" ");
                            iterationCounter = 0;
                        }
                    }
                }

                if (byteFromStream == -1) {
                    String[] bytes = bytesEncoded.toString().split("\\s");
                    lastByte = bytes[bytes.length - 1].replaceAll("\\|", "");
                    if (lastByte.length() != 8) {
                        switch (lastByte.length()) {
                            case 2:
                                for (int i = 0; i < 2; i++) {
                                    bytesEncoded.append("00");
                                    bytesEncoded.append("|");
                                }
                                break;
                            case 4:
                                bytesEncoded.append("00");
                                bytesEncoded.append("|");
                                break;
                        }
                        bytes = bytesEncoded.toString().split("\\s");
                        String[] terms = bytes[bytes.length - 1].split("\\|");
                        for (int j = 0; j < 3; j++) {
                            parity ^= Byte.parseByte(terms[j], 2);
                        }
                        bytesEncoded.append(String.format("%2s", Integer.toBinaryString(parity)).
                                replaceAll(" ", String.format("%1s", Integer.toBinaryString(parity))));
                    }
                }

                String encodedClean = bytesEncoded.toString().replaceAll("\\|", "");
                String[] bytes = encodedClean.split(" ");
                for (String aByte : bytes) {
                    if (Integer.parseInt(aByte, 2) > 127) {
                        fileOutputStream.write(Integer.parseInt(aByte, 2) - 256);
                    } else {
                        fileOutputStream.write(Integer.parseInt(aByte, 2));
                    }
                }
            } catch (IOException e) {
                System.out.printf("An error has occurred: %s", e.getMessage());
            }
        } catch (IOException e) {
            System.out.printf("An error has occurred: %s", e.getMessage());
        }
    }*/

   /* private static void decoder () {
        int charFromSend;
        StringBuilder initialString = new StringBuilder();
        StringBuilder decodedStr = new StringBuilder();
        try (FileInputStream fileInputStream = new FileInputStream("received.txt")) {
            try (Writer writer = new FileWriter("decoded.txt", false)) {
                charFromSend = fileInputStream.read();
                while (charFromSend != -1) {
                    initialString.append(String.format("%8s", Integer.toBinaryString(charFromSend)).
                            replaceAll(" ","0")).append(" ");
                    charFromSend = fileInputStream.read();
                }

                String[] bytes = initialString.toString().split(" ");
                StringBuilder[] twos = new StringBuilder[bytes[0].length() / 2];
                int beginInd;
                int endInd;
                int errorIndex = 0;
                for (String aByte : bytes) {
                    beginInd = 0;
                    endInd = 2;
                    for (int j = 0; j < twos.length; j++) {
                        twos[j] = new StringBuilder();
                        twos[j].append(aByte, beginInd, endInd);
                        beginInd = endInd;
                        endInd += 2;
                    }

                    char[] triples = new char[3];
                    boolean errorInDataPairs = false;
                    for (int j = 0; j < twos.length - 1; j++) {
                        if (twos[j].charAt(0) != twos[j].charAt(1)) {
                            errorIndex = j;
                            errorInDataPairs = true;
                        } else {
                            triples[j] = twos[j].charAt(0);
                        }
                    }

                    int parityBit;
                    int zerosCounter = 0;
                    int onesCounter = 0;
                    if (errorInDataPairs) {
                        parityBit = Character.getNumericValue(twos[twos.length - 1].charAt(0));
                        if (parityBit == 0) {
                            for (char triple : triples) {
                                if (Character.getNumericValue(triple) == 0) {
                                    zerosCounter++;
                                } else if (Character.getNumericValue(triple) == 1) {
                                    onesCounter++;
                                }
                            }

                            if (onesCounter == 0) {
                                triples[errorIndex] = '0';
                            } else if (onesCounter % 2 == 0) {
                                triples[errorIndex] = '0';
                            } else {
                                triples[errorIndex] = '1';
                            }
                        } else if (parityBit == 1) {
                            for (char triple : triples) {
                                if (Character.getNumericValue(triple) == 0) {
                                    zerosCounter++;
                                } else if (Character.getNumericValue(triple) == 1) {
                                    onesCounter++;
                                }
                            }

                            if (zerosCounter == 0) {
                                triples[errorIndex] = '1';
                            } else if (zerosCounter % 2 == 0) {
                                triples[errorIndex] = '1';
                            } else {
                                triples[errorIndex] = '0';
                            }
                        }
                    }
                    decodedStr.append(triples);
                }
                int meaninglessBits = decodedStr.length() % 8;
                if (meaninglessBits != 0) {
                    decodedStr.delete(decodedStr.length() - meaninglessBits, decodedStr.length());
                }

                int start = 0;
                int end = 8;
                for (int i = 0; i < decodedStr.length() / 8; i++) {
                    writer.write(Integer.parseInt(decodedStr.substring(start, end), 2));
                    start = end;
                    end += 8;
                }
            } catch (IOException e) {
                System.out.printf("An error has occurred: %s", e.getMessage());
            }
        } catch (IOException e) {
            System.out.printf("An error has occurred: %s", e.getMessage());
        }

    }*/
}
