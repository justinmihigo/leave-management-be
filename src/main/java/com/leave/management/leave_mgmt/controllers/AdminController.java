package com.leave.management.leave_mgmt.controllers;

import com.leave.management.leave_mgmt.models.Leave;
import com.leave.management.leave_mgmt.models.User;
import com.leave.management.leave_mgmt.services.LeaveService;
import com.leave.management.leave_mgmt.services.UserService;
import com.leave.management.leave_mgmt.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final LeaveService leaveService;
    private final UserService userService;
    private final AdminService adminService;

    public AdminController(LeaveService leaveService, UserService userService, AdminService adminService) {
        this.leaveService = leaveService;
        this.userService = userService;
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/leaves")
    public ResponseEntity<List<Leave>> getAllLeaves() {
        List<Leave> leaves = leaveService.getAllLeaves();
        return ResponseEntity.ok(leaves);
    }

    @PutMapping("/users/{id}/adjust-leave-balance")
    public ResponseEntity<User> adjustLeaveBalance(@PathVariable String id, @RequestBody Map<String, Integer> requestBody) {
        if (!requestBody.containsKey("newBalance")) {
            return ResponseEntity.badRequest().body(null);
        }
        int newBalance = requestBody.get("newBalance");
        User updatedUser = userService.adjustLeaveBalance(id, newBalance);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/export/users")
    public ResponseEntity<String> exportUsersToCSV() {
        try {
            String filePath = adminService.exportUsersToCSV();
            return ResponseEntity.ok("Users exported to: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error exporting users: " + e.getMessage());
        }
    }

    @GetMapping("/export/leaves")
    public ResponseEntity<String> exportLeavesToCSV() {
        try {
            String filePath = adminService.exportLeavesToCSV();
            return ResponseEntity.ok("Leaves exported to: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error exporting leaves: " + e.getMessage());
        }
    }

    @GetMapping("/export/users/excel")
    public ResponseEntity<String> exportUsersToExcel() {
        try {
            String filePath = adminService.exportUsersToExcel();
            return ResponseEntity.ok("Users exported to: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error exporting users to Excel: " + e.getMessage());
        }
    }

    @GetMapping("/export/leaves/excel")
    public ResponseEntity<String> exportLeavesToExcel() {
        try {
            String filePath = adminService.exportLeavesToExcel();
            return ResponseEntity.ok("Leaves exported to: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error exporting leaves to Excel: " + e.getMessage());
        }
    }

    @GetMapping("/report/department")
    public ResponseEntity<String> generateReportByDepartment(@RequestBody String department) {
        try {
            String filePath = adminService.generateReportByDepartment(department);
            return ResponseEntity.ok("Department report generated at: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error generating department report: " + e.getMessage());
        }
    }

    @GetMapping("/report/leave-type")
    public ResponseEntity<String> generateReportByLeaveType(@RequestParam String leaveType) {
        try {
            String filePath = adminService.generateReportByLeaveType(leaveType);
            return ResponseEntity.ok("Leave type report generated at: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error generating leave type report: " + e.getMessage());
        }
    }
}