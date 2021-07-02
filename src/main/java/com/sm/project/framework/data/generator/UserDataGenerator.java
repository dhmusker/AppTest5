package com.sm.project.framework.data.generator;

import com.sm.project.framework.data.entity.User;
import com.sm.project.framework.data.entity.*;
import com.sm.project.framework.data.service.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringComponent
public class UserDataGenerator {


    @Bean
    public CommandLineRunner loadUserData(UserRepository userRepository, OrganisationRepository orgRepo) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.info("User Generator: Preparing User test data: " + userRepository.count() + " records found in the User table.");

            Integer testOrgId = 0;
            Integer adminUserId = 0;
            Integer test1UserId = 0;
            Integer test2UserId = 0;

            try {
                testOrgId = getTestOrgIDHelper( orgRepo, "Test Organisation", new Organisation( "Test Organisation", adminUserId, adminUserId, LocalDateTime.now()) );
                adminUserId = getUserIDHelper( userRepository, testOrgId,"system.admin@project.com", createTestUser(testOrgId,"System", "Admin", "system.admin@project.com", "System Administrator") );
                test1UserId = getUserIDHelper( userRepository, testOrgId, "test1@gmail.com", createTestUser(testOrgId,"Test", "User1", "test1@gmail.com", "Test Case User") );
                test2UserId = getUserIDHelper( userRepository, testOrgId, "test2@gmail.com", createTestUser(testOrgId,"Test", "User2", "test2@gmail.com", "Test Case User") );

            } catch (Exception e) {
                e.printStackTrace();
            }

            logger.debug("User Generator: System Admin User found: ID = " + adminUserId + " found in the User table.");
            logger.debug("User Generator: Test 1 User found: ID = " + test1UserId + " found in the User table.");
            logger.debug("User Generator: Test 2 User found: ID = " + test2UserId + " found in the User table.");

            logger.debug("User Generator: Generated test User data");
        };
    }

    // Find the user ID for a given user email address, and if not founf then create the user and return the id
    private Integer getUserIDHelper(UserRepository repo, Integer testOrgId, String userEmail, User user ) {
        List<Integer> userIdsForEmail = repo.getIdByEmail( testOrgId, userEmail );

        if ( userIdsForEmail == null || userIdsForEmail.size() == 0 ) {
            repo.save( user );
            repo.flush();
            userIdsForEmail = repo.getIdByEmail( testOrgId, userEmail );
        }
        return userIdsForEmail.get(0);
    }

    private User createTestUser( Integer testOrgId, String firstname, String lastname, String email, String title) {
        return new User( testOrgId, firstname, lastname, email, LocalDate.now(), title, true, 1, LocalDateTime.now());
    }

    // Find the user ID for a given user email address, and if not founf then create the user and return the id
    private Integer getTestOrgIDHelper(OrganisationRepository repo, String testOrgName, Organisation testOrg ) {

        List<Integer> testOrgId = repo.getIdByName( testOrgName );

        if ( testOrgId == null || testOrgId.size() == 0 ) {
            repo.save( testOrg );
            repo.flush();
            testOrgId = repo.getIdByName( testOrgName );

        }
        return testOrgId.get(0);
    }

    @Bean
    public CommandLineRunner loadRoleData(RoleRepository roleRepository, UserRepository userRepository, OrganisationRepository orgRepo) {
        return args -> {

            Logger logger = LoggerFactory.getLogger(getClass());
            logger.info("Role Generator: Preparing Role test data: " + roleRepository.count() + " records found in the Role table.");

            Integer testOrgId = 0;
            Integer adminUserId = 0;
            Integer adminRoleId = 0;
            Integer viewerRoleId = 0;
            Integer editorRoleId = 0;

            try {
                testOrgId = getTestOrgIDHelper( orgRepo, "Test Organisation", new Organisation( "Test Organisation", adminUserId, adminUserId, LocalDateTime.now()) );
                adminUserId = getUserIDHelper( userRepository, testOrgId, "system.admin@project.com", null );
                adminRoleId = getRoleIDHelper( roleRepository, testOrgId, "Admin", createTestRole( testOrgId,"Admin", "System Administrator", adminUserId ) );
                viewerRoleId = getRoleIDHelper( roleRepository, testOrgId, "Viewer", createTestRole( testOrgId,"Viewer", "View Only", adminUserId ) );
                editorRoleId = getRoleIDHelper( roleRepository, testOrgId, "Editor", createTestRole( testOrgId,"Editor", "Editors", adminUserId ) );
            } catch (Exception e) {
                e.printStackTrace();
            }

            logger.debug("Role Generator: Admin Role found: ID = " + adminRoleId + " found in the Role table.");
            logger.debug("Role Generator: Viewer Role found: ID = " + viewerRoleId + " found in the Role table.");
            logger.debug("Role Generator: Editor Role found: ID = " + editorRoleId + " found in the Role table.");

            logger.debug("Role Generator: Generated test Role data");
        };
    }

    // Find the user ID for a given user email address, and if not founf then create the user and return the id
    private Integer getRoleIDHelper(RoleRepository repo, Integer orgId, String roleName, Role role ) {
        List<Integer> roleIdsForName = repo.getIdByName( orgId, roleName );

        if ( roleIdsForName == null || roleIdsForName.size() == 0 ) {
            repo.save( role );
            repo.flush();
            roleIdsForName = repo.getIdByName( orgId, roleName );
        }
        return roleIdsForName.get(0);
    }

    private Role createTestRole( Integer orgId, String name, String purpose, Integer adminUser ) {
        return new Role(orgId, name, purpose, adminUser, LocalDateTime.now());
    }

    @Bean
    public CommandLineRunner loadPermissionData(PermissionRepository permissionRepo, UserRepository userRepo, OrganisationRepository orgRepo ) {
        return args -> {

            Logger logger = LoggerFactory.getLogger(getClass());
            logger.info("Permission Generator: Preparing Permission test data: " + permissionRepo.count() + " records found in the Permission table.");

            Integer testOrgId = 0;
            Integer adminUserId = 0;
            Integer pmPermissionId = 0;
            Integer riskPermissionId = 0;
            Integer auditPermissionId = 0;

            try {
                testOrgId = getTestOrgIDHelper( orgRepo, "Test Organisation", new Organisation( "Test Organisation", adminUserId, adminUserId, LocalDateTime.now()) );
                adminUserId = getUserIDHelper( userRepo, testOrgId, "system.admin@project.com", null );
                pmPermissionId = getPermissionIDHelper( permissionRepo, testOrgId, "Project Manager", createTestPermission( testOrgId, "Project Manager", "Management of Projects", adminUserId ) );
                riskPermissionId = getPermissionIDHelper( permissionRepo, testOrgId, "Risk Assessor", createTestPermission( testOrgId, "Risk Assessor", "Project Risk Assessment", adminUserId ) );
                auditPermissionId = getPermissionIDHelper( permissionRepo, testOrgId, "Auditor", createTestPermission( testOrgId, "Auditor", "Project Audit", adminUserId ) );
            } catch (Exception e) {
                e.printStackTrace();
            }

            logger.debug("Permission Generator: Project Manager permission found: ID = " + pmPermissionId + " found in the Permission table.");
            logger.debug("Permission Generator: Risk Permission found: ID = " + riskPermissionId + " found in the Permission table.");
            logger.debug("Permission Generator: Audit Permission found: ID = " + auditPermissionId + " found in the Permission table.");

            logger.debug("Permission Generator: Generated test Permission data");
        };
    }

    // Find the user ID for a given user email address, and if not founf then create the user and return the id
    private Integer getPermissionIDHelper(PermissionRepository repo, Integer orgId, String name, Permission entity ) {
        List<Integer> recordIdsForName = repo.getIdByName( orgId, name );

        if ( recordIdsForName == null || recordIdsForName.size() == 0 ) {
            repo.save( entity );
            repo.flush();
            recordIdsForName = repo.getIdByName( orgId, name );

        }
        return recordIdsForName.get(0);
    }

    private Permission createTestPermission( Integer orgId, String name, String purpose, Integer adminUser ) {
        return new Permission( orgId, name, purpose, adminUser, LocalDateTime.now() );
    }



    @Bean
    public CommandLineRunner loadUserPermissionData(UserPermissionRepository userPermissionRepo, UserRepository userRepo, PermissionRepository permissionRepo, OrganisationRepository orgRepo ) {
        return args -> {

            Logger logger = LoggerFactory.getLogger(getClass());
            logger.info("User Permission Generator: Preparing User Permission test data: " + userPermissionRepo.count() + " records found in the UserPermission table.");

            Integer testOrgId = 0;
            Integer adminUserId = 0;
            Integer test1UserId = 0;
            Integer test2UserId = 0;

            Integer pmPermissionId = 0;
            Integer riskPermissionId = 0;
            Integer auditPermissionId = 0;

            Integer testuser1_pm_PermissionId = 0;
            Integer testuser1_risk_PermissionId = 0;
            Integer testuser1_audit_PermissionId = 0;
            Integer testuser2_audit_PermissionId = 0;

            try {
                testOrgId = getTestOrgIDHelper( orgRepo, "Test Organisation", new Organisation( "Test Organisation", adminUserId, adminUserId, LocalDateTime.now()) );

                adminUserId = getUserIDHelper( userRepo, testOrgId, "system.admin@project.com", null );
                test1UserId = getUserIDHelper( userRepo, testOrgId, "test1@gmail.com", createTestUser( testOrgId,"Test", "User1", "test1@gmail.com", "Test Case User") );
                test2UserId = getUserIDHelper( userRepo, testOrgId, "test2@gmail.com", createTestUser( testOrgId,"Test", "User2", "test2@gmail.com", "Test Case User") );

                pmPermissionId = getPermissionIDHelper( permissionRepo, testOrgId, "Project Manager", createTestPermission( testOrgId,"Project Manager", "Management of Projects", adminUserId ) );
                riskPermissionId = getPermissionIDHelper( permissionRepo, testOrgId, "Risk Assessor", createTestPermission( testOrgId,"Risk Assessor", "Project Risk Assessment", adminUserId ) );
                auditPermissionId = getPermissionIDHelper( permissionRepo, testOrgId, "Auditor", createTestPermission( testOrgId,"Auditor", "Project Audit", adminUserId ) );

                testuser1_pm_PermissionId = getUserPermissionIDHelper( userPermissionRepo, testOrgId, test1UserId, pmPermissionId, createPMUserPermission(testOrgId, test1UserId, pmPermissionId, adminUserId ) );
                testuser1_risk_PermissionId = getUserPermissionIDHelper( userPermissionRepo, testOrgId, test1UserId, riskPermissionId, createRiskUserPermission( testOrgId, test1UserId, riskPermissionId, adminUserId ) );
                testuser1_audit_PermissionId = getUserPermissionIDHelper( userPermissionRepo, testOrgId, test1UserId, auditPermissionId, createAuditorUserPermission( testOrgId, test1UserId, auditPermissionId, adminUserId ) );
                testuser2_audit_PermissionId = getUserPermissionIDHelper( userPermissionRepo, testOrgId, test2UserId, pmPermissionId, createPMUserPermission( testOrgId, test2UserId, pmPermissionId, adminUserId ) );

            } catch (Exception e) {
                e.printStackTrace();
            }

            logger.debug("Permission Generator: Project Manager permission found: ID = " + pmPermissionId + " found in the Permission table.");
            logger.debug("Permission Generator: Risk Permission found: ID = " + riskPermissionId + " found in the Permission table.");
            logger.debug("Permission Generator: Audit Permission found: ID = " + auditPermissionId + " found in the Permission table.");

            logger.debug("Permission Generator: Generated test Permission data");
        };
    }

    // Find the user ID for a given user email address, and if not founf then create the user and return the id
    private Integer getUserPermissionIDHelper(UserPermissionRepository repo, Integer orgId, Integer userId, Integer permissionId, UserPermissionMembership entity ) {
        List<UserPermissionMembership> recordIdsForUser = repo.getUserPermission( orgId, userId, permissionId );

        if ( recordIdsForUser == null || recordIdsForUser.size() == 0 ) {
            repo.save( entity );
            repo.flush();
            recordIdsForUser = repo.getUserPermission( orgId, userId, permissionId );

        }
        return recordIdsForUser.get(0).getId();
    }

    private UserPermissionMembership createPMUserPermission( Integer orgId, Integer userId, Integer permissionId, Integer adminUser ) {
        return new UserPermissionMembership(orgId, userId, permissionId, adminUser, adminUser, LocalDateTime.now(), LocalDateTime.now().plusMonths(1L), adminUser, LocalDateTime.now() );
    }

    private UserPermissionMembership createRiskUserPermission( Integer orgId, Integer userId, Integer permissionId, Integer adminUser ) {
        return new UserPermissionMembership(orgId, userId, permissionId, adminUser, adminUser, LocalDateTime.now(), LocalDateTime.now().plusMonths(1L), adminUser, LocalDateTime.now() );
    }

    private UserPermissionMembership createAuditorUserPermission( Integer orgId, Integer userId, Integer permissionId, Integer adminUser ) {
        return new UserPermissionMembership(orgId, userId, permissionId, adminUser, adminUser, LocalDateTime.now(), LocalDateTime.now().plusMonths(1L), adminUser, LocalDateTime.now() );
    }


    @Bean
    public CommandLineRunner loadUserRoleData(UserRoleRepository userRoleRepo, UserRepository userRepo, RoleRepository roleRepo, OrganisationRepository orgRepo ) {
        return args -> {

            Logger logger = LoggerFactory.getLogger(getClass());
            logger.info("User Role Generator: Preparing User Role test data: " + userRoleRepo.count() + " records found in the UserRole table.");

            Integer testOrgId = 0;

            Integer adminUserId = 0;
            Integer test1UserId = 0;
            Integer test2UserId = 0;
            Integer test3UserId = 0;
            Integer test4UserId = 0;

            Integer adminRoleId = 0;
            Integer viewerRoleId = 0;
            Integer editorRoleId = 0;

            Integer testuser1_admin_RoleId = 0;
            Integer testuser2_viewer_RoleId = 0;
            Integer testuser3_editor_RoleId = 0;
            Integer testuser4_viewer_RoleId = 0;

            try {
                testOrgId = getTestOrgIDHelper( orgRepo, "Test Organisation", new Organisation( "Test Organisation", adminUserId, adminUserId, LocalDateTime.now()) );

                adminUserId = getUserIDHelper( userRepo, testOrgId, "system.admin@project.com", null );
                test1UserId = getUserIDHelper( userRepo, testOrgId, "test1@gmail.com", createTestUser(testOrgId, "Test", "User1", "test1@gmail.com", "Test Case User") );
                test2UserId = getUserIDHelper( userRepo, testOrgId, "test2@gmail.com", createTestUser(testOrgId, "Test", "User2", "test2@gmail.com", "Test Case User") );
                test3UserId = getUserIDHelper( userRepo, testOrgId, "test3@gmail.com", createTestUser(testOrgId, "Test", "User3", "test3@gmail.com", "Test Case User") );
                test4UserId = getUserIDHelper( userRepo, testOrgId, "test4@gmail.com", createTestUser(testOrgId, "Test", "User4", "test4@gmail.com", "Test Case User") );

                adminRoleId = getRoleIDHelper( roleRepo, testOrgId, "Admin", createTestRole( testOrgId, "Admin", "System Administrator", adminUserId ) );
                viewerRoleId = getRoleIDHelper( roleRepo, testOrgId, "Viewer", createTestRole( testOrgId, "Viewer", "View Only", adminUserId ) );
                editorRoleId = getRoleIDHelper( roleRepo, testOrgId, "Editor", createTestRole( testOrgId, "Editor", "Editors", adminUserId ) );

                testuser1_admin_RoleId = getUserRoleIDHelper( userRoleRepo, testOrgId, test1UserId, adminRoleId, createUserRole( testOrgId, test1UserId, adminRoleId, adminUserId ) );
                testuser2_viewer_RoleId = getUserRoleIDHelper( userRoleRepo, testOrgId, test2UserId, viewerRoleId, createUserRole( testOrgId, test2UserId, viewerRoleId, adminUserId ) );
                testuser3_editor_RoleId = getUserRoleIDHelper( userRoleRepo, testOrgId, test3UserId, editorRoleId, createUserRole( testOrgId, test3UserId, editorRoleId, adminUserId ) );
                testuser4_viewer_RoleId = getUserRoleIDHelper( userRoleRepo, testOrgId, test4UserId, viewerRoleId, createUserRole( testOrgId, test4UserId, viewerRoleId, adminUserId ) );

            } catch (Exception e) {
                e.printStackTrace();
            }

            logger.debug("User Role Generator: Admin role for Test User 1 found: ID = " + testuser1_admin_RoleId + " found in the User Role table.");
            logger.debug("User Role Generator: Viewer role for Test User 2 found: ID = " + testuser2_viewer_RoleId + " found in the User Role table.");
            logger.debug("User Role Generator: Editor role for Test User 3 found: ID = " + testuser3_editor_RoleId + " found in the User Role table.");
            logger.debug("User Role Generator: Viewer role for Test User 4 found: ID = " + testuser4_viewer_RoleId + " found in the User Role table.");

            logger.debug("User Role Generator: Generated test User Role data");
        };
    }

    // Find the user ID for a given user email address, and if not founf then create the user and return the id
    private Integer getUserRoleIDHelper(UserRoleRepository repo, Integer orgId, Integer userId, Integer roleId, UserRoleMembership entity ) {
        List<UserRoleMembership> recordIdsForUser = repo.getUserRole( orgId, userId, roleId );

        if ( recordIdsForUser == null || recordIdsForUser.size() == 0 ) {
            repo.save( entity );
            repo.flush();
            recordIdsForUser = repo.getUserRole( orgId, userId, roleId );

        }
        return recordIdsForUser.get(0).getId();
    }

    private UserRoleMembership createUserRole( Integer orgId, Integer userId, Integer roleId, Integer adminUser ) {
        return new UserRoleMembership(orgId, userId, roleId, adminUser, adminUser, LocalDateTime.now(), LocalDateTime.now().plusMonths(1L), adminUser, LocalDateTime.now() );
    }


}