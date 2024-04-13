package com.example.website.security.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import
org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuild er;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import
org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.util.concurrent.TimeUnit;
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
@Autowired
@Qualifier(value =	"CustomUserDetailsService")
private UserDetailsService userDetailsService;
@Autowired private PasswordEncoder passwordEncoder;
@Autowired
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
}
@Override
protected void configure(HttpSecurity http) throws Exception { http.csrf().disable();
http.authorizeRequests()
.antMatchers("/", "/signUp").permitAll()
.antMatchers("/users", "/newReceipt", "/newTransfer", "/newWriteOff", "/download/**", "/generateTorg16/**", "/generateTorg13/**", "/generateTorg1/**", "/generateNakladnaya/**", "/newItem", "/editItem/**", "/deleteItem/**", "/newStock", "/editStock/**", "/deleteStock/**", "/addItem").hasAnyAuthority("ADMIN")
.antMatchers("/receipts", "/transfers", "/writeOffs", "/documents", "/items", "/stocks", "/stockItems").authenticated();
http.formLogin()
.loginPage("/signIn")
.usernameParameter("email")
.defaultSuccessUrl("/")
.failureUrl("/signIn?error")
.permitAll()
.and()
.rememberMe()
.tokenValiditySeconds((int)TimeUnit.DAYS .toSeconds(21));
http.logout()
.logoutUrl("/logout")
.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
.clearAuthentication(true)
.invalidateHttpSession(true)
.deleteCookies("JSESSIONID", "remember-me")
.logoutSuccessUrl("/");
}
}
