package com.leave.management.leave_mgmt.repositories;

import com.leave.management.leave_mgmt.models.Leave;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends MongoRepository<Leave, String> {
    List<Leave> findByUserId(String userId);
    List<Leave> findByStatus(String status);
}