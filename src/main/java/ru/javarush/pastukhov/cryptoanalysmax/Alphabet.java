package ru.javarush.pastukhov.cryptoanalysmax;

import java.util.Arrays;

public class Alphabet {
    static final char[] ALPHABET = "ЁАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюяё".toCharArray();

    static {
        Arrays.sort(ALPHABET);
    }

    public static char[] getAlphabet() {
        return Arrays.copyOf(ALPHABET, ALPHABET.length);
    }

    public static char getLetter(int index) {
        if (index < 0 || index >= ALPHABET.length) {
            throw new IndexOutOfBoundsException("Индекс должен быть от 0 до " + (ALPHABET.length - 1)+" включительно.");
        }
        return ALPHABET[index];
    }
}
