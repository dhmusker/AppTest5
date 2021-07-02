package com.sm.project.framework.data.service;

import com.sm.project.framework.data.entity.UserPermissionMembership;
import com.sm.project.framework.data.entity.UserRoleMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleMembership, Integer> {

    @Query("SELECT u FROM UserRoleMembership u WHERE u.organisation = ?1 and u.userId = ?2")
    public List<UserRoleMembership> getByUserId(Integer organisation, Integer id );

    @Query("SELECT u FROM UserRoleMembership u WHERE u.organisation = ?1 and u.roleId = ?2")
    public List<UserRoleMembership> getByRole( Integer organisation, Integer id );

    @Query("SELECT u FROM UserRoleMembership u WHERE u.organisation = ?1 and u.userId = ?2 and u.roleId = ?3")
    public List<UserRoleMembership> getUserRole( Integer organisation, Integer userId, Integer roleId );

}
