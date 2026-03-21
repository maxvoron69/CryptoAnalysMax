package ru.javarush.pastukhov.cryptoanalysmax;

import java.util.Arrays;
import java.util.Scanner;

public class MainApp {
    public static int key;
    static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Введите ключ - целое число от 1 до 65: ");
        key = input.nextInt();

        Validator.validateInputFile();

        char[] text = FileManager.readFile(FileManager.inputFile);
        char[] encryptedText = CodeCaesars.encryption(text, key);
        FileManager.writeFile(FileManager.outputFile, encryptedText);
        Validator.validateOutputFile();
        System.out.println("Текст зашифрован и записан в файл: " + FileManager.outputFile);

        char[] decryptedText = CodeCaesars.decryption(encryptedText, key);
        FileManager.writeFile(FileManager.newOutputFile, decryptedText);
        System.out.println("Текст дешифрован и записан в файл: " + FileManager.newOutputFile);

        Validator.validateFile(FileManager.inputFile, FileManager.newOutputFile);
        System.out.println("Запускаем силовой взлом шифра.");
        System.out.println(BruteForce.decryptByBruteForce());

        System.out.println("Запускаем взлом методом статистического анализа.");
        char[] decryptedTextHSA = CodeCaesars.decryption(encryptedText, HackingStatisticalAnalysis.getDecryptedKey());
        FileManager.writeFile(FileManager.outputFileHackingStatisticalAnalysis, decryptedTextHSA);
        if (Arrays.equals(decryptedText, decryptedTextHSA)) {
            System.out.println("Зашифрованный текст взломан методом статистического анализа, дешифрован и записан в файл: " + FileManager.outputFileHackingStatisticalAnalysis);
        } else {
            System.out.println("ОШИБКА! Взлом не состоялся!");
        }
    }
}