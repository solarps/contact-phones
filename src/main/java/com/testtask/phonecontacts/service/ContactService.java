package com.testtask.phonecontacts.service;

import com.testtask.phonecontacts.model.ContactModel;
import com.testtask.phonecontacts.persistance.ContactRepository;
import com.testtask.phonecontacts.persistance.UserRepository;
import com.testtask.phonecontacts.persistance.entity.Contact;
import com.testtask.phonecontacts.persistance.entity.User;
import com.testtask.phonecontacts.service.exceptions.EntityExistsException;
import com.testtask.phonecontacts.service.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Collection<ContactModel> findAllContactsForUser(String username) {
        Collection<Contact> contacts = contactRepository.findAllByUsername(username);
        return contacts.stream()
                .map(ContactModel::fromContact)
                .toList();
    }

    @Transactional
    public ContactModel addNewContactForUser(ContactModel contactModel, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        if (contactRepository.findByUsernameAndName(username, contactModel.getName()).isPresent()) {
            throw new EntityExistsException("Contact already exists");
        }
        Contact contact = createContact(user, contactModel);
        return ContactModel.fromContact(contactRepository.save(contact));
    }

    @Transactional
    public ContactModel editContactForUser(ContactModel contactModel, String username) {
        Contact contact = contactRepository.findByUsernameAndName(username, contactModel.getName())
                .orElseThrow(() -> new EntityNotFoundException("Contact not found"));

        contact.setPhones(contactModel.getPhones());
        contact.setEmails(contactModel.getEmails());

        return ContactModel.fromContact(contact);
    }

    @Transactional
    public void deleteContactForUser(String name, String username) {
        Contact contact = contactRepository.findByUsernameAndName(username, name)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found"));
        contactRepository.delete(contact);
    }

    public void addAllContactsForUser(Collection<ContactModel> contactModels, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        Collection<Contact> contactsToSave = new ArrayList<>();
        for (ContactModel contactModel : contactModels) {
            Contact contact = createContact(user, contactModel);
            if (contactRepository.findByUsernameAndName(username, contactModel.getName()).isEmpty()) {
                contactsToSave.add(contact);
            }
        }
        contactRepository.saveAll(contactsToSave);
    }

    private Contact createContact(User user, ContactModel contactModel) {
        return new Contact(contactModel.getName(), user, contactModel.getEmails(), contactModel.getPhones());
    }
}
