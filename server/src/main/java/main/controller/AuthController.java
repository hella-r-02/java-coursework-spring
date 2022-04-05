package main.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.exception.UserAlreadyExistException;
import main.repository.UserRepository;
import main.security.jwt.JwtProperties;
import main.security.jwt.JwtTokenProvider;
import main.service.UserService;
import main.web.AuthRequest;
import main.web.SignUpRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    JwtProperties jwtProperties;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody AuthRequest request) {
        try {
            String name = request.getUserName();
            String token = jwtTokenProvider.createToken(name,
                    userRepository.findByUserName(name)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found")).getRoles());
            Map<Object, Object> model = new HashMap<>();
            model.put("userName", name);
            model.put("token", token);
            model.put("timeout", jwtProperties.getValidityInMs());
            model.put("createDate", new Date().getTime());
            return ResponseEntity.ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        try {
            String name = request.getUserName();
            try {
                userService.addNewUserAfterSignUp(request.getUserName(), request.getPassword(), request.getRoles());
            } catch (UserAlreadyExistException e) {
                throw new BadCredentialsException("User already exist");
            }
            String token = jwtTokenProvider.createToken(name,
                    userRepository.findByUserName(name)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found")).getRoles());
            Map<Object, Object> model = new HashMap<>();
            model.put("userName", name);
            model.put("token", token);
            model.put("timeout", jwtProperties.getValidityInMs());
            model.put("createDate", new Date().getTime());
            return ResponseEntity.ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logOut(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.setAuthenticated(false);
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        SecurityContextHolder.clearContext();
        try {
            request.logout();
        } catch (ServletException e) {
            throw new BadCredentialsException("Invalid HttpServletRequest");
        }
        request.getSession().invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/valid_token")
    public ResponseEntity<String> getValidToken(@RequestParam("username") String username) {
        String token = jwtTokenProvider.createToken(username,
                userRepository.findByUserName(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")).getRoles());
        return new ResponseEntity(token, HttpStatus.OK);
    }
}
