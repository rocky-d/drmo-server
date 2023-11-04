package uo.moodle;

import java.security.SecureRandom;
import java.util.Base64;

import org.apache.commons.codec.digest.Crypt;

public class HashExample {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        HashMyPassword("password");
    }

    public static void HashMyPassword(String secretWord){

        SecureRandom strongRandomNumberGenerator = new SecureRandom();
       
        byte b[] = new byte[13];
        strongRandomNumberGenerator.nextBytes(b);

        String saltedB = new String(Base64.getEncoder().encode(b));

        String salt = "$6$" + saltedB;
        System.out.println(salt);

        String userPasswordToBeSaved = Crypt.crypt(secretWord, salt);

        System.out.println(userPasswordToBeSaved);

        boolean sameSecretWord = checkIfSame(secretWord, userPasswordToBeSaved);
        System.out.println("salasana on: " + sameSecretWord);

        String otherSecretWord = "erilainenSalsana";
        String userPasswordToBeSaved2 = Crypt.crypt(otherSecretWord, salt);

        boolean differentSecretWord = checkIfSame(secretWord, userPasswordToBeSaved2);
        System.out.println("salasana on: " + differentSecretWord);

    }

    public static boolean checkIfSame(String plain, String saltedHashed){

        System.out.println("Plain password: " + plain + " and salted password: " + saltedHashed);
        if (saltedHashed.equals(Crypt.crypt(plain, saltedHashed))) {
            return true;
        } else {
            return false;
        }
    }

}

