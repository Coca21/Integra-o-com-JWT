/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 55489
 */
package com.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crm.config.JwtTokenUtil;
import com.crm.model.JwtRequest;
import com.crm.model.JwtResponse;
import com.crm.service.JwtUserDetailsService;

@RestController
@CrossOrigin
public class ApplicationController {

@Autowired
private AuthenticationManager authenticationManager;

@Autowired
private JwtTokenUtil jwtTokenUtil;

@Autowired
private JwtUserDetailsService userDetailsService;

@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
	authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
	final UserDetails userDetails = userDetailsService
			.loadUserByUsername(authenticationRequest.getUsername());
	final String token = jwtTokenUtil.generateToken(userDetails);
return ResponseEntity.ok(new JwtResponse(token));
}

private void authenticate(String username, String password) throws Exception {
try {
	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
} catch (DisabledException e) {
	throw new Exception("USER_DISABLED", e);
} catch (BadCredentialsException e) {
	throw new Exception("INVALID_CREDENTIALS", e);
}
}
}
