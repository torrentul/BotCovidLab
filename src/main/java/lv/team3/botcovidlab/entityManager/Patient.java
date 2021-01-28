package lv.team3.botcovidlab.entityManager;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Patient {
    @Id
    private Integer id;
    private Long chatId;
    private String name;
    private String lastName;
    private String personalCode;
    private Double temperature;
    private boolean isContactPerson;
    private boolean hasCough;
    private boolean hasTroubleBreathing;
    private boolean hasHeadache;
    private String phoneNumber;

    public Patient() {

    }
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Long getChatId() {
        return chatId;
    }
    public void setChatId(Long id) {
        this.chatId = id;
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

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public boolean isContactPerson() {
        return isContactPerson;
    }

    public void setContactPerson(boolean contactPerson) {
        isContactPerson = contactPerson;
    }

    public boolean isHasCough() {
        return hasCough;
    }

    public void setHasCough(boolean hasCough) {
        this.hasCough = hasCough;
    }

    public boolean isHasTroubleBreathing() {
        return hasTroubleBreathing;
    }

    public void setHasTroubleBreathing(boolean hasTroubleBreathing) {
        this.hasTroubleBreathing = hasTroubleBreathing;
    }

    public boolean isHasHeadache() {
        return hasHeadache;
    }

    public void setHasHeadache(boolean hasHeadache) {
        this.hasHeadache = hasHeadache;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "chatId=" + chatId +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", personalCode='" + personalCode + '\'' +
                ", temperature=" + temperature +
                ", isContactPerson=" + isContactPerson +
                ", hasCough=" + hasCough +
                ", hasTroubleBreathing=" + hasTroubleBreathing +
                ", hasHeadache=" + hasHeadache +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
