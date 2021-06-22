package com.sm.project.data.service;

import com.sm.project.data.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("SELECT COUNT(p) FROM Project p WHERE p.category = ?1")
    public int getCountByCategory( String category );

    @Query("SELECT p FROM Project p WHERE p.category = ?1")
    public List<Project> findByCategory( String category);

    @Query("SELECT p FROM Project p WHERE p.title = ?1")
    public List<Project> getByTitle( String title );

    @Modifying
    @Transactional
    @Query("DELETE FROM Project p WHERE p.category = ?1")
    public void deleteByCategory( String category);
}
