package org.apiservice.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "Groups")
@NoArgsConstructor
public class Group {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User groupOwner;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="users_groups",
            joinColumns=  @JoinColumn(name="group_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="user_id", referencedColumnName="id") )
    private Set<User> users = new HashSet<>();
}
