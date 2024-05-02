package dev.oscarrojas.issuetracker.security;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.oscarrojas.issuetracker.user.UserModel;
import dev.oscarrojas.issuetracker.user.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> opt = repository.findByUsername(username);
        if (opt.isPresent()) {
            UserModel entity = opt.get();
            UserDetailsImpl userDetails = new UserDetailsImpl(
                entity.getUsername(),
                entity.getPassword(),
                entity.getRoles().stream()
                        .map((role) -> new SimpleGrantedAuthority(role.getId()))
                        .collect(Collectors.toSet())
            );
            return userDetails;
        } else {
            throw new UsernameNotFoundException(String.format("Username '%s' not found"));
        }

    }

}
