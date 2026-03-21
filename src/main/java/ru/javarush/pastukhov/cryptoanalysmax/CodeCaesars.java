package ru.javarush.pastukhov.cryptoanalysmax;

import java.util.Arrays;

import static ru.javarush.pastukhov.cryptoanalysmax.Alphabet.ALPHABET;
import static ru.javarush.pastukhov.cryptoanalysmax.Alphabet.getLetter;

public class CodeCaesars {

    public static int key = 55;

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
