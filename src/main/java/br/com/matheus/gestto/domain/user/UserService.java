package br.com.matheus.gestto.domain.user;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    public User findById(UUID id){
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado, id: " + id));
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado, email: " + email));
    }

    public User save(String name, String email, String rawPassword, UserRole userRole){
        return userRepository.save(User.create(name, email, rawPassword, userRole));
    }

    public void delete(UUID id){
        userRepository.delete(findById(id));
    }
}
