package com.nix.model;

import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Date;


@Data
@EqualsAndHashCode(exclude = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "PERSON", schema = "PUBLIC")
@NamedEntityGraph(name = "user.role",
        attributeNodes = @NamedAttributeNode("role"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERSON_ID")
    private long id;

    @Column(name = "LOGIN", unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9_-]{2,10}$", message = "{user.login.pattern}")
    @NaturalId
    @NotEmpty
    private String login;

    @Column(name = "PASSWORD")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{5,15}$", message = "{user.password.pattern}")
    @NotEmpty
    private String password;

    @Column(name = "EMAIL")
    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "{user.email.pattern}")
    @NotEmpty
    private String email;

    @Column(name = "FIRST_NAME")
    @Length(min = 2, max = 25, message = "{user.firstName.length}")
    @NotEmpty
    private String firstName;

    @Column(name = "LAST_NAME")
    @Length(min = 2, max = 25, message = "{user.lastName.length}")
    @NotEmpty
    private String lastName;

    @Column(name = "BIRTHDAY")
    @Temporal(TemporalType.DATE)
    @Past(message = "{user.birthday.past}")
    @NotNull(message = "{user.birthday.empty}")
    private Date birthday;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    @NotNull(message = "{user.role.not.found}")
    private Role role;

}
