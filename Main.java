package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            String input = new String(Files.readAllBytes(Paths.get(args[0])));
            String[] sentences = sentencesSplitter(input);
            long amountOfWords = wordCounter(sentences);
            long amountOfCharacters = characterCounter(input);
            int[] syllAndPolysyll = syllablesAndPolysyllablesCounter (sentences); // [0] - vowels/syllables, [1] - polysyllables
            double automatedReadabilityIndex = 0;
            double fleschKincaid = 0;
            double simpleMeasureOfGobbledygook = 0;
            double colemanLiau = 0;

            printer(input, amountOfCharacters, amountOfWords, sentences.length, syllAndPolysyll[0], syllAndPolysyll[1]);

            System.out.printf("\nEnter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            String option = scanner.nextLine();
            switch (option) {
                case "ARI": automatedReadabilityIndex = ARI(amountOfCharacters, amountOfWords, sentences.length);
                break;
                case "FK": fleschKincaid = FK(amountOfWords, sentences.length, syllAndPolysyll[0]);
                    break;
                case "SMOG": simpleMeasureOfGobbledygook = SMOG(sentences.length, syllAndPolysyll[1]);
                    break;
                case "CL": colemanLiau = CL(amountOfCharacters, amountOfWords, sentences.length);
                    break;
                case "all": automatedReadabilityIndex = ARI(amountOfCharacters, amountOfWords, sentences.length);
                    fleschKincaid = FK(amountOfWords, sentences.length, syllAndPolysyll[0]);
                    simpleMeasureOfGobbledygook = SMOG(sentences.length, syllAndPolysyll[1]);
                    colemanLiau = CL(amountOfCharacters, amountOfWords, sentences.length);
                    break;
                default: break;
            }
            indexPrinter(automatedReadabilityIndex, fleschKincaid, simpleMeasureOfGobbledygook, colemanLiau);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String[] sentencesSplitter (String input) {
        return input.split("(!|\\.|\\?)\\s*");
    }

    private static long wordCounter (String[] sentences) {
        int[] wordsInSentences = new int[sentences.length];
        long amountOfWords = 0;
        int j = 0;

        for (String i: sentences) {
            String[] words = i.split("\\s");
            wordsInSentences[j] = words.length;
            j++;
        }

        for (j = 0; j < wordsInSentences.length; j++) {
            amountOfWords += wordsInSentences[j];
        }
        return amountOfWords;
    }

    private static long characterCounter (String input) {
        return input.replaceAll("\\s", "").length();
    }

    private static double ARI (long chars, long words, int sentences) {
        final double const1 = 4.71;
        final double const2 = 0.5;
        final double const3 = 21.43;
        return const1 * ((double)chars / words)
                + const2 * ((double)words / sentences) - const3;
    }

    private static double FK (long words, int sentences, int syllables) {
        final double const1 = 0.39;
        final double const2 = 11.8;
        final double const3 = 15.59;
        return const1 * ((double)words / sentences)
                + const2 * ((double)syllables / words) - const3;
    }

    private static double SMOG (int sentences, int polysyllables) {
        final double const1 = 1.043;
        final double const2 = 30.0;
        final double const3 = 3.1291;
        return const1 * Math.sqrt(polysyllables * (const2 / sentences)) + const3;
    }

    private static double CL (long chars, long words, int sentences) {
        final double const1 = 0.0588;
        final double const2 = 0.296;
        final double const3 = 15.8;
        final double L = ((double)chars / words) * 100;
        final double S = ((double)sentences / words) * 100;
        return const1 * L - const2 * S - const3;
    }

    private static int[] syllablesAndPolysyllablesCounter (String[] sentences) {
        int[] vowelsAndPolysyllables = new int[2]; // [0] - vowels, [1] - polysyllables
        for (String i: sentences) {
            String[] words = i.split("\\s");
            for (String j : words) {
                String temp = j.replaceAll("(e\\b)", "").
                        replaceAll("([aeiouyAEIOUY]{2,})", "a"); //строка без е на конце и двух и более гласных рядом
                int vowelsInWord = temp.length() - temp.replaceAll("[aeiouyAEIOUY]", "").length(); //гласных в слове
                if (vowelsInWord == 0) {
                    vowelsAndPolysyllables[0]++;
                } else {
                    vowelsAndPolysyllables[0] += vowelsInWord;
                }
                if (vowelsInWord > 2) {
                    vowelsAndPolysyllables[1]++;
                }
            }
        }
        return vowelsAndPolysyllables;
    }

    private static void printer (String input, long chars, long words, int sentences,
                                 int syllables, int polysyllables) {
        System.out.println("The text is:");
        System.out.println(input);
        System.out.printf("\nWords: %d", words);
        System.out.printf("\nSentences: %d", sentences);
        System.out.printf("\nCharacters: %d", chars);
        System.out.printf("\nSyllables: %d", syllables);
        System.out.printf("\nPolysyllables: %d", polysyllables);
    }

    private static void indexPrinter (double ari, double fk,
                                      double smog, double cl) {
        if (ari != 0) {
            System.out.printf("\nAutomated Readability Index: %.2f ", ari);
            switch ((int)Math.round(ari)) {
                case 1:  System.out.printf("(about 6-year-olds).");
                    break;
                case 2:  System.out.printf("(about 7-year-olds).");
                    break;
                case 3:  System.out.printf("(about 9-year-olds).");
                    break;
                case 4:  System.out.printf("(about 10-year-olds).");
                    break;
                case 5:  System.out.printf("(about 11-year-olds).");
                    break;
                case 6:  System.out.printf("(about 12-year-olds).");
                    break;
                case 7:  System.out.printf("(about 13-year-olds).");
                    break;
                case 8:  System.out.printf("(about 14-year-olds).");
                    break;
                case 9:  System.out.printf("(about 15-year-olds).");
                    break;
                case 10:  System.out.printf("(about 16-year-olds).");
                    break;
                case 11:  System.out.printf("(about 17-year-olds).");
                    break;
                case 12:  System.out.printf("(about 18-year-olds).");
                    break;
                case 13:  System.out.printf("(about 24-year-olds).");
                    break;
                case 14:  System.out.printf("(about 24+-year-olds).");
                    break;
                default: System.out.println("\nAn error has occurred!");
                    break;
            }
        }
        if (fk != 0) {
            System.out.printf("\nFlesch–Kincaid readability tests: %.2f ", fk);
            switch ((int)Math.round(fk)) {
                case 1:  System.out.printf("(about 6-year-olds).");
                    break;
                case 2:  System.out.printf("(about 7-year-olds).");
                    break;
                case 3:  System.out.printf("(about 9-year-olds).");
                    break;
                case 4:  System.out.printf("(about 10-year-olds).");
                    break;
                case 5:  System.out.printf("(about 11-year-olds).");
                    break;
                case 6:  System.out.printf("(about 12-year-olds).");
                    break;
                case 7:  System.out.printf("(about 13-year-olds).");
                    break;
                case 8:  System.out.printf("(about 14-year-olds).");
                    break;
                case 9:  System.out.printf("(about 15-year-olds).");
                    break;
                case 10:  System.out.printf("(about 16-year-olds).");
                    break;
                case 11:  System.out.printf("(about 17-year-olds).");
                    break;
                case 12:  System.out.printf("(about 18-year-olds).");
                    break;
                case 13:  System.out.printf("(about 24-year-olds).");
                    break;
                case 14:  System.out.printf("(about 24+-year-olds).");
                    break;
                default: System.out.println("\nAn error has occurred!");
                    break;
            }
        }
        if (smog != 0) {
            System.out.printf("\nSimple Measure of Gobbledygook: %.2f ", smog);
            switch ((int)Math.round(smog)) {
                case 1:  System.out.printf("(about 6-year-olds).");
                    break;
                case 2:  System.out.printf("(about 7-year-olds).");
                    break;
                case 3:  System.out.printf("(about 9-year-olds).");
                    break;
                case 4:  System.out.printf("(about 10-year-olds).");
                    break;
                case 5:  System.out.printf("(about 11-year-olds).");
                    break;
                case 6:  System.out.printf("(about 12-year-olds).");
                    break;
                case 7:  System.out.printf("(about 13-year-olds).");
                    break;
                case 8:  System.out.printf("(about 14-year-olds).");
                    break;
                case 9:  System.out.printf("(about 15-year-olds).");
                    break;
                case 10:  System.out.printf("(about 16-year-olds).");
                    break;
                case 11:  System.out.printf("(about 17-year-olds).");
                    break;
                case 12:  System.out.printf("(about 18-year-olds).");
                    break;
                case 13:  System.out.printf("(about 24-year-olds).");
                    break;
                case 14:  System.out.printf("(about 24+-year-olds).");
                    break;
                default: System.out.println("\nAn error has occurred!");
                    break;
            }
        }
        if (cl != 0) {
            System.out.printf("\nColeman–Liau index: %.2f ", cl);
            switch ((int)Math.round(cl)) {
                case 1:  System.out.printf("(about 6-year-olds).");
                    break;
                case 2:  System.out.printf("(about 7-year-olds).");
                    break;
                case 3:  System.out.printf("(about 9-year-olds).");
                    break;
                case 4:  System.out.printf("(about 10-year-olds).");
                    break;
                case 5:  System.out.printf("(about 11-year-olds).");
                    break;
                case 6:  System.out.printf("(about 12-year-olds).");
                    break;
                case 7:  System.out.printf("(about 13-year-olds).");
                    break;
                case 8:  System.out.printf("(about 14-year-olds).");
                    break;
                case 9:  System.out.printf("(about 15-year-olds).");
                    break;
                case 10:  System.out.printf("(about 16-year-olds).");
                    break;
                case 11:  System.out.printf("(about 17-year-olds).");
                    break;
                case 12:  System.out.printf("(about 18-year-olds).");
                    break;
                case 13:  System.out.printf("(about 24-year-olds).");
                    break;
                case 14:  System.out.printf("(about 24+-year-olds).");
                    break;
                default: System.out.println("\nAn error has occurred!");
                    break;
            }
        }
    }
}
