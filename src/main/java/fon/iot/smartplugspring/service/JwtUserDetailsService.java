package fon.iot.smartplugspring.service;

import fon.iot.smartplugspring.dao.UserRepository;
import fon.iot.smartplugspring.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if (!optionalUser.isPresent())
            throw new UsernameNotFoundException("User not found with username: " + username);

        UserEntity user = optionalUser.get();
        return new User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }

    public boolean checkIfExistsByUsername(String username) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent())
            return true;

        return false;
    }

    public UserEntity registerUser(String username, String password) {
        UserEntity user = new UserEntity(username, new BCryptPasswordEncoder().encode(password));
        return userRepository.save(user);
    }
}
