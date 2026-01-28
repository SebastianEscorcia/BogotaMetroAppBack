package com.sena.BogotaMetroApp.utils.logic;



import java.security.SecureRandom;

public abstract class TarjetaUtil {
    private static final SecureRandom random = new SecureRandom();
    private static final String PREFIJO = "4501";


    public static String generarNumeroTarjeta() {
        StringBuilder numero = new StringBuilder(PREFIJO);

        // Genera 11 dígitos aleatorios
        for (int i = 0; i < 11; i++) {
            numero.append(random.nextInt(10));
        }

        // Calcular dígito verificador Luhn
        int digitoVerificador = generarDigitoLuhn(numero.toString());
        numero.append(digitoVerificador);

        try {
            return formatearNumeroTarjeta(numero.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatearNumeroTarjeta(String numeroSinFormato) throws Exception {
        if (numeroSinFormato == null || numeroSinFormato.length() != 16) {
            throw new RuntimeException("Numero de tarjeta no válido");
        }
        return numeroSinFormato.replaceAll("(.{4})", "$1-").substring(0, 19);
    }

    private static int generarDigitoLuhn(String base) {
        int sum = 0;
        boolean alternate = true;

        // Recorremos de derecha a izquierda
        for (int i = base.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(base.substring(i, i + 1));

            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }

            sum += n;
            alternate = !alternate;
        }

        return (10 - (sum % 10)) % 10;
    }

}
