package io.neoOkpara.librado.shared;

import java.security.SecureRandom;
import java.util.Random;

//@Component
public class UtilsMethods {

	private final static Random RANDOM = new SecureRandom();
	private final static String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefgijklmnopqrstuvwxyz";

	public static String generateRandomId(int length) {
		return generateRandomString(length);
	}

	private static String generateRandomString(int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		return new String(sb);
	}

}
