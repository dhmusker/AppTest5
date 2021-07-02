package com.sm.project.data.service;

import com.sm.project.data.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("SELECT COUNT(p) FROM Project p WHERE p.organisation = ?1 and p.categoryName = ?2")
    public int getCountByCategoryName( Integer organisation, String categoryName );

    @Query("SELECT p FROM Project p WHERE p.organisation = ?1")
    public List<Project> findAll( Integer organisation);

    @Query("SELECT p FROM Project p WHERE p.organisation = ?1 and p.categoryName = ?2")
    public List<Project> findByCategoryName( Integer organisation, String categoryName);

    @Query("SELECT p FROM Project p WHERE p.organisation = ?1 and p.title = ?2")
    public List<Project> getByTitle( Integer organisation, String title );

    @Modifying
    @Transactional
    @Query("DELETE FROM Project p WHERE p.organisation = ?1 and p.categoryName = ?2")
    public void deleteByCategoryName( Integer organisation, String categoryName);

    @Modifying
    @Transactional
    @Query("DELETE FROM Project p WHERE p.organisation = ?1 and p.id = ?2")
    public void deleteByProjectId( Integer organisation, Integer id);

}
