package com.example.kjavasystem.auth.controller;

import com.example.kjavasystem.config.JwtTokenUtil;
import com.example.kjavasystem.utils.enums.MessageResponseEnum;
import com.example.kjavasystem.utils.model.JwtRequest;
import com.example.kjavasystem.utils.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public Response createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = jwtInMemoryUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return new Response(token, MessageResponseEnum.SUCCESS.getMessage());
    }

    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
