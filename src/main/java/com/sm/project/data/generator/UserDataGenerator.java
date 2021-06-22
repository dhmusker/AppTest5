package com.sm.project.data.generator;

import com.sm.project.data.entity.User;
import com.sm.project.data.service.UserRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SpringComponent
public class UserDataGenerator {


    @Bean
    public CommandLineRunner loadUserData(UserRepository userRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (userRepository.count() != 0L) {
                logger.info("Using existing database: " + userRepository.count() + " records found in the User table.");
                return;
            }
            int seed = 123;
            logger.info("Generating demo data, as there are 0 records in the User table.");
            logger.info("... generating 100 User entities...");
            userRepository.saveAll( (Iterable<User>)createExampleUsers(100) );
            logger.info("Generated demo data");
        };
    }

    private static Iterable<User> createExampleUsers(int count) {
        ExampleDataGenerator<User> generator = new ExampleDataGenerator<>(
                User.class, LocalDateTime.of(2021, 6, 7, 0, 0, 0));

        generator.setData(User::setFirstName, DataType.FIRST_NAME);
        generator.setData(User::setId, DataType.ID);
        generator.setData(User::setFirstName, DataType.FIRST_NAME);
        generator.setData(User::setLastName, DataType.LAST_NAME);
        generator.setData(User::setEmail, DataType.EMAIL);
        generator.setData(User::setDateEnrolled, DataType.DATE_LAST_7_DAYS);
        generator.setData(User::setTitle, DataType.OCCUPATION);
        generator.setData(User::setUserActive, DataType.BOOLEAN_10_90);
        return generator.create(count, 123);
    }

}