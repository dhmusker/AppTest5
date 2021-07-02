package com.sm.project.framework.data.service;

import com.sm.project.framework.data.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;


@Service
public class PermissionService extends CrudService<Permission, Integer> {

    private PermissionRepository repository;

    public PermissionService(@Autowired PermissionRepository repository) {
        this.repository = repository;
    }

    @Override
    protected PermissionRepository getRepository() {
        return repository;
    }

    public List<Permission> getByName(Integer organisation, String name ) {
        List<Permission> list = repository.getByName( organisation, name );
        return list;
    }

    public List<Integer> getIdByName(Integer organisation, String name ) {
        return repository.getIdByName( organisation, name );
    }
}
