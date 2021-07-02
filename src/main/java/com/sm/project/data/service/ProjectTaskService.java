package com.sm.project.data.service;

import com.sm.project.data.entity.Project;
import com.sm.project.data.entity.ProjectTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;

@Service
public class ProjectTaskService extends CrudService<ProjectTask, Integer> {

    private ProjectTaskRepository repository;

    public ProjectTaskService(@Autowired ProjectTaskRepository repository) {
        this.repository = repository;
    }

    @Override
    protected ProjectTaskRepository getRepository() {
        return repository;
    }


    public void deleteAll() {
        repository.deleteAll();
    }

    public void saveAll( List<ProjectTask> projectTasks) {
        repository.saveAll( projectTasks );
    }

    public int getTestCount() {
        return repository.getCountByCategory("Test");
    }

    public List<ProjectTask> findAllTestRecords() {
        List<ProjectTask> list = repository.findByCategory("Test");
        return list;
    }

    public void deleteAllTestRecords() {
        repository.deleteByCategory("Test");
    }

    public List<ProjectTask> findByProjectId( Integer projectId) {
        List<ProjectTask> list = repository.findByProjectId(projectId);
        return list;
    }

    public void deleteById ( Integer taskId ) {
        repository.deleteById( taskId );
    }

    public ProjectTask update(ProjectTask entity) {
        return repository.save(entity);
    }
}
