package com.sm.project.framework.data.service;

import com.sm.project.framework.data.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;


@Service
public class RoleService extends CrudService<Role, Integer> {

    private RoleRepository repository;

    public RoleService(@Autowired RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    protected RoleRepository getRepository() {
        return repository;
    }

    public List<Role> getByName(Integer organisation, String name ) {
        List<Role> list = repository.getByName( organisation, name );
        return list;
    }

    public List<Integer> getIdByName(Integer organisation, String name ) {
        return repository.getIdByName( organisation, name );
    }

}
