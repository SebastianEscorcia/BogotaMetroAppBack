package com.sena.BogotaMetroApp.utils.logic;

import java.security.SecureRandom;

public class TokenGenerator {

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();
    private static final int TOKEN_LENGTH = 10; // Puedes ajustar largo

    public static String generateTicket() {
        StringBuilder token = new StringBuilder("MTB-");

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            token.append(CHAR_POOL.charAt(index));
        }
        return token.toString();
    }
}
