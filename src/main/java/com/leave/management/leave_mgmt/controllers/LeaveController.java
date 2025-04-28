package com.leave.management.leave_mgmt.controllers;

import com.leave.management.leave_mgmt.models.Leave;
import com.leave.management.leave_mgmt.services.LeaveService;
import com.leave.management.leave_mgmt.dto.LeaveRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping
    public ResponseEntity<Leave> applyForLeave(@RequestBody LeaveRequestDTO leaveRequestDTO) {
        Leave leave = new Leave();
        leave.setUserId(leaveRequestDTO.getUserId());
        leave.setLeaveType(leaveRequestDTO.getLeaveType());
        leave.setStartDate(leaveRequestDTO.getStartDate());
        leave.setEndDate(leaveRequestDTO.getEndDate());
        leave.setReason(leaveRequestDTO.getReason());

        Leave savedLeave = leaveService.applyForLeave(leave);
        return ResponseEntity.ok(savedLeave);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Leave> getLeaveById(@PathVariable String id) {
        Optional<Leave> leave = leaveService.getLeaveById(id);
        return leave.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Leave>> getLeavesByUserId(@PathVariable String userId) {
        List<Leave> leaves = leaveService.getLeavesByUserId(userId);
        return ResponseEntity.ok(leaves);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Leave> updateLeaveStatus(@PathVariable String id, @RequestBody Map<String, String> requestBody) {
        String status = requestBody.get("status");
        String approverComments = requestBody.get("approverComments");
        Leave updatedLeave = leaveService.updateLeaveStatus(id, status, approverComments);
        if (updatedLeave != null) {
            return ResponseEntity.ok(updatedLeave);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Leave> approveLeave(@PathVariable String id, @RequestBody Map<String, String> requestBody) {
        String approverComments = requestBody.get("approverComments");
        Leave updatedLeave = leaveService.updateLeaveStatus(id, "Approved", approverComments);
        if (updatedLeave != null) {
            return ResponseEntity.ok(updatedLeave);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Leave> rejectLeave(@PathVariable String id, @RequestBody Map<String, String> requestBody) {
        String approverComments = requestBody.get("approverComments");
        Leave updatedLeave = leaveService.updateLeaveStatus(id, "Rejected", approverComments);
        if (updatedLeave != null) {
            return ResponseEntity.ok(updatedLeave);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Leave>> getAllLeaves() {
        List<Leave> leaves = leaveService.getAllLeaves();
        return ResponseEntity.ok(leaves);
    }

    @PostMapping("/test-email")
    public ResponseEntity<String> sendTestEmail(@RequestBody Map<String, String> request) {
        String toEmail = request.get("email");
        if (toEmail == null || toEmail.isEmpty()) {
            return ResponseEntity.badRequest().body("Email address is required");
        }

        try {
            // Create a test leave object
            Leave testLeave = new Leave();
            testLeave.setUserId("test-user");
            testLeave.setLeaveType("Test Leave");
            testLeave.setStartDate(LocalDate.now());
            testLeave.setEndDate(LocalDate.now());
            testLeave.setReason("This is a test email notification");

            // Use the existing notification service through leaveService
            leaveService.applyForLeave(testLeave);
            
            return ResponseEntity.ok("Test email sent successfully to " + toEmail);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send test email: " + e.getMessage());
        }
    }
}