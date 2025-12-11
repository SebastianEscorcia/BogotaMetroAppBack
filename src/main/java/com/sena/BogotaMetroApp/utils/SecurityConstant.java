package com.sena.BogotaMetroApp.utils;


public abstract class SecurityConstant {


    //Rutas públicas

    public static final String[] PUBLIC_URLS = {
            "/api/auth/**",
            //CHATS
            "/ws-metro/**",
            //registro pasajero
            "/api/pasajero/registro",
            "/api/auth/recuperar-clave",
            "/api/auth/cambiar-clave",
            "/api/torniquete/validar-ingreso"
    };


}
