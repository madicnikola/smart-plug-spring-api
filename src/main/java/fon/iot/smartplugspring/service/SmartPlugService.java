package fon.iot.smartplugspring.service;

import fon.iot.smartplugspring.dao.SmartPlugRepository;
import fon.iot.smartplugspring.entity.SmartPlug;
import fon.iot.smartplugspring.entity.UserEntity;
import fon.iot.smartplugspring.exception.SmartPlugNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SmartPlugService {
    private final SmartPlugRepository smartPlugRepository;

    @Autowired
    public SmartPlugService(SmartPlugRepository smartPlugRepository) {
        this.smartPlugRepository = smartPlugRepository;
    }

    public List<SmartPlug> getSmartPlugs(Long userID) {
        return smartPlugRepository.findAllByOwner(new UserEntity(userID));
    }

    public List<SmartPlug> saveAll(List<SmartPlug> smartPlugs) {
        return smartPlugRepository.saveAll(smartPlugs);
    }

    public SmartPlug updateSmartPlug(Long plugID, SmartPlug smartPlug) {
        smartPlug.setId(plugID);
        return smartPlugRepository.save(smartPlug);
    }

    public SmartPlug getSmartPlug(Long plugID) {
        Optional<SmartPlug> optionalPlug = smartPlugRepository.findById(plugID);
        if (!optionalPlug.isPresent()) {
            throw new SmartPlugNotFoundException("SmartPlug is not found");
        }
        return optionalPlug.get();
    }

    public SmartPlug deleteSmartPlug(Long plugID) {
        Optional<SmartPlug> optionalPlug = smartPlugRepository.findById(plugID);
        if (!optionalPlug.isPresent()) {
            throw new SmartPlugNotFoundException("SmartPlug is not found");
        }
        smartPlugRepository.delete(optionalPlug.get());
        return optionalPlug.get();
    }

    public SmartPlug registerSmartPlug(Long plugID, String ipAddress) {
        SmartPlug plug = new SmartPlug(plugID);
        plug.setIpAddress(ipAddress);
        return smartPlugRepository.save(plug);
    }

    public List<SmartPlug> getSmartMyPlugs(String ownerUsername) {
        return smartPlugRepository.findAllByOwner(new UserEntity(ownerUsername));
    }
}
