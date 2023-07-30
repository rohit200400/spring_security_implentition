package spring.security.demo.controler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spring.security.demo.entity.DemoUser;
import spring.security.demo.service.DemoUserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class DemoController {

    @Autowired
    private DemoUserService demoUserService;
    @GetMapping("/all")
    public ResponseEntity<List<DemoUser>> getAllUsers(){

        return new ResponseEntity<>(demoUserService.getAllUsers(), HttpStatus.FOUND);
    }

    @PostMapping("/add")
    public ResponseEntity<DemoUser> add(@RequestBody DemoUser user){
        DemoUser savedUser =demoUserService.add(user);
        if (savedUser != null) return ResponseEntity.ok(savedUser);
        else return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
    }
}
