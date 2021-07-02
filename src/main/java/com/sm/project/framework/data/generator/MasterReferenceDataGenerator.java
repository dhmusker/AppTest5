package com.sm.project.framework.data.generator;

import com.sm.project.framework.data.entity.*;
import com.sm.project.framework.data.service.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringComponent
public class MasterReferenceDataGenerator {


    @Bean
    public CommandLineRunner loadTestOrganisation(OrganisationRepository repository, UserRepository userRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.info("Organisation Generator: Preparing Test Organisation data: " + repository.count() + " records found in the Organisation table.");

            Integer testOrgId = 0;
            Integer adminUserId = 0;

            try {
                testOrgId = getTestOrgIDHelper( repository, "Test Organisation", new Organisation( "Test Organisation", adminUserId, adminUserId, LocalDateTime.now()) );
                adminUserId = getUserIDHelper( userRepository, testOrgId,"system.admin@project.com" );

            } catch (Exception e) {
                e.printStackTrace();
            }

            logger.info("Test Organisation found, record ID: " + testOrgId);
        };
    }

    @Bean
    public CommandLineRunner loadCategoryData(MasterReferenceDataRepository repository, UserRepository userRepository, OrganisationRepository orgRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.info("MasterReferenceData Generator: Preparing test data: " + repository.count() + " records found in the MasterReferenceData table.");

            Integer testOrgId = 0;
            Integer adminUserId = 0;
            List<MasterReferenceData> data;

            try {
                testOrgId = getTestOrgIDHelper( orgRepository, "Test Organisation", null );
                adminUserId = getUserIDHelper( userRepository, testOrgId, "system.admin@project.com" );

                data = repository.getByOrgDataset( testOrgId, "Category");

                LocalDate today = LocalDate.now();
                LocalDateTime now = LocalDateTime.now();

                if (data != null && data.size() >0 ) {
                    logger.info("Master Reference Data table contains data for Category and has: " + data.size() + " rows.");
                } else {
                    logger.info("Master Reference Data table does not contain any Category data, adding test categories now.");
                    data = Arrays.asList(
                            new MasterReferenceData( testOrgId,"Category","1","Test",true, today, null, adminUserId, now),
                            new MasterReferenceData( testOrgId,"Category","2","Skills",true, today, null, adminUserId, now),
                            new MasterReferenceData( testOrgId,"Category","3","Work",true, today, null, adminUserId, now),
                            new MasterReferenceData( testOrgId,"Category","4","Home",true, today, null, adminUserId, now)
                            );

                    repository.saveAll(data);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            logger.info("MasterReferenceData Generator: Category data now has: " + repository.count() + " records found in the MasterReferenceData table.");
        };
    }

    // Find the user ID for a given user email address, and if not founf then create the user and return the id
    private Integer getUserIDHelper(UserRepository repo, Integer orgId, String userEmail ) {
        List<Integer> userIdsForEmail = repo.getIdByEmail( orgId, userEmail );

        return userIdsForEmail.get(0);
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

}