package ru.javarush.pastukhov.cryptoanalysmax;

import java.util.Arrays;

public class MainApp {
    public static void main(String[] args) {

        Validator.validateInputFile();
        Validator.validateOutputFile();

        char[] text = FileManager.readFile(FileManager.inputFile);
        char[] encryptedText = CodeCaesars.encryption(text, CodeCaesars.key);

        FileManager.writeFile(FileManager.outputFile, encryptedText);
        System.out.println("Текст зашифрован и записан в файл: " + FileManager.outputFile);

        char[] decryptedText = CodeCaesars.decryption(encryptedText, CodeCaesars.key);
        FileManager.writeFile(FileManager.newOutputFile, decryptedText);
        System.out.println("Текст дешифрован и записан в файл: " + FileManager.newOutputFile);

        Validator.validateFile(FileManager.inputFile, FileManager.newOutputFile);

        System.out.println(BruteForce.decryptByBruteForce(FileManager.outputFile));

        char[] decryptedTextHSA = CodeCaesars.decryption(encryptedText, HackingStatisticalAnalysis.getDecryptedKey());
        FileManager.writeFile(FileManager.outputFileHackingStatisticalAnalysis, decryptedTextHSA);
        if (Arrays.equals(decryptedText, decryptedTextHSA)) {
            System.out.println("Зашифрованный текст взломан методом статистического анализа, дешифрован и записан в файл: " + FileManager.outputFileHackingStatisticalAnalysis);
        } else{
            System.out.println("ОШИБКА! Взлом не состоялся!");
        }
    }
}