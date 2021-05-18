package it.paspaola.quarkus.dto;

public class Sim {

    private String id;
    private String carrier;
    private String puk;
    private String phoneNumber;

    public Sim() {

    }

    public Sim(String id, String carrier, String puk, String phoneNumber) {
        this();
        this.id = id;
        this.carrier = carrier;
        this.puk = puk;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getPuk() {
        return puk;
    }

    public void setPuk(String puk) {
        this.puk = puk;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
