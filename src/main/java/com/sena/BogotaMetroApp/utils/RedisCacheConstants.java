package com.sena.BogotaMetroApp.utils;

import lombok.Getter;

@Getter
public abstract class RedisCacheConstants {

    // QR Keys
    public static final String USER_QR_KEY_PATTERN = "usuario:%d:qr";
    public static final String QR_CODE_KEY_PATTERN = "qr:%s";

    // FAQ Keys
    public static final String ALL_CATEGORY_FAQS_KEY = "faqs:categories:all";
    public static final String ALL_SUPPORT_FAQS_KEY = "faqs:support:all";
    public static final String SUPPORT_FAQS_BY_CATEGORY_PATTERN = "faqs:support:category:%d";

    // Patterns para invalidación
    public static final String SUPPORT_FAQS_PATTERN = "faqs:support:*";
    public static final String ALL_FAQS_PATTERN = "faqs:*";

    // TTL en minutos
    public static final long CATEGORY_FAQS_TTL_MINUTES = 60L;  // 1 hora
    public static final long SUPPORT_FAQS_TTL_MINUTES = 30L;   // 30 minutos


    public static String userQrKey(Long usuarioId) {
        return String.format(USER_QR_KEY_PATTERN, usuarioId);
    }

    public static String qrCodeKey(String codigo) {
        return String.format(QR_CODE_KEY_PATTERN, codigo);
    }

    public static String supportFaqsByCategoryKey(Long categoryId) {
        return String.format(SUPPORT_FAQS_BY_CATEGORY_PATTERN, categoryId);
    }
}
