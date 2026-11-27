package br.com.matheus.gestto.security;

import br.com.matheus.gestto.domain.user.User;
import br.com.matheus.gestto.domain.user.UserRepository;
import br.com.matheus.gestto.domain.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceApp implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceApp(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new UserDetailsApp(userService.findByEmail(email));
    }
}