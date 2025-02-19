package com.mtiteiu.clinic.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Builder
public record MyUserDetails(@JsonIgnore User user) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

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
        return user.getEnabled();
    }

    public String getDisplayName() {
        return user.getPerson().getLastName();
    }

    public Long getUserId() {
        return user.getId();
    }

    public Boolean getHasPatientDetails() {
        return user.getPerson().getPatientDetails() != null;
    }

    public String getFirstName() {
        return user.getPerson().getFirstName();
    }

    public String getLastName() {
        return user.getPerson().getLastName();
    }

    public String getPhoneNumber() {
        return user.getPhoneNumber();
    }

    public String getGender(){
        return user.getPerson().getGender().getDisplayName();
    }

    public String getCNP() {
        return user.getPerson().getCnp();
    }
}
