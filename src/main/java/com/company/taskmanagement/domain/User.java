package com.company.taskmanagement.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // BU IMPORT ÖNEMLİ

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails { // <-- EN ÖNEMLİ SATIR BURASI

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 100)
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Task> tasks;

    // === UserDetails METODLARI ===
    // Bu metodlar Spring Security'nin kullanıcıyı tanıması için gereklidir.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Bu projede rolleri (ADMIN, USER) kullanmıyoruz, bu yüzden boş liste dönüyoruz.
        return List.of();
    }

    @Override
    public String getUsername() {
        // Spring Security için "kullanıcı adı" bizim için "e-posta"dır.
        return this.email;
    }

    // @Override'ları eklemek iyi bir pratiktir.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}