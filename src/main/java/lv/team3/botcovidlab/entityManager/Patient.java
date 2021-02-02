package lv.team3.botcovidlab.entityManager;


import java.util.Objects;

/**
 * Stores data about patients, who interact with chatbots and
 * wish to apply for covid tests
 */
public class Patient {
    private Long chatId;
    private String name;
    private String lastName;
    private String personalCode;
    private String temperature;
    private boolean isContactPerson;
    private boolean hasCough;
    private boolean hasTroubleBreathing;
    private boolean hasHeadache;
    private String phoneNumber;

    public Patient() {

    }

    public Patient(Long chatId, String name, String lastName,
                   String personalCode, String temperature, boolean isContactPerson,
                   boolean hasCough, boolean hasTroubleBreathing, boolean hasHeadache, String phoneNumber) {
        this.chatId = chatId;
        this.name = name;
        this.lastName = lastName;
        this.personalCode = personalCode;
        this.temperature = temperature;
        this.isContactPerson = isContactPerson;
        this.hasCough = hasCough;
        this.hasTroubleBreathing = hasTroubleBreathing;
        this.hasHeadache = hasHeadache;
        this.phoneNumber = phoneNumber;
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

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return isContactPerson == patient.isContactPerson && hasCough == patient.hasCough
                && hasTroubleBreathing == patient.hasTroubleBreathing && hasHeadache == patient.hasHeadache
                && chatId.equals(patient.chatId) && name.equals(patient.name) && lastName.equals(patient.lastName)
                && personalCode.equals(patient.personalCode) && temperature.equals(patient.temperature)
                &&phoneNumber.equals(patient.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, name, lastName,
                personalCode, temperature, isContactPerson,
                hasCough, hasTroubleBreathing, hasHeadache, phoneNumber);
    }
}
