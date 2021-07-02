package com.sm.project.framework.data.service;

import com.sm.project.framework.data.entity.MasterReferenceData;
import com.sm.project.framework.data.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;


@Service
public class MasterReferenceDataService extends CrudService<MasterReferenceData, Integer> {

    private MasterReferenceDataRepository repository;

    public MasterReferenceDataService(@Autowired MasterReferenceDataRepository repository) {
        this.repository = repository;
    }

    @Override
    protected MasterReferenceDataRepository getRepository() {
        return repository;
    }

    public List<MasterReferenceData> getByOrgDataset(Integer organisation, String dataset ) {
        List<MasterReferenceData> list = repository.getByOrgDataset( organisation, dataset );
        return list;
    }

    public List<MasterReferenceData> getByOrgDatasetCode(Integer organisation, String dataset, String refCode ) {
        List<MasterReferenceData> list = repository.getByOrgDatasetCode( organisation, dataset, refCode );
        return list;
    }


    public List<Integer> getIdByName(Integer organisation, String dataset, String refCode ) {
        return repository.getIdByName( organisation, dataset, refCode );
    }

    public void deleteById(Integer organisation, Integer id) {
        repository.deleteById( organisation, id );
    }
}
