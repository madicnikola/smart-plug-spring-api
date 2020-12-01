package fon.iot.smartplugspring.api;


import fon.iot.smartplugspring.config.JwtTokenUtil;
import fon.iot.smartplugspring.entity.SmartPlug;
import fon.iot.smartplugspring.entity.UserEntity;
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

    @GetMapping("/smart-plug/{plugID}")
    public SmartPlug getSmartPlug(@PathVariable("plugID") Long plugID) {
        return smartPlugService.getSmartPlug(plugID);
    }

    @GetMapping("/user/{ownerID}/smart-plugs")
    public List<SmartPlug> getSmartPlugs(@PathVariable("ownerID") Long ownerID) {
        return smartPlugService.getSmartPlugs(ownerID);
    }
    @GetMapping("/get-my-smart-plugs")
    public List<SmartPlug> getMySmartPlugs(@RequestHeader HttpHeaders headers) {
        return smartPlugService.getSmartPlugs(getOwnerIDFromToken(headers));
    }

    private Long getOwnerIDFromToken(HttpHeaders headers) {
        String token = headers.getFirst("Authorization").substring(7);
        String username = tokenUtil.getUsernameFromToken(token);
        UserEntity owner = userService.getUserByUsername(username);
        return owner.getId();
    }

    @PutMapping("/smart-plug/{plugID}")
    public SmartPlug updateSmartPlug(@PathVariable("plugID") Long plugID, @RequestBody SmartPlug smartPlug) {
        return smartPlugService.updateSmartPlug(plugID, smartPlug);
    }

    @DeleteMapping("/smart-plug/{plugID}")
    public SmartPlug deleteSmartPlug(@PathVariable("plugID") Long plugID) {
        return smartPlugService.deleteSmartPlug(plugID);
    }

    @GetMapping("/smart-plug/register/{plugID}")
    public SmartPlug registerSmartPlug(HttpServletRequest request, @PathVariable("plugID") Long plugID) {
        String ipAddress = request.getRemoteAddr();
        return smartPlugService.registerSmartPlug(plugID, ipAddress);
    }

    @GetMapping("/smart-plug/register2/{plugID}")
    public SmartPlug register2SmartPlug(HttpServletRequest request, @PathVariable("plugID") Long plugID) {
        String ipAddress = request.getRemoteAddr();
        return smartPlugService.registerSmartPlug(plugID, ipAddress);
    }


//    @GetMapping("/plugstestdata")
//    public List<SmartPlug> getDummyPlugs() {
//        List<SmartPlug> smartPlugs = new LinkedList<SmartPlug>() {{
//            add(new SmartPlug(1L, "Plug1", false, "192.185.165.1", new User(3L)));
//            add(new SmartPlug(2L, "Plug2", true, "192.185.165.2", new User(3L)));
//        }};
//        return smartPlugService.saveAll(smartPlugs);
//    }


}
