package com.example.Widget.in.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Widget.in.entities.user;

@Repository
public interface UserRepository extends JpaRepository<user,Integer> {
    user findByUsername(String username);
}
