package com.sm.project.framework.data.service;

import com.sm.project.framework.data.entity.UserPermissionMembership;
import com.sm.project.framework.data.entity.UserRoleMembership;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;


@Service
public class UserRoleService extends CrudService<UserRoleMembership, Integer> {

    private UserRoleRepository repository;

    public UserRoleService(@Autowired UserRoleRepository repository) {
        this.repository = repository;
    }

    @Override
    protected UserRoleRepository getRepository() {
        return repository;
    }

    public List<UserRoleMembership> getByUserId(Integer organisation, Integer userId ) {
        List<UserRoleMembership> list = repository.getByUserId( organisation, userId );
        return list;
    }

    public List<UserRoleMembership> getByRole(Integer organisation, Integer roleId  ) {
        List<UserRoleMembership> list = repository.getByRole( organisation, roleId );
        return list;
    }

    public List<UserRoleMembership> getUserRole( Integer organisation, Integer userId, Integer roleId ) {
        return repository.getUserRole( organisation, userId, roleId );
    }
}
