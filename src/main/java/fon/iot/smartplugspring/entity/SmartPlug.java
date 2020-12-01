package fon.iot.smartplugspring.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class SmartPlug {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private boolean turnedOn;
    private String ipAddress;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    public SmartPlug() {
    }


    public SmartPlug(Long id) {
        this.id = id;
    }

    public SmartPlug(Long id, String name, boolean turnedOn, String ipAddress, UserEntity owner) {
        this.id = id;
        this.name = name;
        this.turnedOn = turnedOn;
        this.ipAddress = ipAddress;
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTurnedOn() {
        return turnedOn;
    }

    public void setTurnedOn(boolean turnedOn) {
        this.turnedOn = turnedOn;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "SmartPlug{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", turnedOn=" + turnedOn +
                ", ipAddress='" + ipAddress + '\'' +
                ", owner=" + owner +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SmartPlug)) return false;
        SmartPlug smartPlug = (SmartPlug) o;
        return id.equals(smartPlug.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
