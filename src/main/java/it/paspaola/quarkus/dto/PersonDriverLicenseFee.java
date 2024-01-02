package it.paspaola.quarkus.dto;

public class PersonDriverLicenseFee {

    private Person person;
    private DriverLicense driverLicense;
    private FeeBySsn feeBySsn;


    public PersonDriverLicenseFee() {

    }

    public PersonDriverLicenseFee(Person person, DriverLicense driverLicense, FeeBySsn feeBySsn) {
        this.person = person;
        this.driverLicense = driverLicense;
        this.feeBySsn = feeBySsn;
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

    public FeeBySsn getFeeBySsn() {
        return feeBySsn;
    }

    public void setFeeBySsn(FeeBySsn feeBySsn) {
        this.feeBySsn = feeBySsn;
    }

    @Override
    public String toString() {
        return "PersonDriverLicenseFee{" +
                "person=" + person +
                ", driverLicense=" + driverLicense +
                ", feeBySsn=" + feeBySsn +
                '}';
    }
}
