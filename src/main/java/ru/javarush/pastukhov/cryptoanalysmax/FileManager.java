package ru.javarush.pastukhov.cryptoanalysmax;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {
    static String inputFile = "C:\\Users\\user\\Documents\\Text.txt";
    static String outputFile = "C:\\Users\\user\\Documents\\Encrypted.txt";
    static String newOutputFile = "C:\\Users\\user\\Documents\\Decrypted.txt";
    static String outputFileHackingStatisticalAnalysis = "C:\\Users\\user\\Documents\\DecryptedHSA.txt";


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

    public static void writeFile(String str, char[] array) {
        try {
            String text = new String(array);
            Files.write(Path.of(str), text.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
        }
    }
}



