package ru.javarush.pastukhov.cryptoanalysmax;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Validator {

    public static void validateInputFile() {
        Path path = Paths.get(FileManager.inputFile);
        if (!Files.exists(path)) {
            System.err.println("Файл не найден: " + path);
        }
    }

    public static void validateOutputFile() {
        Path path = Paths.get(FileManager.outputFile);
        if (!Files.exists(path)) {
            System.err.println("Файл не найден: " + path);
        }
    }

    public static void validateFile(String str1, String str2) {
        Path fileBeforeEncrypting = Path.of(str1);
        Path fileAfterDecrypting = Paths.get(str2);

        if (Files.exists(fileBeforeEncrypting) && Files.exists(fileAfterDecrypting))
            try {
            if (Files.mismatch(fileBeforeEncrypting, fileAfterDecrypting) < 0) {
                System.out.println("Тексты до шифрования и после дешифрации равны");
            } else {
                System.out.println("Тексты до шифрования и после дешифрации отличаются");
            }
        } catch (
                IOException e) {
            throw new RuntimeException("Ошибка при сравнении файлов", e);
        }
        else {
            System.out.println("Ошибка! Указанный файл не существует!");
        }
    }
}


