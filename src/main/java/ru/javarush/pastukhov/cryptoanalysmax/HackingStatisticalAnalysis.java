package ru.javarush.pastukhov.cryptoanalysmax;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HackingStatisticalAnalysis {

    public static int getDecryptedKey() {
        char[] encrypted = FileManager.readFile(FileManager.outputFile);

        Map<Character, Integer> freq = new HashMap<>();
        for (char c : encrypted) {
            if (Character.isLetter(c)) {
                freq.merge(Character.toLowerCase(c), 1, Integer::sum);
            }
        }

        List<Character> actual = freq.entrySet().stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(3)
                .toList();

        if (actual.size() < 3) return 0;

        char[] alphabet = CodeCaesars.getAlphabet();
        int len = alphabet.length;

        int k1 = shift(actual.get(0), 'о', alphabet, len);
        int k2 = shift(actual.get(1), 'е', alphabet, len);
        int k3 = shift(actual.get(2), 'а', alphabet, len);

        Map<Integer, Long> keyPopularity = Arrays.stream(new int[]{k1, k2, k3})
                .boxed()
                .collect(Collectors.groupingBy(k -> k, Collectors.counting()));

        return keyPopularity.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);
    }

    private static int shift(char enc, char exp, char[] alphabet, int len) {
        int e = Arrays.binarySearch(alphabet, enc);
        int x = Arrays.binarySearch(alphabet, exp);
        if (e < 0 || x < 0) return -1;
        return (e - x + len) % len;
    }
}
