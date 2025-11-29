package com.sena.BogotaMetroApp.services;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtServices {
    private final String SECRETE = "e2075c4595347971576b4d2b78e4858d981b7a5c4b988a1d85c447d73167b9e3e243e9a73681e8b53b44d647c0c95fa221af74206f528e662ee54c3ac346868d";
    public String generateToken(Usuario usuario){
        return Jwts.builder().setSubject(usuario.getCorreo()).claim("rol",usuario.getRol().getNombre()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 60 * 10)).signWith(Keys.hmacShaKeyFor(SECRETE.getBytes()), SignatureAlgorithm.HS256).compact();
    }
    public  String extraerCorreo(String token){
        return  Jwts.parser().setSigningKey(SECRETE.getBytes()).build().parseClaimsJws(token).getBody().getSubject();
    }
}
