package main.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import main.security.jwt.JwtSecurityConfigurer;
import main.security.jwt.JwtTokenProvider;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth/signin", "/auth/signup").permitAll()
                .antMatchers("/article", "/article/{\\d+}", "/article/find/name", "/article/find/operation_id/{\\d+}").permitAll()
                .antMatchers("/operation", "/operation/{\\d+}", "/operation/dto/{\\d+}", "/operation/find/name").permitAll()
                .antMatchers("/operation/find/article_id/{\\d+}", "/operation/find/balance_id/{\\d+}", "/operation/find/lower", "/operation/find/upper").permitAll()
                .antMatchers("/balance", "/balance/{\\d+}", "/balance/dto/{\\d+}", "/balance/find", "/balance/find/operation_id/{\\d+}").permitAll()
                .antMatchers("/auth/valid_token").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("auth/logout"))
                .logoutSuccessUrl("/operation")
                .and()
                .apply(new JwtSecurityConfigurer(jwtTokenProvider));
    }
}
