package com.main_project.movie_service.repository;

import com.main_project.movie_service.entity.SeatSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatScheduleRepository extends JpaRepository<SeatSchedule, String> {
}
