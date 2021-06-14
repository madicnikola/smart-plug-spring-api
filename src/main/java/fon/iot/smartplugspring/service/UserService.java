package fon.iot.smartplugspring.service;

import fon.iot.smartplugspring.config.JwtTokenUtil;
import fon.iot.smartplugspring.dao.UserRepository;
import fon.iot.smartplugspring.dto.PasswordChangeRequest;
import fon.iot.smartplugspring.entity.UserEntity;
import fon.iot.smartplugspring.exceptions.InvalidHeaders;
import fon.iot.smartplugspring.exceptions.InvalidPasswordException;
import fon.iot.smartplugspring.exceptions.UserNotFoundException;
import fon.iot.smartplugspring.exceptions.WrongPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtil tokenUtil;

    @Autowired
    public UserService(UserRepository userRepository, JwtTokenUtil tokenUtil) {
        this.userRepository = userRepository;
        this.tokenUtil = tokenUtil;
    }

    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(long userID) {
        Optional<UserEntity> optionalUser = userRepository.findById(userID);
        if (!optionalUser.isPresent())
            throw new UserNotFoundException("User not found");

        return optionalUser.get();
    }

    public UserEntity saveUser(UserEntity user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw e;
        }
    }

    public UserEntity updateUser(long userID, UserEntity user) {
        Optional<UserEntity> optionalUser = userRepository.findById(userID);
        if (!optionalUser.isPresent())
            throw new UserNotFoundException("User not found");

        user.setId(userID);
        return userRepository.save(user);

    }

    public void deleteUser(long userID) {
        Optional<UserEntity> optionalUser = userRepository.findById(userID);
        if (!optionalUser.isPresent())
            throw new UserNotFoundException("User not found");

        userRepository.delete(new UserEntity(userID));

    }

    public UserEntity signin(String username, String password) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("User not found!");
        }
        UserEntity user = optionalUser.get();
        if (user.getPassword() != password) {
            throw new WrongPasswordException("Wrong password");
        }
        return user;
    }

    public void changePassword(HttpHeaders headers, PasswordChangeRequest request) throws InvalidPasswordException {
        String username = getUsernameFromHeaders(headers);
        System.out.println(username);
        UserEntity user = getUserByUsername(username);
        System.out.println(request.getCurrentPassword());

        if (new BCryptPasswordEncoder().matches(request.getCurrentPassword(), user.getPassword())) {
            user.setPassword(new BCryptPasswordEncoder().encode(request.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new InvalidPasswordException("Wrong password!");
        }


    }

    private String getUsernameFromHeaders(HttpHeaders headers) {
        String token = headers.getFirst("Authorization") == null ? "" :
                headers.getFirst("Authorization").substring(7);
        if (token.isEmpty()) {
            throw new InvalidHeaders("Unauthorized!");
        }
        return tokenUtil.getUsernameFromToken(token);
    }

    public UserEntity getUserByUsername(String username) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent())
            throw new UserNotFoundException("User not found!");
        return optionalUser.get();
    }
}
