package fon.iot.smartplugspring.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue
    private long id;
    @NotNull
    private String username;
    private String password;
    @Transient
    private String permissions;

    public UserEntity() {
    }

    public UserEntity(long id) {
        this.id = id;
    }

    public UserEntity(String username) {
        this.username = username;
    }

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public UserEntity(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserEntity(String username, String password, String permissions) {
        this.username = username;
        this.password = password;
        this.permissions = permissions;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String role) {
        this.permissions = role;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + permissions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity user = (UserEntity) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
