package fon.iot.smartplugspring.api;


import fon.iot.smartplugspring.dto.MessageResponse;
import fon.iot.smartplugspring.entity.SmartPlug;
import fon.iot.smartplugspring.exceptions.SmartPlugServiceException;
import fon.iot.smartplugspring.service.SmartPlugService;
import fon.iot.smartplugspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/smart-plugs")
public class SmartPlugResource {
    private final SmartPlugService smartPlugService;
    private final UserService userService;

    @Autowired
    public SmartPlugResource(SmartPlugService smartPlugService, UserService userService) {
        this.smartPlugService = smartPlugService;
        this.userService = userService;
    }

    @GetMapping("/{plugID}")
    public SmartPlug getSmartPlug(@PathVariable("plugID") Long plugID) {
        return smartPlugService.getSmartPlug(plugID);
    }

    @GetMapping("/get-all-my-smart-plugs")
    public List<SmartPlug> getMySmartPlugs(@RequestHeader HttpHeaders headers) {
        return smartPlugService.getMySmartPlugs(headers);
    }


    @PutMapping("/{plugID}")
    public ResponseEntity<SmartPlug> updateSmartPlug(@PathVariable("plugID") Long plugID, @RequestBody SmartPlug smartPlug) {
        try {
            return ResponseEntity.ok(smartPlugService.updateSmartPlug(plugID, smartPlug));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }


    }

    @PutMapping("/{plugID}/switch")
    public SmartPlug smartPlugSwitch(@RequestHeader HttpHeaders headers, @PathVariable("plugID") Long plugID, @RequestBody boolean powerState) {
        return smartPlugService.switchSmartPlug(headers, plugID, powerState);
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

    @PutMapping("user/save/all")
    public ResponseEntity<String> saveAll(@RequestBody List<SmartPlug> smartPlugs) {
        try {
            smartPlugService.saveAll(smartPlugs);
            return ResponseEntity.status(HttpStatus.OK).body("Smart plugs saved successfully");
        } catch (SmartPlugServiceException e) {
//            e.printStackTrace();
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/assign")
    public ResponseEntity<MessageResponse> assignSmartPlugToUser(@RequestHeader HttpHeaders headers, @RequestBody Long plugId) {
        try {
            smartPlugService.assignSmartPlugToUser(headers, plugId);
            return ResponseEntity.ok(new MessageResponse("Smart plug assigned successfully"));
        } catch (SmartPlugServiceException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));

        }

    }
}
