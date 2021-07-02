package com.sm.project.framework.data.service;

import com.sm.project.framework.data.entity.Organisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;


@Service
public class OrganisationService extends CrudService<Organisation, Integer> {

    private OrganisationRepository repository;

    public OrganisationService(@Autowired OrganisationRepository repository) {
        this.repository = repository;
    }

    @Override
    protected OrganisationRepository getRepository() {
        return repository;
    }

    public List<Organisation> getByName(String name ) {
        List<Organisation> list = repository.getByName( name );
        return list;
    }

    public List<Integer> getIdByName(String name ) {
        return repository.getIdByName( name );
    }
}
