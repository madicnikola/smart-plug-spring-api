package fon.iot.smartplugspring.service;

import fon.iot.smartplugspring.config.JwtTokenUtil;
import fon.iot.smartplugspring.dao.SmartPlugRepository;
import fon.iot.smartplugspring.entity.SmartPlug;
import fon.iot.smartplugspring.entity.UserEntity;
import fon.iot.smartplugspring.exceptions.InvalidHeaders;
import fon.iot.smartplugspring.exceptions.SmartPlugNotFoundException;
import fon.iot.smartplugspring.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SmartPlugService {
    private final SmartPlugRepository smartPlugRepository;
    private final UserService userService;
    private final JwtTokenUtil tokenUtil;

    @Autowired
    public SmartPlugService(SmartPlugRepository smartPlugRepository, UserService userService, JwtTokenUtil tokenUtil) {
        this.smartPlugRepository = smartPlugRepository;
        this.userService = userService;
        this.tokenUtil = tokenUtil;
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


    public List<SmartPlug> getMySmartPlugs(HttpHeaders headers) {
        String ownerUsername = getOwnerUsernameFromHeaders(headers);
        return smartPlugRepository.findAllByOwner_Username(ownerUsername);
    }

    public SmartPlug switchSmartPlug(HttpHeaders headers, Long plugID, boolean powerState) {
        String username = getOwnerUsernameFromHeaders(headers);
        SmartPlug smartPlug = getSmartPlug(plugID);
        if (!smartPlug.getOwner().getUsername().equals(username)) {
            System.out.println("exception");
            throw new UnauthorizedException("Unauthorized!");
        }
        // send request to arduino ..
        // if successful
        smartPlug.setTurnedOn(powerState);
        return updateSmartPlug(plugID, smartPlug);
    }

    private String getOwnerUsernameFromHeaders(HttpHeaders headers) {
        String token = headers.getFirst("Authorization") == null ? "" : headers.getFirst("Authorization").substring(7);
        if (token.isEmpty())
            throw new InvalidHeaders("Unauthorized!");

        return tokenUtil.getUsernameFromToken(token);
    }

    public void assignSmartPlugToUser(HttpHeaders headers, Long plugId) {
        String username = getOwnerUsernameFromHeaders(headers);
        UserEntity user = userService.getUserByUsername(username);
        Optional<SmartPlug> optionalSmartPlug = smartPlugRepository.findById(plugId);
        if (optionalSmartPlug.isPresent()) {
            SmartPlug smartPlug = optionalSmartPlug.get();
            smartPlug.setOwner(user);
            smartPlugRepository.save(smartPlug);
        } else {
            throw new SmartPlugNotFoundException("Smart plug with id: " + plugId + " not found.");
        }
    }

}
