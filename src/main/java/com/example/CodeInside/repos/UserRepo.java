package com.example.CodeInside.repos;

import com.example.CodeInside.models.User;
import com.example.CodeInside.models.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT u.id FROM User u JOIN u.roles r WHERE r.name IN (:excludedRoles))")
    List<User> findEmployeesWithoutRoles(@Param("excludedRoles") List<ERole> excludedRoles);
    List<User> findAll();
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

}
