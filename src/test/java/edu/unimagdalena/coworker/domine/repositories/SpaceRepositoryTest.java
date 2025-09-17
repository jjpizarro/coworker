package edu.unimagdalena.coworker.domine.repositories;

import edu.unimagdalena.coworker.domine.entities.Space;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@Testcontainers
class SpaceRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    SpaceRepository spaceRepository;
    private Space space;
    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Dado los datos de un space guardarlo en la db")
    void save_space() {
        //Arrange
        space = Space.builder().name("HQ Centro").address("Cra4#21-21").build();
        // Act
        spaceRepository.save(space);
        // Assert
        assertThat(space.getId()).isNotNull();
    }
}