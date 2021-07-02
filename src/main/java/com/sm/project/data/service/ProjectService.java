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

    public List<Project> findAll( Integer organisation ) {
        List<Project> list = repository.findAll( organisation );
        return list;
    }

    public List<Project> getByTitle( Integer organisation, String title ) {
        List<Project> list = repository.getByTitle( organisation, title );
        return list;
    }

    public void deleteByProjectId( Integer organisation, Integer projectId ) {
        repository.deleteByProjectId( organisation, projectId );
    }

    public void saveAll( List<Project> projects) {
        repository.saveAll( projects );
    }

    public int getTestCount( Integer organisation ) {
        return repository.getCountByCategoryName( organisation, "Test");
    }

    public List<Project> findAllTestRecords( Integer organisation ) {
        List<Project> list = repository.findByCategoryName(organisation, "Test");
        return list;
    }

    public void deleteAllTestRecords( Integer organisation ) {
        repository.deleteByCategoryName(organisation, "Test");
    }

}
