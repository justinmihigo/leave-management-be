package com.leave.management.leave_mgmt.services;

import com.leave.management.leave_mgmt.models.Leave;
import com.leave.management.leave_mgmt.models.User;
import com.leave.management.leave_mgmt.repositories.LeaveRepository;
import com.leave.management.leave_mgmt.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Autowired
    public LeaveService(LeaveRepository leaveRepository, UserRepository userRepository, NotificationService notificationService) {
        this.leaveRepository = leaveRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public Leave applyForLeave(Leave leave) {
        leave.setStatus("Pending");
        Leave savedLeave = leaveRepository.save(leave);

        // Send email notification to the manager
        String managerEmail = "mihigojustin7@gmail.com";
        String subject = "New Leave Application Submitted";
        String body = "A new leave application has been submitted by user ID: " + leave.getUserId() + ".\n" +
                      "Leave Type: " + leave.getLeaveType() + "\n" +
                      "Start Date: " + leave.getStartDate() + "\n" +
                      "End Date: " + leave.getEndDate() + "\n" +
                      "Reason: " + leave.getReason();
        notificationService.sendEmail(managerEmail, subject, body);

        return savedLeave;
    }

    public Optional<Leave> getLeaveById(String id) {
        return leaveRepository.findById(id);
    }

    public List<Leave> getLeavesByUserId(String userId) {
        return leaveRepository.findByUserId(userId);
    }

    public List<Leave> getLeavesByStatus(String status) {
        return leaveRepository.findByStatus(status);
    }

    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }

    public Leave updateLeaveStatus(String id, String status, String approverComments) {
        Optional<Leave> leaveOptional = leaveRepository.findById(id);
        if (leaveOptional.isPresent()) {
            Leave leave = leaveOptional.get();
            leave.setStatus(status);
            leave.setApproverComments(approverComments);
            return leaveRepository.save(leave);
        }
        return null;
    }

    public Leave approveLeave(String leaveId, String approverComments) {
        Optional<Leave> leaveOptional = leaveRepository.findById(leaveId);
        if (leaveOptional.isPresent()) {
            Leave leave = leaveOptional.get();
            leave.setStatus("Approved");
            leave.setApproverComments(approverComments);
            leaveRepository.save(leave);

            // Send notification
            User user = userRepository.findById(leave.getUserId()).orElse(null);
            if (user != null) {
                notificationService.sendEmail(user.getEmail(), "Leave Approved", "Your leave request has been approved. Comments: " + approverComments);
            }

            return leave;
        }
        return null;
    }

    public Leave rejectLeave(String leaveId, String approverComments) {
        Optional<Leave> leaveOptional = leaveRepository.findById(leaveId);
        if (leaveOptional.isPresent()) {
            Leave leave = leaveOptional.get();
            leave.setStatus("Rejected");
            leave.setApproverComments(approverComments);
            leaveRepository.save(leave);

            // Send notification
            User user = userRepository.findById(leave.getUserId()).orElse(null);
            if (user != null) {
                notificationService.sendEmail(user.getEmail(), "Leave Rejected", "Your leave request has been rejected. Comments: " + approverComments);
            }

            return leave;
        }
        return null;
    }

    public void autoAccrueLeaveBalances() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            int newBalance = user.getLeaveBalance() + 2; // Assuming 1.66 days/month rounded to 2 for simplicity
            user.setLeaveBalance(newBalance);
            userRepository.save(user);
        }
    }

    public void carryForwardLeaveBalances() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            int currentBalance = user.getLeaveBalance();
            if (currentBalance > 20) { // Assuming 20 days max carry-forward
                user.setLeaveBalance(20);
            }
            userRepository.save(user);
        }
    }
}