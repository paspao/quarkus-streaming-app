package it.paspaola.quarkus.util;

import it.paspaola.quarkus.dto.FeeBySsn;
import it.paspaola.quarkus.dto.PersonDriverLicenseFee;
import org.apache.kafka.streams.kstream.ValueJoiner;

public class PersonDriverLicenseFeeValueJoiner implements ValueJoiner<PersonDriverLicenseFee, FeeBySsn, PersonDriverLicenseFee> {
    @Override
    public PersonDriverLicenseFee apply(PersonDriverLicenseFee personDriverLicenseFee, FeeBySsn feeBySsn) {
        return null;
    }
}
