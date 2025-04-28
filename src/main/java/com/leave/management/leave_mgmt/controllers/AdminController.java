package com.leave.management.leave_mgmt.controllers;

import com.leave.management.leave_mgmt.models.Leave;
import com.leave.management.leave_mgmt.models.User;
import com.leave.management.leave_mgmt.services.LeaveService;
import com.leave.management.leave_mgmt.services.UserService;
import com.leave.management.leave_mgmt.services.AdminService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
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
    public ResponseEntity<InputStreamResource> exportUsersToCSV() {
        try {
            String filePath = adminService.exportUsersToCSV();
            File file = new File(filePath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/export/leaves")
    public ResponseEntity<InputStreamResource> exportLeavesToCSV() {
        try {
            String filePath = adminService.exportLeavesToCSV();
            File file = new File(filePath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=leaves.csv");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/export/users/excel")
    public ResponseEntity<InputStreamResource> exportUsersToExcel() {
        try {
            String filePath = adminService.exportUsersToExcel();
            File file = new File(filePath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/export/leaves/excel")
    public ResponseEntity<InputStreamResource> exportLeavesToExcel() {
        try {
            String filePath = adminService.exportLeavesToExcel();
            File file = new File(filePath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=leaves.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
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

    @GetMapping("/export/users/department/{department}/csv")
    public ResponseEntity<InputStreamResource> exportUsersByDepartmentToCSV(@PathVariable String department) {
        try {
            String filePath = adminService.exportUsersByDepartmentToCSV(department);
            File file = new File(filePath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users_" + department + ".csv");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/export/users/department/{department}/excel")
    public ResponseEntity<InputStreamResource> exportUsersByDepartmentToExcel(@PathVariable String department) {
        try {
            String filePath = adminService.exportUsersByDepartmentToExcel(department);
            File file = new File(filePath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users_" + department + ".xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}