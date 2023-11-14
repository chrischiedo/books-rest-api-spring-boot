package dev.chiedo.booksrestapi.userdetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.chiedo.booksrestapi.model.User;
import dev.chiedo.booksrestapi.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository
                .findAppUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
        return new UserAdapter(user);
    }
}
