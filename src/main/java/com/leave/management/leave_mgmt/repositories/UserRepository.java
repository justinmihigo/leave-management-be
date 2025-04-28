package com.leave.management.leave_mgmt.repositories;

import com.leave.management.leave_mgmt.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    List<User> findByDepartment(String department);
}