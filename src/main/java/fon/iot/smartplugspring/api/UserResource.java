package fon.iot.smartplugspring.api;

import fon.iot.smartplugspring.entity.UserEntity;
import fon.iot.smartplugspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserResource {
    private final UserService userService;


    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<UserEntity> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userID}")
    public UserEntity getUser(@PathVariable("userID") long userID) {
        return userService.getUserById(userID);
    }

    @PostMapping("/add")
    public UserEntity addNewUser(@RequestBody UserEntity user) {
        try {
            return userService.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @PutMapping("/{userID}")
    public UserEntity updateUser(@PathVariable("userID") long userID, @RequestBody UserEntity user) {

        return userService.updateUser(userID, user);
    }

    @DeleteMapping("/{userID}")
    public void deleteUser(@PathVariable("userID") long userID) {
        userService.deleteUser(userID);
    }

//    @GetMapping("/dummy")
//    public UserEntity saveDummyUser() {
//
//        return null;
//    }
//    @GetMapping("/{username}")
//    public org.springframework.security.core.userdetails.UserDetails getUser(@PathVariable("username") String username) {
//        return userDetailsService.loadUserByUsername(username);
//    }
}
