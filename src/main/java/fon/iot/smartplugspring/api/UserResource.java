package fon.iot.smartplugspring.api;

import fon.iot.smartplugspring.dto.MessageResponse;
import fon.iot.smartplugspring.dto.PasswordChangeRequest;
import fon.iot.smartplugspring.entity.UserEntity;
import fon.iot.smartplugspring.exceptions.InvalidPasswordException;
import fon.iot.smartplugspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> updateUser(@PathVariable("userID") long userID, @RequestBody UserEntity user) {
        userService.updateUser(userID, user);
        return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
    }

    @DeleteMapping("/{userID}")
    public void deleteUser(@PathVariable("userID") long userID) {
        userService.deleteUser(userID);
    }

    @PutMapping("/password-change")
    public ResponseEntity<MessageResponse> changePassword(@RequestHeader HttpHeaders headers, @RequestBody PasswordChangeRequest request) {
        try {
            userService.changePassword(headers, request);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Password changed successfully"));
        } catch (InvalidPasswordException e) {
//            e.printStackTrace();
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage() + " Please check and try again"));
        }
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
