package com.testtask.phonecontacts.service;

import com.testtask.phonecontacts.model.ContactModel;
import com.testtask.phonecontacts.persistance.ContactRepository;
import com.testtask.phonecontacts.persistance.EmailRepository;
import com.testtask.phonecontacts.persistance.PhoneRepository;
import com.testtask.phonecontacts.persistance.UserRepository;
import com.testtask.phonecontacts.persistance.entity.Contact;
import com.testtask.phonecontacts.persistance.entity.User;
import com.testtask.phonecontacts.service.exceptions.EntityExistsException;
import com.testtask.phonecontacts.service.exceptions.EntityNotFoundException;
import com.testtask.phonecontacts.util.TestContactUtil;
import com.testtask.phonecontacts.util.TestUserUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ContactServiceTest {
    @MockBean
    private ContactRepository contactRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PhoneRepository phoneRepository;
    @MockBean
    private EmailRepository emailRepository;

    @Autowired
    private ContactService contactService;

    @Test
    void findAllContactsForUserTest() {
        String username = "test";
        when(contactRepository.findAllByUsername(username)).thenReturn(Collections.emptyList());

        assertEquals(Collections.emptyList(), contactService.findAllContactsForUser(username));
        verify(contactRepository, times(1)).findAllByUsername(username);
    }

    @Test
    void addNewContactForUserTest() {
        User user = TestUserUtil.createUser();
        ContactModel contactModel = TestContactUtil.createModel();
        Contact contact = TestContactUtil.createContact();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(contactRepository.existsByName(any())).thenReturn(false);
        when(contactRepository.save(any())).thenReturn(contact);

        assertEquals(contactService.addNewContactForUser(contactModel, user.getUsername()).getName(), ContactModel.fromContact(contact).getName());

        verify(userRepository, times(1)).findByUsername(any());
        verify(contactRepository, times(1)).existsByName(any());
        verify(contactRepository, times(1)).save(any());
    }

    @Test
    void addNewContactForUser_ExceptionCase() {
        User user = TestUserUtil.createUser();
        ContactModel contactModel = TestContactUtil.createModel();
        String testUsername = user.getUsername();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(contactRepository.existsByName(any())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> contactService.addNewContactForUser(contactModel, testUsername));

        verify(userRepository, times(1)).findByUsername(any());
        verify(contactRepository, times(1)).existsByName(any());
    }

    @Test
    void editContactForUserTest() {
        Contact contact = TestContactUtil.createContact();
        ContactModel model = TestContactUtil.createModel();
        when(contactRepository.findByUsernameAndName(any(), any())).thenReturn(Optional.of(contact));
        doNothing().when(phoneRepository).removeAllByContact(any());
        doNothing().when(emailRepository).removeAllByContact(any());

        assertNotNull(contactService.editContactForUser(model, contact.getName()));

        verify(phoneRepository,times(1)).removeAllByContact(any());
        verify(emailRepository,times(1)).removeAllByContact(any());
    }

    @Test
    void editContactForUserTest_ExceptionCase() {
        when(contactRepository.findByUsernameAndName(any(), any())).thenReturn(Optional.empty());
        ContactModel contactModel = TestContactUtil.createModel();

        assertThrows(EntityNotFoundException.class, () -> contactService.editContactForUser(contactModel, "Test"));
        verify(contactRepository, times(1)).findByUsernameAndName(any(), any());
    }

    @Test
    void deleteContactTest_ExceptionCase() {
        when(contactRepository.findByUsernameAndName(any(), any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> contactService.deleteContactForUser("test", "test"));
        verify(contactRepository, times(1)).findByUsernameAndName(any(), any());
    }
}