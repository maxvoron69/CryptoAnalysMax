package ru.javarush.pastukhov.cryptoanalysmax;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
    public static final String inputFile = "src/main/resources/input.txt";
    public static final String outputFile = "out/encrypted.txt";
    public static final String newOutputFile = "out/decrypted.txt";
    public static final String outputFileHackingStatisticalAnalysis = "out/decrypted_statistical.txt";

    public static char[] readFile(String str) {
        Path path = Path.of(str);
        try {
            byte[] bytesArray = Files.readAllBytes(path);
            String text = new String(bytesArray, StandardCharsets.UTF_8);
            return text.toCharArray();
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            return new char[0];
        }
    }

    public static void writeFile(String path, char[] array) {
        createParentDirectories(path);
        try {
            String text = new String(array);
            Files.writeString(Path.of(path), text,  StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
        }
    }

    public static void createParentDirectories(String path) {
        Path dir = Paths.get(path).getParent();
        if (dir != null && !Files.exists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                throw new RuntimeException("Не удалось создать директорию: " + dir, e);
            }
        }
    }
}



