package model;


import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.persistence.*;

@Entity(name = "pillbox_user")
@SequenceGenerator(name = "user_value_generator", sequenceName = "pillbox_user_user_id_seq", allocationSize = 1, initialValue = 1)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_value_generator")
    @Column(name = "user_id")
    private int userId;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
