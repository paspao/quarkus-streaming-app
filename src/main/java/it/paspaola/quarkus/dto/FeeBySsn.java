package it.paspaola.quarkus.dto;

import java.util.ArrayList;
import java.util.List;

public class FeeBySsn {

    private String ssn;

    private List<Fee> feeList;

    public FeeBySsn() {
        this.feeList = new ArrayList<>();
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public List<Fee> getFeeList() {
        return feeList;
    }

    public void setFeeList(List<Fee> feeList) {
        this.feeList = feeList;
    }
}
