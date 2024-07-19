package org.apiservice.model;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "Users")
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nonnull
    @Column(name = "fullname")
    private String fullName;

    @Nonnull
    @Column(name = "email", unique = true)
    private String email;

    @OneToMany(mappedBy = "groupOwner")
    private List<Group> ownGroups;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="users_groups",
            joinColumns=  @JoinColumn(name="user_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="group_id", referencedColumnName="id") )
    private Set<Group> groups = new HashSet<>();




}
