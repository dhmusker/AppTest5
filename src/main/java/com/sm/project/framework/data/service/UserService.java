package com.sm.project.framework.data.service;

import com.sm.project.framework.data.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;


@Service
public class UserService extends CrudService<User, Integer> {

    private UserRepository repository;

    public UserService(@Autowired UserRepository repository) {
        this.repository = repository;
    }

    @Override
    protected UserRepository getRepository() {
        return repository;
    }

    public List<User> getBySurname(Integer organisation, String lastName ) {
        List<User> list = repository.getByLastName( organisation, lastName );
        return list;
    }

    public List<User> getByEmail(Integer organisation, String email ) {
        List<User> list = repository.getByEmail( organisation, email );
        return list;
    }

    public List<Integer> getIdByEmail(Integer organisation, String email ) {
        return repository.getIdByEmail( organisation, email );
    }

    public List<String> findNamesByTitle(Integer organisation, String title ) {
        return repository.findNamesByTitle( organisation, title );
    }

    public List<User> findAll(Integer organisation) {
        return repository.findAll( organisation );
    }

}
