package ru.javarush.pastukhov.cryptoanalysmax;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class BruteForce {
    private static final String[] COMMON_RUSSIAN_WORDS = {

            "и", "в", "не", "на", "я", "быть", "что", "а", "весь", "она",
            "этот", "как", "он", "с", "тот", "но", "они", "у", "к", "то",
            "мы", "вы", "о", "из", "за", "по", "для", "оно", "так", "его",
            "ее", "если", "который", "же", "при", "от", "вот", "до", "ну",
            "вы", "бы", "ещё", "уже", "только", "мочь", "свой", "один", "там",
            "или", "тогда", "кто", "нет", "да", "какой", "чем", "когда", "туда",
            "сюда", "это", "себя", "сам", "ни", "через", "более", "между", "рано",
            "вниз", "вверх", "вокруг", "рядом", "почти", "просто", "вдруг", "всегда",
            "иногда", "часто", "редко", "теперь", "потом", "сегодня", "завтра",
            "вчера", "здесь", "там", "где", "куда", "откуда", "почему", "зачем",
            "как", "такой", "такая", "такое", "такие", "чей", "чья", "чьё", "чьи",
            "мой", "твой", "свой", "наш", "ваш", "их", "его", "её"
    };
    private static final Path BRUTE_FORCE_DIR = Paths.get(System.getProperty("java.io.tmpdir"), "caesar_bruteforce");


    public static void allDecryptionOptions(String textToDecrypt) throws IOException {
        char[] encryptedChars = FileManager.readFile(textToDecrypt);
        Files.createDirectories(BRUTE_FORCE_DIR);

        for (int key = 1; key <= 65; key++) {
            char[] decryptedArray = CodeCaesars.encryption(encryptedChars, -key);
            Path filePath = BRUTE_FORCE_DIR.resolve(key + ".txt");

            try {
                Files.writeString(filePath, new String(decryptedArray));
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
            String filePath = "C:\\Users\\user\\Documents\\BrutoForce\\" + key + ".txt";
            Path path = Path.of(filePath);

            if (!Files.exists(path)) continue;

            try {
                byte[] bytes = Files.readAllBytes(path);
                String content = new String(bytes, StandardCharsets.UTF_8);
                String[] words = content.toLowerCase().split("[\\s\\p{Punct}]+");

                int matchCount = 0;
                for (String word : words) {
                    for (String common : COMMON_RUSSIAN_WORDS) {
                        if (word.equals(common)) {
                            matchCount++;
                            break;
                        }
                    }
                }

                if (matchCount > maxMatches) {
                    maxMatches = matchCount;
                    bestKey = key;
                }

            } catch (IOException e) {
                System.err.println("Ошибка чтения файла: " + filePath);
            }
        }
        Path source = BRUTE_FORCE_DIR.resolve(bestKey + ".txt");
        Path target = Paths.get("C:\\Users\\user\\Documents\\DecryptedBF.txt");

        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

            deleteDirectoryRecursively();

            return "Файл полученный в результате силового взлома: C:\\Users\\user\\Documents\\DecryptedBF.txt";

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при копировании файла", e);
        }
    }
    private static void deleteDirectoryRecursively() {
        if (!Files.exists(BruteForce.BRUTE_FORCE_DIR)) return;

        try {
            // Небольшая пауза, чтобы ОС успела освободить файлы
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        try {
            Files.walkFileTree(BruteForce.BRUTE_FORCE_DIR, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        System.err.println("Не удалось удалить файл: " + file + " → " + e.getMessage());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    try {
                        Files.delete(dir);
                    } catch (DirectoryNotEmptyException e) {
                        System.err.println("Папка не пуста при удалении: " + dir);
                    } catch (IOException e) {
                        System.err.println("Не удалось удалить папку: " + dir + " → " + e.getMessage());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            System.err.println("Ошибка при обходе директории: " + BruteForce.BRUTE_FORCE_DIR + " → " + e.getMessage());
        }
    }

}

