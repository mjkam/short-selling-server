package com.example.demo;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.MountableFile;

@SpringBootTest
public class AbstractIntegrationTest extends BaseTest {
    static MySQLContainer<?> mySQLContainer =
            new MySQLContainer<>("mysql:5.7.33")
                    .withCopyFileToContainer(MountableFile.forHostPath("./sql/scheme.sql"),
                            "/docker-entrypoint-initdb.d/")
                    .withDatabaseName("short_selling")
                    .withExposedPorts(3306)
                    .withReuse(true);

    @DynamicPropertySource
    public static void setupTestContainer(DynamicPropertyRegistry registry) {
        Startables.deepStart(mySQLContainer).join();


        System.out.println(mySQLContainer.getJdbcUrl());
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }
}
