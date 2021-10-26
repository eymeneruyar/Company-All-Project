package companyAll_MVC.services;

import companyAll_MVC.entities.Role;
import companyAll_MVC.entities.User;
import companyAll_MVC.entities.UserActivity;
import companyAll_MVC.repositories._jpa.UserActivityRepository;
import companyAll_MVC.repositories._jpa.UserRepository;
import companyAll_MVC.utils.Util;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService extends SimpleUrlLogoutSuccessHandler implements UserDetailsService, LogoutSuccessHandler {

    final UserRepository userRepository;
    final UserActivityRepository userActivityRepository;
    public UserService(UserRepository userRepository, UserActivityRepository userActivityRepository) {
        this.userRepository = userRepository;
        this.userActivityRepository = userActivityRepository;
    }

    //Security Login
    @Override
    public UserDetails loadUserByUsername(String email) {
        //System.out.println("Email: " + email);
        UserDetails userDetails = null;
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email);
        if(userOptional.isPresent()){
            User us = userOptional.get();
            userDetails = new org.springframework.security.core.userdetails.User(
                    us.getEmail(),
                    us.getPassword(),
                    us.isEnabled(),
                    us.isTokenExpired(),
                    true,
                    true,
                    getAuthorities(us.getRoles()) );
        }
        else{
            throw new UsernameNotFoundException("Kullanıcı adı ya da şifre hatalı!");
        }
        return userDetails;
    }

    private static List<GrantedAuthority> getAuthorities (List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    public User register(User us) throws AuthenticationException {

        if(!Util.isEmail(us.getEmail())){
            throw new AuthenticationException("Bu mail formatı hatalı!");
        }

        //Kullanıcılar bölümünden güncelleme işlemi yapabilmek için kapattım.
        /*Optional<User> uOpt = uRepo.findByuEmailEqualsIgnoreCase(us.getUEmail());
        if(uOpt.isPresent()){
            throw new AuthenticationException("Bu kullanıcı daha önce kayıtlı");
        }*/

        us.setPassword(encoder().encode(us.getPassword()));

        return userRepository.saveAndFlush(us);
    }

    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    // User Activity info
    public UserActivity info(HttpServletRequest req, HttpServletResponse res, UserActivity userActivity) throws IOException {

        Authentication aut = SecurityContextHolder.getContext().getAuthentication();
        String email = aut.getName(); // username

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();

        String session = req.getSession().getId();

        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        if (user.isPresent()) {
            userActivity.setName(user.get().getName());
            userActivity.setSurname(user.get().getSurname());
            userActivity.setImage(user.get().getProfileImage());
            String roles = "";
            for (Role item : user.get().getRoles()) {
                roles += item.getName() + ", ";
            }
            if (roles.length() > 0) {
                roles = roles.substring(0, roles.length() - 2);
            }
            userActivity.setRole(roles);
        }

        userActivity.setEmail(email);
        userActivity.setSessionId(session);
        userActivity.setIp(ip);

        userActivity.setUrl(req.getRequestURI());
        userActivity.setDate(LocalDateTime.now());

        userActivityRepository.save(userActivity);

        return userActivity;

    }

}
