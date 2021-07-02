package com.sm.project.framework.data.service;

import com.sm.project.framework.data.entity.MasterReferenceData;
import com.sm.project.framework.data.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface MasterReferenceDataRepository extends JpaRepository<MasterReferenceData, Integer> {

    @Query("SELECT m FROM MasterReferenceData m WHERE m.organisation = ?1 and m.dataset = ?2")
    List<MasterReferenceData> getByOrgDataset(Integer organisation, String dataset );

    @Query("SELECT m FROM MasterReferenceData m WHERE m.organisation = ?1 and m.dataset = ?2 and m.refCode = ?3")
    List<MasterReferenceData> getByOrgDatasetCode(Integer organisation, String dataset, String refCode );

    @Query("SELECT m.id FROM MasterReferenceData m WHERE m.organisation = ?1 and m.dataset = ?2 and m.refCode = ?1")
    List<Integer> getIdByName( Integer organisation, String dataset, String refCode );

    @Modifying
    @Transactional
    @Query("DELETE FROM MasterReferenceData m WHERE m.organisation = ?1 and m.id = ?2")
    public void deleteById( Integer organisation, Integer id );
}
