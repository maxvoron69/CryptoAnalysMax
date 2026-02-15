package ru.javarush.pastukhov.cryptoanalysmax;

import java.util.Arrays;

public class CodeCaesars {
    private static final char[] ALPHABET = "ЁАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюяё".toCharArray();
    public static int key = 55;

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


    public static char[] encryption(char[] chars, int key){
        char[] arrayOutputFile = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            int pos = Arrays.binarySearch(ALPHABET, c);
            if (pos < 0) {
                arrayOutputFile[i] = c;
            } else {
                int index = Math.floorMod(pos + key, ALPHABET.length);
                arrayOutputFile[i] = getLetter(index);
            }
        }
        return arrayOutputFile;
    }


    public static char[] decryption(char[] array, int key) {
        return encryption(array, -key);
    }
}
