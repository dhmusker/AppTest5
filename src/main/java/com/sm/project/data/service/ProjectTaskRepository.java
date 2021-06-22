package com.sm.project.data.service;

import com.sm.project.data.entity.ProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Integer> {

    @Query("SELECT COUNT(p) FROM ProjectTask p WHERE p.category = ?1")
    public int getCountByCategory( String category );

    @Query("SELECT p FROM ProjectTask p WHERE p.category = ?1")
    public List<ProjectTask> findByCategory(String category);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProjectTask p WHERE p.category = ?1")
    public void deleteByCategory( String category );

}
