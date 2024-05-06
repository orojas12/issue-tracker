package dev.oscarrojas.issuetracker.security;

import dev.oscarrojas.issuetracker.user.UserModel;
import dev.oscarrojas.issuetracker.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

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
                    Set.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
            return userDetails;
        } else {
            throw new UsernameNotFoundException(String.format("Username '%s' not found"));
        }

    }

}
