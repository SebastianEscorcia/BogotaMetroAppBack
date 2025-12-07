package com.sena.BogotaMetroApp.services;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtServices {

    @Value("${app.security.jwt-key}")
    private String SECRETE ;
    public String generateToken(Usuario usuario){
        return Jwts.builder().setSubject(usuario.getCorreo()).claim("rol",usuario.getRol().getNombre()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 60 * 10)).signWith(Keys.hmacShaKeyFor(SECRETE.getBytes()), SignatureAlgorithm.HS256).compact();
    }
    public  String extraerCorreo(String token){
        return  Jwts.parser().setSigningKey(SECRETE.getBytes()).build().parseClaimsJws(token).getBody().getSubject();
    }
}
