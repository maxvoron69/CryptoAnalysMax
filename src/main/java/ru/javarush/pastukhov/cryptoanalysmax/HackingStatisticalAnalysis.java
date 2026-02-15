package ru.javarush.pastukhov.cryptoanalysmax;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HackingStatisticalAnalysis {
    private static final char[] FREQ_USED_LETTER = {'о', 'е', 'а', 'и', 'н', 'т', 'с', 'р', 'в', 'л'};
    private static final String[] COMMON_WORDS = {"и", "в", "не", "на", "я", "быть", "что", "а", "весь", "она"};

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
                .limit(FREQ_USED_LETTER.length)
                .toList();

        if (actual.size() < FREQ_USED_LETTER.length) return 0;

        char[] alphabet = CodeCaesars.getAlphabet();
        int len = alphabet.length;
        int[] keys = new int[FREQ_USED_LETTER.length];
        for (int i = 0; i < FREQ_USED_LETTER.length; i++) {
            keys[i] = shift(actual.get(i), FREQ_USED_LETTER[i], alphabet, len);
        }
        Map<Integer, Long> popularity = Arrays.stream(keys)
                .boxed()
                .filter(k -> k >= 0)
                .collect(Collectors.groupingBy(k -> k, Collectors.counting()));

        List<Map.Entry<Integer, Long>> topTwo = popularity.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(2)
                .toList();

        if (topTwo.isEmpty()) {
            return 0;
        } else if (topTwo.size() == 1) {
            System.out.println("Найден один кандидат на ключ: " + topTwo.getFirst().getKey());
            return topTwo.getFirst().getKey();
        } else {
            Integer key1 = topTwo.get(0).getKey();
            Integer key2 = topTwo.get(1).getKey();
            Long count1 = topTwo.get(0).getValue();
            Long count2 = topTwo.get(1).getValue();

            System.out.printf("Два наиболее вероятных ключа:%n");
            System.out.printf("Ключ %d — %d голосов%n", key1, count1);
            System.out.printf("Ключ %d — %d голосов%n", key2, count2);

            // Если у одного из ключей явное преимущество — выбираем его
            if (count1 > count2) {
                System.out.println("✅ Выбран ключ: " + key1 + " (явный лидер)");
                return key1;
            } else if (count2 > count1) {
                System.out.println("✅ Выбран ключ: " + key2 + " (явный лидер)");
                return key2;
            } else {
                // Голоса равны — используем дополнительный критерий
                System.out.println("⚖️ Голоса равны. Используем дополнительный критерий...");

                // Критерий: проверим, какой ключ даёт больше осмысленных слов при расшифровке
                int textScore1 = evaluateKeyScore(key1);
                int textScore2 = evaluateKeyScore(key2);

                System.out.printf("Оценка текста при ключе %d: %d совпадений%n", key1, textScore1);
                System.out.printf("Оценка текста при ключе %d: %d совпадений%n", key2, textScore2);

                return textScore1 >= textScore2 ? key1 : key2;
            }
        }
    }

    private static int evaluateKeyScore(int key) {
        char[] encrypted = FileManager.readFile(FileManager.outputFile);
        char[] decrypted = CodeCaesars.decryption(encrypted, key);

        String text = new String(decrypted).toLowerCase();
        String[] words = text.split("[\\s\\p{Punct}]+");

        int matchCount = 0;
        for (String word : words) {
            for (String common : COMMON_WORDS) {
                if (word.equals(common)) {
                    matchCount++;
                    break;
                }
            }
        }
        return matchCount;
    }

    private static int shift(char enc, char exp, char[] alphabet, int len) {
        int e = Arrays.binarySearch(alphabet, enc);
        int x = Arrays.binarySearch(alphabet, exp);
        if (e < 0 || x < 0) return -1;
        return Math.floorMod(e - x, len);
    }
}
