package com.sm.project.framework.data.service;

import com.sm.project.framework.data.entity.UserPermissionMembership;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;


@Service
public class UserPermissionService extends CrudService<UserPermissionMembership, Integer> {

    private UserPermissionRepository repository;

    public UserPermissionService(@Autowired UserPermissionRepository repository) {
        this.repository = repository;
    }

    @Override
    protected UserPermissionRepository getRepository() {
        return repository;
    }

    public List<UserPermissionMembership> getByUserId(Integer organisation, Integer id ) {
        List<UserPermissionMembership> list = repository.getByUserId( organisation, id );
        return list;
    }

    public List<UserPermissionMembership> getByPermission(Integer organisation, Integer id  ) {
        List<UserPermissionMembership> list = repository.getByPermission( organisation, id );
        return list;
    }

    public List<UserPermissionMembership> getUserPermission(Integer organisation,  Integer userId, Integer permissionId ) {
        return repository.getUserPermission( organisation, userId, permissionId );
    }

}
