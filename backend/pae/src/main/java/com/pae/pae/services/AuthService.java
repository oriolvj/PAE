package com.pae.pae.services;

import com.pae.pae.models.UsuariDTO;
import com.pae.pae.repositories.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class AuthService implements UserDetailsService {

    @Autowired private UsuariRepository usuariRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Entra al loadUserByUsername");
        Optional<UsuariDTO> useropt = Optional.ofNullable(usuariRepository.getUsuari(username));
        if(useropt.isEmpty()) throw new UsernameNotFoundException("Usuari amb username: " + username+ "no trobat");
        UsuariDTO user = useropt.get();
        System.out.println("Crea user DTO: "+ user.toString());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPwd(), Collections.singletonList(new SimpleGrantedAuthority(user.getRol().toString())));
    }
}
