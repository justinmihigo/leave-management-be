package com.leave.management.leave_mgmt.services;

import com.leave.management.leave_mgmt.models.Leave;
import com.leave.management.leave_mgmt.models.User;
import com.leave.management.leave_mgmt.repositories.LeaveRepository;
import com.leave.management.leave_mgmt.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final LeaveRepository leaveRepository;

    public AdminService(UserRepository userRepository, LeaveRepository leaveRepository) {
        this.userRepository = userRepository;
        this.leaveRepository = leaveRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }

    public String exportUsersToCSV() throws IOException {
        List<User> users = getAllUsers();
        String filePath = "users.csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("ID,Name,Email,Role,Leave Balance\n");
            for (User user : users) {
                writer.append(user.getId()).append(",")
                      .append(user.getName()).append(",")
                      .append(user.getEmail()).append(",")
                      .append(user.getRole()).append(",")
                      .append(String.valueOf(user.getLeaveBalance())).append("\n");
            }
        }
        return filePath;
    }

    public String exportLeavesToCSV() throws IOException {
        List<Leave> leaves = getAllLeaves();
        String filePath = "leaves.csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("ID,User ID,Leave Type,Start Date,End Date,Status,Reason,Approver Comments\n");
            for (Leave leave : leaves) {
                writer.append(leave.getId()).append(",")
                      .append(leave.getUserId()).append(",")
                      .append(leave.getLeaveType()).append(",")
                      .append(leave.getStartDate().toString()).append(",")
                      .append(leave.getEndDate().toString()).append(",")
                      .append(leave.getStatus()).append(",")
                      .append(leave.getReason() != null ? leave.getReason() : "").append(",")
                      .append(leave.getApproverComments() != null ? leave.getApproverComments() : "").append("\n");
            }
        }
        return filePath;
    }

    public String exportUsersToExcel() throws IOException {
        List<User> users = getAllUsers();
        String filePath = "users.xlsx";
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Email");
            header.createCell(3).setCellValue("Role");
            header.createCell(4).setCellValue("Leave Balance");

            int rowIndex = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getName());
                row.createCell(2).setCellValue(user.getEmail());
                row.createCell(3).setCellValue(user.getRole());
                row.createCell(4).setCellValue(user.getLeaveBalance());
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
        return filePath;
    }

    public String exportLeavesToExcel() throws IOException {
        List<Leave> leaves = getAllLeaves();
        String filePath = "leaves.xlsx";
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Leaves");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("User ID");
            header.createCell(2).setCellValue("Leave Type");
            header.createCell(3).setCellValue("Start Date");
            header.createCell(4).setCellValue("End Date");
            header.createCell(5).setCellValue("Status");
            header.createCell(6).setCellValue("Reason");
            header.createCell(7).setCellValue("Approver Comments");

            int rowIndex = 1;
            for (Leave leave : leaves) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(leave.getId());
                row.createCell(1).setCellValue(leave.getUserId());
                row.createCell(2).setCellValue(leave.getLeaveType());
                row.createCell(3).setCellValue(leave.getStartDate().toString());
                row.createCell(4).setCellValue(leave.getEndDate().toString());
                row.createCell(5).setCellValue(leave.getStatus());
                row.createCell(6).setCellValue(leave.getReason() != null ? leave.getReason() : "");
                row.createCell(7).setCellValue(leave.getApproverComments() != null ? leave.getApproverComments() : "");
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
        return filePath;
    }

    public String generateReportByDepartment(String department) throws IOException {
        List<User> users = userRepository.findAll();
        String filePath = "department_report.csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("ID,Name,Email,Role,Leave Balance\n");
            for (User user : users) {
                if (user.getRole().equalsIgnoreCase(department)) {
                    writer.append(user.getId()).append(",")
                          .append(user.getName()).append(",")
                          .append(user.getEmail()).append(",")
                          .append(user.getRole()).append(",")
                          .append(String.valueOf(user.getLeaveBalance())).append("\n");
                }
            }
        }
        return filePath;
    }

    public String generateReportByLeaveType(String leaveType) throws IOException {
        List<Leave> leaves = leaveRepository.findAll();
        String filePath = "leave_type_report.csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("ID,User ID,Leave Type,Start Date,End Date,Status,Reason,Approver Comments\n");
            for (Leave leave : leaves) {
                if (leave.getLeaveType().equalsIgnoreCase(leaveType)) {
                    writer.append(leave.getId()).append(",")
                          .append(leave.getUserId()).append(",")
                          .append(leave.getLeaveType()).append(",")
                          .append(leave.getStartDate().toString()).append(",")
                          .append(leave.getEndDate().toString()).append(",")
                          .append(leave.getStatus()).append(",")
                          .append(leave.getReason() != null ? leave.getReason() : "").append(",")
                          .append(leave.getApproverComments() != null ? leave.getApproverComments() : "").append("\n");
                }
            }
        }
        return filePath;
    }
}