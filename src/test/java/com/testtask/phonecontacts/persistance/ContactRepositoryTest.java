package com.testtask.phonecontacts.persistance;

import com.testtask.phonecontacts.persistance.entity.Contact;
import com.testtask.phonecontacts.persistance.entity.Role;
import com.testtask.phonecontacts.persistance.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource("classpath:application-test.yaml")
@Sql(value = "classpath:init/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ContactRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ContactRepository contactRepository;

    @Test
    void testFindAllByUsername() {
        User user = new User("testuser", "testpassword", Role.USER);
        Contact contact = new Contact("test", user, Collections.emptySet(), Collections.emptySet());
        entityManager.persist(user);
        entityManager.persist(contact);
        entityManager.flush();
        Collection<Contact> allByUsername = contactRepository.findAllByUsername("testuser");

        assertNotNull(allByUsername);
    }

    @Test
    void testExistsByName() {
        User user = new User("testuser", "testpassword", Role.USER);
        Contact contact = new Contact("test", user, Collections.emptySet(), Collections.emptySet());
        entityManager.persist(user);
        entityManager.persist(contact);
        entityManager.flush();

        boolean foundContact = contactRepository.existsByName("test");
        assertTrue(foundContact);
    }

    @Test
    void testExistsByName_UsernameNotFound() {
        boolean foundContact = contactRepository.existsByName("testuser");

        assertFalse(foundContact);
    }

    @Test
    void testFindByUsernameAndName(){
        User user = new User("testuser", "testpassword", Role.USER);
        Contact contact = new Contact("test", user, Collections.emptySet(), Collections.emptySet());
        entityManager.persist(user);
        entityManager.persist(contact);
        entityManager.flush();

        Optional<Contact> foundContact = contactRepository.findByUsernameAndName("testuser","test");

        assertTrue(foundContact.isPresent());
        assertEquals("test", foundContact.get().getName());
    }

    @Test
    void testFindByUsernameAndName_NotFoundCase(){
        Optional<Contact> foundContact = contactRepository.findByUsernameAndName("testuser","test");

        assertFalse(foundContact.isPresent());
    }
}