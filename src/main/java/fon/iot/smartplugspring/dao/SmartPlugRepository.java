package fon.iot.smartplugspring.dao;

import fon.iot.smartplugspring.entity.SmartPlug;
import fon.iot.smartplugspring.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmartPlugRepository extends JpaRepository<SmartPlug, Long> {


    List<SmartPlug> findAllByOwner(UserEntity owner);

    List<SmartPlug> findAllByOwner_Username(String owner_username);

}
