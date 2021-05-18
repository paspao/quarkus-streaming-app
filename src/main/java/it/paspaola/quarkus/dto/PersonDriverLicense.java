package it.paspaola.quarkus.dto;

public class PersonDriverLicense {
    private Person person;
    private DriverLicense driverLicense;

    public PersonDriverLicense(Person person, DriverLicense driverLicense) {
        this();
        this.person = person;
        this.driverLicense = driverLicense;
    }


    public PersonDriverLicense() {

    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public DriverLicense getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(DriverLicense driverLicense) {
        this.driverLicense = driverLicense;
    }
}
