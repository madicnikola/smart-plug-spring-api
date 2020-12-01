package fon.iot.smartplugspring.service;

import fon.iot.smartplugspring.dao.UserRepository;
import fon.iot.smartplugspring.entity.UserEntity;
import fon.iot.smartplugspring.exception.UserNotFoundException;
import fon.iot.smartplugspring.exception.WrongPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public UserEntity getUserByUsername(String username) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent())
            throw new UserNotFoundException("User not found!");
        return optionalUser.get();
    }
}
