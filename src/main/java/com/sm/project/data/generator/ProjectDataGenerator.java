package com.sm.project.data.generator;

import com.sm.project.data.entity.Project;
import com.sm.project.data.entity.ProjectTask;
import com.sm.project.data.service.ProjectRepository;
import com.sm.project.data.service.ProjectService;
import com.sm.project.data.service.ProjectTaskRepository;
import com.sm.project.data.service.ProjectTaskService;
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
    public CommandLineRunner loadProjectData(ProjectService service) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (service.getTestCount() != 0L) {
                logger.info("Using existing database: " + service.getTestCount() + " records of category 'Test' found in the Project table.");
//                return;
            }

            logger.info("Generating test data, cleaning the existing Project table." );

            service.deleteAllTestRecords();

            logger.info("... generating Project entities...");

            service.saveAll(getProjects());

            logger.info("Generated test data, there are now " + service.getTestCount() + " records of category 'Test' found in the Project table." );

        };
    }

    private List<Project> getProjects() {

        LocalDate date = LocalDate.of(2021, 6, 7);

        return Arrays.asList(
                new Project(1,"Build starter web site", "Dermot Musker", "Need to create a Project System to manage building of the Service Management web sites", date, date, false,"Test"),
                new Project(2,"Create project for Business Analysis", "Dermot Musker", "", null, null, false,"Test"),
                new Project(3,"Create project for Service Strategy", "Dermot Musker", "", null, null, false,"Test"),
                new Project(4,"Create project for Information Security Management", "Dermot Musker", "", null, null, false,"Test"),
                new Project(5,"Create project for Configuration Management", "Dermot Musker", "", null, null, false,"Test"),
                new Project(6,"Create project for Incident Management", "Dermot Musker", "", null, null, false,"Test"),
                new Project(7,"Create project for Problem Management", "Dermot Musker", "", null, null, false,"Test"),
                new Project(8,"Create project for Change Management", "Dermot Musker", "", null, null, false,"Test"),
                new Project(9,"Create project for Release Management", "Dermot Musker", "", null, null, false,"Test"));
    }

    @Bean
    public CommandLineRunner loadProjectTaskData(ProjectTaskService service, ProjectService projectService) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (service.getTestCount() != 0L) {
                logger.info("Using existing database: " + service.getTestCount() + " records of category 'Test' found in the Project Task table.");
//                return;
            }

            logger.info("Generating test data, cleaning the existing Project Task table.");

            service.deleteAllTestRecords();

            logger.info("... generating Project Task entities...");

            List<Project> testlist = projectService.getByTitle("Build starter web site");

            if ( testlist.size() != 0L ) {
                Project p = testlist.get(0);
                service.saveAll(getProjectTasks(p.getId()));
            } else {
                logger.error("Generated test Project with title 'Build starter web site' not found, couldnt load the project tasks" );
            }


            logger.info("Generated test data, there are now " + service.getTestCount() + " records of category 'Test' found in the Project Task table." );

        };
    }

    private List<ProjectTask> getProjectTasks(Integer projectID) {

        LocalDate date = LocalDate.of(2021, 6, 7);
        LocalDateTime datetime = LocalDateTime.of(2021, 6, 7, 0, 0);

        return Arrays.asList(

                /**
                new ProjectTask(1, projectID, "Create HTML Home page", "Create HTML Home page","Dermot Musker", "Not Started", "Test" ),
                new ProjectTask(2, projectID, "Add public Login Page", "Add public Login Page","Dermot Musker", "Not Started", "Test" ) );

                 */
                new ProjectTask(1, projectID, "Create HTML Home page", "Create HTML Home page","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test" ),
                new ProjectTask(2, projectID, "Add public Login Page", "Add public Login Page","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test" ),
                new ProjectTask(3, projectID, "Add public Logout Page", "Add public Logout Page","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test"),
                new ProjectTask(4, projectID, "Research how to separate public from private routes", "Research how to separate public from private routes","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test" ),
                new ProjectTask(5, projectID, "Separate public from private routes", "Separate public from private routes","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test"),
                new ProjectTask(6, projectID, "Separate public from private static resources", "Separate public from private static resources","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test" ),
                new ProjectTask(7, projectID, "Add Security for a generic role", "Add Security for a generic role","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test" ),
                new ProjectTask(8, projectID, "Add testing for public and private routes", "Add testing for public and private routes","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test" ),
                new ProjectTask(9, projectID, "Add Project Task data", "Add Project Task data","Dermot Musker", "Not Started", date, date,
                        datetime, datetime, 0.0, 1, 0.0, 0.0, "Test" ) );
    }

}
