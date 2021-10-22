package companyAll_MVC.configs;

import companyAll_MVC.services.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    final UserService userService;
    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(userService.encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic() //Postman de authorize olmak için ekledim.
                .and()
                .authorizeRequests()
                .antMatchers("/dashboard/**").hasRole("ADMIN")
                .antMatchers("/advertisement/**").hasRole("ADMIN")
                .antMatchers("/contents/**").hasRole("ADMIN")
                .antMatchers("/customer/**").hasRole("ADMIN")
                .antMatchers("/productAdd/**").hasRole("ADMIN")
                .antMatchers("/productsList/**").permitAll()
                .antMatchers("/productDetails/**").hasRole("ADMIN")
                .antMatchers("/galleries/**").hasRole("ADMIN")
                .antMatchers("/newsAdd/**").hasRole("ADMIN")
                .antMatchers("/newsList/**").hasRole("ADMIN")
                .antMatchers("/newsDetail/**").hasRole("ADMIN")
                .antMatchers("/announcements/**").hasRole("ADMIN")
                .antMatchers("/likes/**").hasRole("ADMIN")
                .antMatchers("/surveys/**").hasRole("ADMIN")
                .antMatchers("/orders/**").hasRole("ADMIN")
                .antMatchers("/settings/**").hasRole("ADMIN")
                .antMatchers("/home/**").permitAll()
                .antMatchers("/register/**").permitAll()
                .antMatchers("/forgotPassword/**").permitAll()

                //Rest Api Configuration
                .antMatchers("/api/product/**").hasRole("ADMIN")
                .antMatchers("/api/likes/**").hasRole("ADMIN")
                .antMatchers("/api/dashboard/**").hasRole("ADMIN")

                .antMatchers("/static/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/templates/**").permitAll()
                .antMatchers("/app-assets/**").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/homePage/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/mainJs/**").permitAll()
                .antMatchers("/uploadImages/**").permitAll()
                .antMatchers("/error/**").permitAll()
                .antMatchers("/inc/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login")
                .defaultSuccessUrl("/dashboard",true)
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/home")
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403");

        //Disable the post methods data actions
        http.csrf().disable();

        //Create Cookie with Spring Security
        http
                .rememberMe()
                .key("deneme")
                .rememberMeCookieName("remember-me")
                .tokenValiditySeconds(60*60)
                .alwaysRemember(true)
                .useSecureCookie(true);

        //Iframe showing options
        http.headers().frameOptions().disable();

        //Rest Api Configuration
        /*http
                .httpBasic()
                .and()
                .authorizeRequests()
                //.antMatchers("/dashboard/**").hasRole("ADMIN")
                //.antMatchers("/advertisement/**").hasRole("ADMIN")
                //.antMatchers("/contents/**").hasRole("ADMIN")
                //.antMatchers("/customer/**").hasRole("ADMIN")

                //.antMatchers("/galleries/**").hasRole("ADMIN")
                //.antMatchers("/newsAdd/**").hasRole("ADMIN")
                //.antMatchers("/newsList/**").hasRole("ADMIN")
                //.antMatchers("/newsDetail/**").hasRole("ADMIN")
                //.antMatchers("/announcements/**").hasRole("ADMIN")

                //.antMatchers("/surveys/**").hasRole("ADMIN")
                //.antMatchers("/orders/**").hasRole("ADMIN")
                //.antMatchers("/settings/**").hasRole("ADMIN")
                //.antMatchers("/register/**").permitAll()
                .antMatchers("/admin/**").permitAll()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .logout().logoutSuccessUrl("/admin/logout").invalidateHttpSession(true);*/

    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");

    }

}
