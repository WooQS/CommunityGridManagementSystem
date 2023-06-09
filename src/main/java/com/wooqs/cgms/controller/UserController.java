package com.wooqs.cgms.controller;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.wooqs.cgms.model.User;
import com.wooqs.cgms.service.UserService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    //注入UserService
    @Autowired
    private UserService userService;

    //根据id查询用户
    @GetMapping("/id/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //根据用户名查询用户
    @GetMapping("/{username}")
    public ResponseEntity<List<User>> getByUsername(@PathVariable String username) {
        List<User> user = userService.search(username);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //查询所有用户
    @GetMapping("")
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAll();
        if (users != null && !users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //保存用户信息
    @PostMapping("")
    public ResponseEntity<Void> save(@RequestBody User user) {
        try{userService.save(user);}
        catch(Exception MySQLIntegrityConstraintViolationException){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //更新用户信息
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody User user) {
        user.setUserId(id);
        try{userService.update(user);}
        catch(Exception MySQLIntegrityConstraintViolationException){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //根据id删除用户信息
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user != null) {
            try{
                userService.deleteById(id);
            }catch (Exception MySQLIntegrityConstraintViolationException){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<User> login(@RequestParam String username, @RequestParam String password) {
        User loginUser = userService.getByUsername(username);
        if (loginUser != null && loginUser.getPassword().equals(password)) {
            loginUser.setLastLoginTime(LocalDateTime.now());
            update(loginUser.getUserId(),loginUser);
            if (loginUser.getRoleId() == 1 || loginUser.getRoleId() == 2 ) {
                return new ResponseEntity<>(loginUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
