package org.apiservice.model;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;
import lombok.*;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@ToString
@Table(name = "Users")
@Builder
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
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @OneToMany(mappedBy = "groupOwner")
    private Set<Group> ownGroups;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="users_groups",
            joinColumns=  @JoinColumn(name="user_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="group_id", referencedColumnName="id") )
    private Set<Group> groups;

    public User(@Nonnull String fullName, @Nonnull String email) {
        this.fullName = fullName;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(fullName, user.fullName) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, email);
    }


}
