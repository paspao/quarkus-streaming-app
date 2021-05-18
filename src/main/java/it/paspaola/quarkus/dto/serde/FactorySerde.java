package it.paspaola.quarkus.dto.serde;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.paspaola.quarkus.dto.DriverLicense;
import it.paspaola.quarkus.dto.Fee;
import it.paspaola.quarkus.dto.Person;
import it.paspaola.quarkus.dto.PersonDriverLicense;
import it.paspaola.quarkus.dto.Sim;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FactorySerde {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(FactorySerde.class.getName());

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static Serde<Person> getPersonSerde() {
        return new Serde<>() {
            @Override
            public Serializer<Person> serializer() {
                return new PersonSerializer();
            }

            @Override
            public Deserializer<Person> deserializer() {
                return new PersonDeserializer();
            }
        };
    }

    public static Serde<DriverLicense> getDriverLicenseSerde() {
        return new Serde<>() {
            @Override
            public Serializer<DriverLicense> serializer() {
                return new DriverLicenseSerde();
            }

            @Override
            public Deserializer<DriverLicense> deserializer() {
                return new DriverLicenseDeserializer();
            }
        };
    }

    public static Serde<PersonDriverLicense> getPersonDriverLicenseSerde() {
        return new Serde<>() {
            @Override
            public Serializer<PersonDriverLicense> serializer() {
                return new PersonDriverLicenseSerializer();
            }

            @Override
            public Deserializer<PersonDriverLicense> deserializer() {
                return new PersonDriverLicenseDeserializer();
            }
        };
    }

    public static Serde<Sim> getSimSerde() {
        return new Serde<>() {
            @Override
            public Serializer<Sim> serializer() {
                return new SimSerializer();
            }

            @Override
            public Deserializer<Sim> deserializer() {
                return new SimDeserializer();
            }
        };
    }

    public static Serde<Fee> getFeeSerde() {
        return new Serde<>() {
            @Override
            public Serializer<Fee> serializer() {
                return new FeeSerializer();
            }

            @Override
            public Deserializer<Fee> deserializer() {
                return new FeeDeserializer();
            }
        };
    }


    public static final class PersonSerializer implements Serializer<Person> {


        @Override
        public byte[] serialize(String topic, Person person) {
            byte[] res = null;
            try {
                res = objectMapper.writeValueAsBytes(person);
            } catch (JsonProcessingException e) {
                logger.error("ERROR", e);
            }
            return res;
        }
    }


    public static final class PersonDeserializer implements Deserializer<Person> {

        @Override
        public Person deserialize(String topic, byte[] bytes) {
            Person person = null;
            try {
                person = objectMapper.readValue(bytes, Person.class);
            } catch (IOException e) {
                logger.error("ERROR", e);
            }
            return person;
        }
    }


    static final class DriverLicenseSerde implements Serializer<DriverLicense> {


        @Override
        public byte[] serialize(String topic, DriverLicense person) {
            byte[] res = null;
            try {
                res = objectMapper.writeValueAsBytes(person);
            } catch (JsonProcessingException e) {
                logger.error("ERROR", e);
            }
            return res;
        }
    }


    public static final class DriverLicenseDeserializer implements Deserializer<DriverLicense> {

        @Override
        public DriverLicense deserialize(String topic, byte[] bytes) {
            DriverLicense driverLicense = null;
            try {
                driverLicense = objectMapper.readValue(bytes, DriverLicense.class);
            } catch (IOException e) {
                logger.error("ERROR", e);
            }
            return driverLicense;
        }
    }

    public static final class PersonDriverLicenseSerializer implements Serializer<PersonDriverLicense> {


        @Override
        public byte[] serialize(String topic, PersonDriverLicense personDriverLicense) {
            byte[] res = null;
            try {
                res = objectMapper.writeValueAsBytes(personDriverLicense);
            } catch (JsonProcessingException e) {
                logger.error("ERROR", e);
            }
            return res;
        }
    }

    public static final class PersonDriverLicenseDeserializer implements Deserializer<PersonDriverLicense> {


        @Override
        public PersonDriverLicense deserialize(String topic, byte[] bytes) {
            PersonDriverLicense driverLicense = null;
            try {
                driverLicense = objectMapper.readValue(bytes, PersonDriverLicense.class);
            } catch (IOException e) {
                logger.error("ERROR", e);
            }
            return driverLicense;
        }
    }

    public static final class FeeSerializer implements Serializer<Fee> {


        @Override
        public byte[] serialize(String topic, Fee fee) {
            byte[] res = null;
            try {
                res = objectMapper.writeValueAsBytes(fee);
            } catch (JsonProcessingException e) {
                logger.error("ERROR", e);
            }
            return res;
        }
    }

    public static final class FeeDeserializer implements Deserializer<Fee> {


        @Override
        public Fee deserialize(String topic, byte[] bytes) {
            Fee fee = null;
            try {
                fee = objectMapper.readValue(bytes, Fee.class);
            } catch (IOException e) {
                logger.error("ERROR", e);
            }
            return fee;
        }
    }

    public static final class SimSerializer implements Serializer<Sim> {


        @Override
        public byte[] serialize(String topic, Sim sim) {
            byte[] res = null;
            try {
                res = objectMapper.writeValueAsBytes(sim);
            } catch (JsonProcessingException e) {
                logger.error("ERROR", e);
            }
            return res;
        }
    }

    public static final class SimDeserializer implements Deserializer<Sim> {


        @Override
        public Sim deserialize(String topic, byte[] bytes) {
            Sim sim = null;
            try {
                sim = objectMapper.readValue(bytes, Sim.class);
            } catch (IOException e) {
                logger.error("ERROR", e);
            }
            return sim;
        }
    }
}
