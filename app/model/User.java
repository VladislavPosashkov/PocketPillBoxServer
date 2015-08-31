package model;


import javax.persistence.*;
import java.util.List;

@Entity(name = "pillbox_user")
@SequenceGenerator(name = "user_value_generator", sequenceName = "pillbox_user_user_id_seq", allocationSize = 1, initialValue = 1)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_value_generator")
    @Column(name = "user_id")
    private Integer userId;


    @Column(name="user_key", unique = true)
    private String facebookKey;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @OneToMany (mappedBy = "owner")
    private List<Medication> medications;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFacebookKey() {
        return facebookKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFacebookKey(String facebookKey) {
        this.facebookKey = facebookKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }
}
