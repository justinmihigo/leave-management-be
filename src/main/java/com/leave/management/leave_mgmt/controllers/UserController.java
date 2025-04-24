package com.leave.management.leave_mgmt.controllers;

import com.leave.management.leave_mgmt.models.User;
import com.leave.management.leave_mgmt.models.Leave;
import com.leave.management.leave_mgmt.services.UserService;
import com.leave.management.leave_mgmt.services.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LeaveService leaveService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/leave-balance")
    public ResponseEntity<Integer> getLeaveBalance(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get().getLeaveBalance());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/leave-history")
    public ResponseEntity<List<Leave>> getLeaveHistory(@PathVariable String id) {
        List<Leave> leaveHistory = leaveService.getLeavesByUserId(id);
        return ResponseEntity.ok(leaveHistory);
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
} 