package it.paspaola.quarkus.dto;

public class Fee {

    private String id;
    private String scn;
    private String name;
    private float value;

    public Fee() {

    }

    public Fee(String id, String scn, String name, float value) {
        this();
        this.id = id;
        this.scn = scn;
        this.name = name;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScn() {
        return scn;
    }

    public void setScn(String scn) {
        this.scn = scn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
