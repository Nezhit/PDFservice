package com.example.CodeInside.repos;

import com.example.CodeInside.models.Request;
import com.example.CodeInside.models.Role;
import com.example.CodeInside.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepo extends JpaRepository<Request,Long> {
    List<Request> findBySender(User sender);
    List<Request> findByRecipient(User recipient);
}
