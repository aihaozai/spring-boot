package com.example.myproject.mapper;
import com.example.myproject.entity.ApplyLeave;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyLeaveMapper {

    void updateApplyLeave(ApplyLeave applyLeave);
}