package lv.team3.botcovidlab.entityManager;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Patient {
    @Id
    private Integer id;
    private String name;
    private String lastName;
    private String personalCode;
    private Integer temperature;
    private boolean isContactPerson;

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersonalCode() {
        return personalCode;
    }

    public void setPersonalCode(String personalCode) {
        this.personalCode = personalCode;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public boolean getIsContactPerson() {
        return isContactPerson;
    }

    public void setIsContactPerson(boolean contactPerson) {
        isContactPerson = contactPerson;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", personalCode='" + personalCode + '\'' +
                ", temperature=" + temperature +
                ", isContactPerson=" + isContactPerson +
                '}';
    }
}
