package com.testtask.phonecontacts.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonManagedReference
    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<Email> emails;

    @JsonManagedReference
    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<Phone> phones;

    public Contact(String name, User user, Set<Email> emails, Set<Phone> phones) {
        this.name = name;
        this.user = user;
        this.emails = emails;
        this.phones = phones;
    }
}
