package ru.javarush.pastukhov.cryptoanalysmax;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

public class BruteForce {
    private static final Set<String> COMMON_RUSSIAN_WORDS = Set.of(
            "и", "в", "не", "на", "я", "быть", "что", "а", "весь", "она",
            "этот", "он", "с", "тот", "но", "они", "у", "к", "то",
            "мы", "о", "из", "за", "по", "для", "оно", "так", "ее", "если", "который", "же", "при", "от", "вот", "до", "ну",
            "вы", "бы", "ещё", "уже", "только", "мочь", "один", "или", "тогда", "кто", "нет", "да", "какой", "чем", "когда", "туда",
            "сюда", "это", "себя", "сам", "ни", "через", "более", "между", "рано",
            "вниз", "вверх", "вокруг", "рядом", "почти", "просто", "вдруг", "всегда",
            "иногда", "часто", "редко", "теперь", "потом", "сегодня", "завтра",
            "вчера", "здесь", "там", "где", "куда", "откуда", "почему", "зачем",
            "как", "такой", "такая", "такое", "такие", "чей", "чья", "чьё", "чьи",
            "мой", "твой", "свой", "наш", "ваш", "их", "его", "её"
    );
    private static final Path BRUTE_FORCE_DIR = Paths.get(System.getProperty("java.io.tmpdir"), "caesar_bruteforce");

    public static void allDecryptionOptions(String inputFilePath) throws IOException {
        char[] encryptedChars = FileManager.readFile(inputFilePath);
        Files.createDirectories(BRUTE_FORCE_DIR);

        for (int key = 1; key <= 65; key++) {
            char[] decryptedArray = CodeCaesars.decryption(encryptedChars, key);
            Path filePath = BRUTE_FORCE_DIR.resolve(key + ".txt");

            try {
                Files.writeString(filePath, new String(decryptedArray), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка записи файла: " + filePath, e);
            }
        }
    }

    public static String decryptByBruteForce() {
        try {
            allDecryptionOptions(FileManager.outputFile);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка генерации вариантов расшифровки", e);
        }

        int maxMatches = -1;
        int bestKey = 1;

        for (int key = 1; key <= 65; key++) {
            Path path = BRUTE_FORCE_DIR.resolve(key + ".txt");
            if (!Files.exists(path)) continue;

            try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
                long matchCount = lines
                        .flatMap(line -> Arrays.stream(line.toLowerCase().split("[\\s\\p{Punct}]+")))
                        .filter(COMMON_RUSSIAN_WORDS::contains)
                        .count();

                if (matchCount > maxMatches) {
                    maxMatches = (int) matchCount;
                    bestKey = key;
                }

            } catch (IOException e) {
                System.err.println("Ошибка чтения файла: " + path);
            }
        }
        Path source = BRUTE_FORCE_DIR.resolve(bestKey + ".txt");
        Path target = Paths.get("out/decrypted_bruteforce.txt");

        try {
            Files.createDirectories(target.getParent());
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

            deleteDirectoryRecursively();

            return "Файл полученный в результате силового взлома: out/decrypted_bruteforce.txt";

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при копировании файла", e);
        }
    }

    private static void deleteDirectoryRecursively() {
        if (!Files.exists(BRUTE_FORCE_DIR)) return;

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        try {
            Files.walkFileTree(BRUTE_FORCE_DIR, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            System.err.println("Ошибка при обходе директории: " + BRUTE_FORCE_DIR + " → " + e.getMessage());
        }
    }

}

