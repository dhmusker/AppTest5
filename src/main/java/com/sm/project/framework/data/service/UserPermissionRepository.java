package com.sm.project.framework.data.service;

import com.sm.project.framework.data.entity.UserPermissionMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserPermissionRepository extends JpaRepository<UserPermissionMembership, Integer> {


    @Query("SELECT u FROM UserPermissionMembership u WHERE u.organisation = ?1 and u.userId = ?2")
    public List<UserPermissionMembership> getByUserId(Integer organisation, Integer id );

    @Query("SELECT u FROM UserPermissionMembership u WHERE u.organisation = ?1 and u.permissionId = ?2")
    public List<UserPermissionMembership> getByPermission( Integer organisation, Integer id );

    @Query("SELECT u FROM UserPermissionMembership u WHERE u.organisation = ?1 and u.userId = ?2 and u.permissionId = ?3")
    public List<UserPermissionMembership> getUserPermission( Integer organisation, Integer userId, Integer permissionId );

}
