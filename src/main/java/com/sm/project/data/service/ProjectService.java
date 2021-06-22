package com.sm.project.data.service;

import com.sm.project.data.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;

@Service
public class ProjectService extends CrudService<Project, Integer> {

    private ProjectRepository repository;

    public ProjectService(@Autowired ProjectRepository repository) {
            this.repository = repository;
        }

    @Override
    protected ProjectRepository getRepository() {
            return repository;
        }

    public List<Project> findAll() {
        List<Project> list = repository.findAll();
        return list;
    }

    public List<Project> getByTitle( String title ) {
        List<Project> list = repository.getByTitle( title );
        return list;
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void saveAll( List<Project> projects) {
        repository.saveAll( projects );
    }

    public int getTestCount() {
        return repository.getCountByCategory("Test");
    }

    public List<Project> findAllTestRecords() {
        List<Project> list = repository.findByCategory("Test");
        return list;
    }

    public void deleteAllTestRecords() {
        repository.deleteByCategory("Test");
    }

}
