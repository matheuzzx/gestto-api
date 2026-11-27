package br.com.matheus.gestto.domain.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public User findById(UUID id){
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado, id: " + id));
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado, email: " + email));
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public User save(String name, String email, String rawPassword){

        if(existsByEmail(email)){
            throw new RuntimeException("Email already exists: " + email);
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);

        return userRepository.save(User.create(name, email, hashedPassword, UserRole.CUSTOMER));
    }

    public void delete(UUID id){
        userRepository.delete(findById(id));
    }
}
