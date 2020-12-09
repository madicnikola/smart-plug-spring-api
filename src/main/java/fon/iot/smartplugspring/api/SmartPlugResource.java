package fon.iot.smartplugspring.api;


import fon.iot.smartplugspring.config.JwtTokenUtil;
import fon.iot.smartplugspring.entity.SmartPlug;
import fon.iot.smartplugspring.entity.UserEntity;
import fon.iot.smartplugspring.exception.InvalidHeaders;
import fon.iot.smartplugspring.service.SmartPlugService;
import fon.iot.smartplugspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/smart-plugs")
public class SmartPlugResource {
    private final SmartPlugService smartPlugService;
    private final JwtTokenUtil tokenUtil;
    private final UserService userService;

    @Autowired
    public SmartPlugResource(SmartPlugService smartPlugService, JwtTokenUtil tokenUtil, UserService userService) {
        this.smartPlugService = smartPlugService;
        this.tokenUtil = tokenUtil;
        this.userService = userService;
    }

    @GetMapping("/{plugID}")
    public SmartPlug getSmartPlug(@PathVariable("plugID") Long plugID) {
        return smartPlugService.getSmartPlug(plugID);
    }

    @GetMapping("/get-my-smart-plugs")
    public List<SmartPlug> getMySmartPlugs(@RequestHeader HttpHeaders headers) {
        return smartPlugService.getMySmartPlugs(getOwnerUsernameFromHeaders(headers));
    }


    private String getOwnerUsernameFromHeaders(HttpHeaders headers) {
        String token = headers.getFirst("Authorization") == null ? "" : headers.getFirst("Authorization").substring(7);
        if(token.isEmpty())
            throw new InvalidHeaders("Unauthorized!");
        return tokenUtil.getUsernameFromToken(token);

    }

    @PutMapping("/{plugID}")
    public SmartPlug updateSmartPlug(@PathVariable("plugID") Long plugID, @RequestBody SmartPlug smartPlug) {
        return smartPlugService.updateSmartPlug(plugID, smartPlug);
    }

    @PutMapping("/{plugID}/switch")
    public SmartPlug smartPlugSwitch(@RequestHeader HttpHeaders headers, @PathVariable("plugID") Long plugID, @RequestBody boolean powerState) {
        String ownerUsername = getOwnerUsernameFromHeaders(headers);
        return smartPlugService.switchSmartPlug(ownerUsername, plugID, powerState);
    }

    @DeleteMapping("/{plugID}")
    public SmartPlug deleteSmartPlug(@PathVariable("plugID") Long plugID) {
        return smartPlugService.deleteSmartPlug(plugID);
    }

    @GetMapping("/register/{plugID}")
    public SmartPlug registerSmartPlug(HttpServletRequest request, @PathVariable("plugID") Long plugID) {
        String ipAddress = request.getRemoteAddr();
        return smartPlugService.registerSmartPlug(plugID, ipAddress);
    }

    @GetMapping("/register2/{plugID}")
    public SmartPlug register2SmartPlug(HttpServletRequest request, @PathVariable("plugID") Long plugID) {
        String ipAddress = request.getRemoteAddr();
        return smartPlugService.registerSmartPlug(plugID, ipAddress);
    }

//    private Long getOwnerIDFromHeaders(HttpHeaders headers) {
//        String token = headers.getFirst("Authorization").substring(7);
//        String username = tokenUtil.getUsernameFromToken(token);
//        UserEntity owner = userService.getUserByUsername(username);
//        return owner.getId();
//    }

//    @GetMapping("/plugstestdata")
//    public List<SmartPlug> getDummyPlugs() {
//        List<SmartPlug> smartPlugs = new LinkedList<SmartPlug>() {{
//            add(new SmartPlug(1L, "Plug1", false, "192.185.165.1", new User(3L)));
//            add(new SmartPlug(2L, "Plug2", true, "192.185.165.2", new User(3L)));
//        }};
//        return smartPlugService.saveAll(smartPlugs);
//    }


}
