//package lv.team3.botcovidlab.entityManager;
//
//import javax.persistence.Entity;
//import javax.persistence.Id;
//
//@Entity
//public class Patient {
//    @Id
//    private Integer id;
//    private String name;
//    private String lastName;
//    private String personalCode;
//    private Double temperature;
//    private boolean isContactPerson;
//    private String phoneNumber;
//    public Patient() {
//
//    }
//    public Patient(String name, String lastName, String personalCode, Double temperature, boolean isContactPerson, String phoneNumber) {
//        this.name = name;
//        this.lastName = lastName;
//        this.personalCode = personalCode;
//        this.temperature = temperature;
//        this.isContactPerson = isContactPerson;
//        this.phoneNumber = phoneNumber;
//    }
//
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    @Id
//    public Integer getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getPersonalCode() {
//        return personalCode;
//    }
//
//    public void setPersonalCode(String personalCode) {
//        this.personalCode = personalCode;
//    }
//
//    public Double getTemperature() {
//        return temperature;
//    }
//
//    public void setTemperature(Double temperature) {
//        this.temperature = temperature;
//    }
//
//    public boolean getIsContactPerson() {
//        return isContactPerson;
//    }
//
//    public void setIsContactPerson(boolean contactPerson) {
//        isContactPerson = contactPerson;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    @Override
//    public String toString() {
//        return "Patient{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", personalCode='" + personalCode + '\'' +
//                ", temperature=" + temperature +
//                ", isContactPerson=" + isContactPerson +
//                ", phoneNumber='" + phoneNumber + '\'' +
//                '}';
//    }
//}
