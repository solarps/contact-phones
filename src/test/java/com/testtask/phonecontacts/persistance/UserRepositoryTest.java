package com.testtask.phonecontacts.persistance;

import com.testtask.phonecontacts.persistance.entity.Role;
import com.testtask.phonecontacts.persistance.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource("classpath:application-test.yaml")
@Sql(value = "classpath:init/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
     void testFindByUsername() {
        User user = new User("testuser", "password", Role.USER);
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> foundUser = userRepository.findByUsername("testuser");

        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
     void testExistsByUsername() {
        User user = new User("testuser", "password",Role.USER);
        entityManager.persist(user);
        entityManager.flush();

        boolean exists = userRepository.existsByUsername("testuser");

        assertTrue(exists);
    }

    @Test
     void testExistsByUsername_NonExistentUser() {
        boolean exists = userRepository.existsByUsername("nonexistent");

        assertFalse(exists);
    }
}