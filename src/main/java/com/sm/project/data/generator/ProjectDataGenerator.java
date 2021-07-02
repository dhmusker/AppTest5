package com.sm.project.data.generator;

import com.sm.project.data.entity.Project;
import com.sm.project.data.entity.ProjectTask;
import com.sm.project.data.service.ProjectRepository;
import com.sm.project.data.service.ProjectService;
import com.sm.project.data.service.ProjectTaskRepository;
import com.sm.project.data.service.ProjectTaskService;
import com.sm.project.framework.data.entity.Organisation;
import com.sm.project.framework.data.service.OrganisationRepository;
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
public class ProjectDataGenerator {

    @Bean
    public CommandLineRunner loadProjectData(OrganisationRepository repository, ProjectService service) {
        return args -> {

            Logger logger = LoggerFactory.getLogger(getClass());
            Integer testOrgId = 0;
            Integer adminUserId = 0;

            if (service.getTestCount( 6299 ) != 0L) {
                logger.info("Using existing database: " + service.getTestCount(6299) + " records of category 'Test' found in the Project table.");
//                return;
            }

            logger.info("Generating test data, cleaning the existing Project table." );

            testOrgId = getTestOrgIDHelper( repository, "Test Organisation", new Organisation( "Test Organisation", adminUserId, adminUserId, LocalDateTime.now()) );
            service.deleteAllTestRecords(6299);

            logger.info("... generating Project entities...");

            service.saveAll(getProjects(testOrgId));

            logger.info("Generated test data, there are now " + service.getTestCount(6299) + " records of category 'Test' found in the Project table." );

        };
    }

    private List<Project> getProjects(Integer testOrgId) {

        LocalDate date = LocalDate.of(2021, 6, 7);

        return Arrays.asList(
                new Project(1,testOrgId,"Build starter web site", 2547, "Dermot Musker", "Need to create a Project System to manage building of the Service Management web sites", date, date, false,"1","Test"),
                new Project(2,testOrgId,"Create project for Business Analysis", 2547, "Dermot Musker", "", null, null, false,"1","Test"),
                new Project(3,testOrgId,"Create project for Service Strategy", 2547, "Dermot Musker", "", null, null, false,"1","Test"),
                new Project(4,testOrgId,"Create project for Information Security Management", 2547, "Dermot Musker", "", null, null, false,"1","Test"),
                new Project(5,testOrgId,"Create project for Configuration Management", 2547, "Dermot Musker", "", null, null, false,"1","Test"),
                new Project(6,testOrgId,"Create project for Incident Management", 2547, "Dermot Musker", "", null, null, false,"1","Test"),
                new Project(7,testOrgId,"Create project for Problem Management", 2547, "Dermot Musker", "", null, null, false,"1","Test"),
                new Project(8,testOrgId,"Create project for Change Management", 2547, "Dermot Musker", "", null, null, false,"1","Test"),
                new Project(9,testOrgId,"Create project for Release Management", 2547, "Dermot Musker", "", null, null, false,"1","Test"));
    }

    @Bean
    public CommandLineRunner loadProjectTaskData(OrganisationRepository repository, ProjectTaskService service, ProjectService projectService) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            Integer testOrgId = 0;
            Integer adminUserId = 0;

            if (service.getTestCount() != 0L) {
                logger.info("Using existing database: " + service.getTestCount() + " records of category 'Test' found in the Project Task table.");
//                return;
            }

            logger.info("Generating test data, cleaning the existing Project Task table.");

            testOrgId = getTestOrgIDHelper( repository, "Test Organisation", new Organisation( "Test Organisation", adminUserId, adminUserId, LocalDateTime.now()) );
            service.deleteAllTestRecords();

            logger.info("... generating Project Task entities...");

            List<Project> testlist = projectService.getByTitle(6299, "Build starter web site");

            if ( testlist.size() != 0L ) {
                Project p = testlist.get(0);
                service.saveAll(getProjectTasks(testOrgId, p.getId()));
            } else {
                logger.error("Generated test Project with title 'Build starter web site' not found, couldnt load the project tasks" );
            }


            logger.info("Generated test data, there are now " + service.getTestCount() + " records of category 'Test' found in the Project Task table." );

        };
    }

    private List<ProjectTask> getProjectTasks(Integer testOrgId, Integer projectID) {

        LocalDate date = LocalDate.of(2021, 6, 7);
        LocalDateTime datetime = LocalDateTime.of(2021, 6, 7, 0, 0);

        return Arrays.asList(

                /**
                new ProjectTask(1, projectID, "Create HTML Home page", "Create HTML Home page","Dermot Musker", "Not Started", "Test" ),
                new ProjectTask(2, projectID, "Add public Login Page", "Add public Login Page","Dermot Musker", "Not Started", "Test" ) );

                 */
                new ProjectTask(1, testOrgId, projectID, "Create HTML Home page", "Create HTML Home page","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test" ),
                new ProjectTask(2, testOrgId, projectID, "Add public Login Page", "Add public Login Page","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test" ),
                new ProjectTask(3, testOrgId, projectID, "Add public Logout Page", "Add public Logout Page","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test"),
                new ProjectTask(4, testOrgId, projectID, "Research how to separate public from private routes", "Research how to separate public from private routes","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test" ),
                new ProjectTask(5, testOrgId, projectID, "Separate public from private routes", "Separate public from private routes","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test"),
                new ProjectTask(6, testOrgId, projectID, "Separate public from private static resources", "Separate public from private static resources","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test" ),
                new ProjectTask(7, testOrgId, projectID, "Add Security for a generic role", "Add Security for a generic role","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test" ),
                new ProjectTask(8, testOrgId, projectID, "Add testing for public and private routes", "Add testing for public and private routes","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test" ),
                new ProjectTask(9, testOrgId, projectID, "Add Project Task data", "Add Project Task data","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test" ) );
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
