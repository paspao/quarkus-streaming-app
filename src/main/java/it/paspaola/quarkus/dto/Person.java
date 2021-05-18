package it.paspaola.quarkus.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.paspaola.quarkus.util.JsonDateOfBirthDeserializer;

import java.time.LocalDate;

public class Person {


    private String scn;
    private String name;
    private String surname;
    private SEX sex;
    @JsonDeserialize(using = JsonDateOfBirthDeserializer.class)
    private LocalDate dateOfBirth;
    private String phoneNumber;

    public Person(){}
    public Person(String scn, String name, String surname, SEX sex, LocalDate dateOfBirth, String phoneNumber) {
        this();
        this.scn = scn;
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public SEX getSex() {
        return sex;
    }

    public void setSex(SEX sex) {
        this.sex = sex;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Person{" +
                "scn='" + scn + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", sex=" + sex +
                ", dateOfBirth=" + dateOfBirth +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public enum SEX {M, F}

}
