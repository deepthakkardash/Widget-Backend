package com.example.Widget.in.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Widget.in.entities.user_widget;

import java.util.List;

@Repository
public interface UserWidgetRepository extends JpaRepository<user_widget,Integer> {
    List<user_widget> findByUser_Userid(int userid);
    void deleteByUser_Userid(int userid);
}
