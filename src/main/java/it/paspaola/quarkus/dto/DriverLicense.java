package it.paspaola.quarkus.dto;

public class DriverLicense {

    private String id;
    private String type;
    private String licenseNumber;

    public DriverLicense() {

    }

    public DriverLicense(String id, String type, String licenseNumber) {
        this();
        this.id = id;
        this.type = type;
        this.licenseNumber = licenseNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
}
