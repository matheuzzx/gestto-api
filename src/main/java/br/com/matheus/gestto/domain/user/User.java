package br.com.matheus.gestto.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
public class User {

    private User(String name, String email, String password, UserRole role){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public static User create(String name, String email, String password, UserRole role){

        if(role.toString().equals("ADMIN") || role.toString().equals("CUSTOMER")){
            throw new IllegalArgumentException("Role Inválida");
        }

        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if(email == null || email.isBlank()){
            throw new IllegalArgumentException("Email é obrigatório");
        }

        if(password == null || password.isBlank()){
            throw new IllegalArgumentException("Senha é obrigatória");
        }

        return new User(name, email, password, role);

    }

}
