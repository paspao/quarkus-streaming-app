package it.paspaola.quarkus.streaming;

import io.quarkus.runtime.configuration.ConfigUtils;
import it.paspaola.quarkus.dto.DriverLicense;
import it.paspaola.quarkus.dto.Fee;
import it.paspaola.quarkus.dto.FeeBySsn;
import it.paspaola.quarkus.dto.Person;
import it.paspaola.quarkus.dto.PersonDriverLicense;
import it.paspaola.quarkus.dto.PersonDriverLicenseFee;
import it.paspaola.quarkus.dto.Sim;
import it.paspaola.quarkus.dto.serde.FactorySerde;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@ApplicationScoped
public class StreamingApp {

    private final static Logger logger = LoggerFactory.getLogger(StreamingApp.class.getName());

    private final static String ALL = "alltogether";
    private final static String ALL_ENHANCEMENT = "alltogether-enhancement";
    private final static String DRIVERLICENSE = "driverlicense";
    private final static String FEE = "fee";
    private final static String PERSON = "person";
    private final static String SIM = "sim";
    private final static DateTimeFormatter dateOfBirthFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");


    @PostConstruct
    public void postInit() {
        if (!ConfigUtils.isProfileActive("test")) {
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-1:19092");
            props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer");
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, FactorySerde.PersonSerializer.class);

            AdminClient adminClient = KafkaAdminClient.create(props);
            adminClient.createTopics(List.of(new NewTopic(PERSON, 3, (short) 3)));
            adminClient.createTopics(List.of(new NewTopic(SIM, 4, (short) 3)));
            adminClient.createTopics(List.of(new NewTopic(FEE, 5, (short) 3)));
            adminClient.createTopics(List.of(new NewTopic(DRIVERLICENSE, 3, (short) 3)));
            adminClient.createTopics(List.of(new NewTopic(ALL_ENHANCEMENT, 3, (short) 3)));
            CreateTopicsResult resultAllTogether = adminClient.createTopics(List.of(new NewTopic(ALL, 4, (short) 3)));


            resultAllTogether.all().whenComplete((unused, throwable) -> {
                if (throwable == null) {
                    List<String> carType = Collections.singletonList("CAR");
                    List<String> motorbikeAndCarType = List.of("CAR", "MOTORBIKE");
                    KafkaProducer<String, Person> kafkaPersonProducer = new KafkaProducer<>(props, Serdes.String().serializer(), FactorySerde.getPersonSerde().serializer());
                    KafkaProducer<String, Fee> kafkaFeeProducer = new KafkaProducer<>(props, Serdes.String().serializer(), FactorySerde.getFeeSerde().serializer());
                    KafkaProducer<String, Sim> kafkaSimProducer = new KafkaProducer<>(props, Serdes.String().serializer(), FactorySerde.getSimSerde().serializer());
                    KafkaProducer<String, DriverLicense> kafkaDriverLicenseProducer = new KafkaProducer<>(props, Serdes.String().serializer(), FactorySerde.getDriverLicenseSerde().serializer());

                    ProducerRecord<String, Person> personProducerRecord1 = new ProducerRecord<>(PERSON, "456", new Person("456", "Pasquale", "Paola", Person.SEX.M, LocalDate.parse("19810625", dateOfBirthFormatter), "1113245768"));
                    ProducerRecord<String, Person> personProducerRecord2 = new ProducerRecord<>(PERSON, "567", new Person("567", "Giacomo", "Matteotti", Person.SEX.M, LocalDate.parse("19810626", dateOfBirthFormatter), "1113245778"));
                    ProducerRecord<String, Person> personProducerRecord3 = new ProducerRecord<>(PERSON, "678", new Person("678", "Giulia", "Cesare", Person.SEX.F, LocalDate.parse("19810627", dateOfBirthFormatter), "1113245788"));
                    ProducerRecord<String, Person> personProducerRecord4 = new ProducerRecord<>(PERSON, "789", new Person("789", "Giacchino", "Murat", Person.SEX.M, LocalDate.parse("19810628", dateOfBirthFormatter), "1113245798"));

                    ProducerRecord<String, Fee> feeProducerRecord1 = new ProducerRecord<>(FEE, "123", new Fee("123", "456", "IMU", 512));
                    ProducerRecord<String, Fee> feeProducerRecord2 = new ProducerRecord<>(FEE, "234", new Fee("234", "567", "IMU", 513));
                    ProducerRecord<String, Fee> feeProducerRecord3 = new ProducerRecord<>(FEE, "345", new Fee("345", "678", "IMU", 514));
                    ProducerRecord<String, Fee> feeProducerRecord4 = new ProducerRecord<>(FEE, "456", new Fee("456", "456", "TASI", 515));
                    ProducerRecord<String, Fee> feeProducerRecord5 = new ProducerRecord<>(FEE, "567", new Fee("567", "456", "IMU", 516));

                    ProducerRecord<String, Sim> simProducerRecord1 = new ProducerRecord<>(SIM, "111", new Sim("111", "TIM", "4783", "1113245768"));
                    ProducerRecord<String, Sim> simProducerRecord2 = new ProducerRecord<>(SIM, "222", new Sim("222", "VODAFONE", "4784", "1113245778"));
                    ProducerRecord<String, Sim> simProducerRecord3 = new ProducerRecord<>(SIM, "333", new Sim("333", "WIND", "4785", "1113245788"));
                    ProducerRecord<String, Sim> simProducerRecord4 = new ProducerRecord<>(SIM, "444", new Sim("444", "TRE", "4786", "1113245798"));

                    ProducerRecord<String, DriverLicense> driverLicenseProducerRecord1 = new ProducerRecord<>(DRIVERLICENSE, "456", new DriverLicense("456", carType, "1213245768"));
                    ProducerRecord<String, DriverLicense> driverLicenseProducerRecord2 = new ProducerRecord<>(DRIVERLICENSE, "456", new DriverLicense("456", motorbikeAndCarType, "1213245768"));
                    ProducerRecord<String, DriverLicense> driverLicenseProducerRecord3 = new ProducerRecord<>(DRIVERLICENSE, "567", new DriverLicense("567", carType, "2213245768"));
                    ProducerRecord<String, DriverLicense> driverLicenseProducerRecord4 = new ProducerRecord<>(DRIVERLICENSE, "678", new DriverLicense("678", carType, "3213245768"));
                    ProducerRecord<String, DriverLicense> driverLicenseProducerRecord5 = new ProducerRecord<>(DRIVERLICENSE, "789", new DriverLicense("789", carType, "4213245768"));

                    kafkaPersonProducer.send(personProducerRecord1);
                    kafkaPersonProducer.send(personProducerRecord2);
                    kafkaPersonProducer.send(personProducerRecord3);
                    kafkaPersonProducer.send(personProducerRecord4);

                    kafkaFeeProducer.send(feeProducerRecord1);
                    kafkaFeeProducer.send(feeProducerRecord2);
                    kafkaFeeProducer.send(feeProducerRecord3);
                    kafkaFeeProducer.send(feeProducerRecord4);
                    kafkaFeeProducer.send(feeProducerRecord5);

                    kafkaSimProducer.send(simProducerRecord1);
                    kafkaSimProducer.send(simProducerRecord2);
                    kafkaSimProducer.send(simProducerRecord3);
                    kafkaSimProducer.send(simProducerRecord4);

                    kafkaDriverLicenseProducer.send(driverLicenseProducerRecord1);
                    kafkaDriverLicenseProducer.send(driverLicenseProducerRecord2);
                    kafkaDriverLicenseProducer.send(driverLicenseProducerRecord3);
                    kafkaDriverLicenseProducer.send(driverLicenseProducerRecord4);
                    kafkaDriverLicenseProducer.send(driverLicenseProducerRecord5);
                }
            });
        }

    }


    final StreamsBuilder builder = new StreamsBuilder();

    final ForeachAction<String, Person> loggingForEach = (key, person) -> {
        if (person != null)
            logger.info("Key: {}, Value: {}", key, person);
    };

    //@Produces
    public Topology appJoinPersonAndDriverLicense() {

        final KStream<String, Person> personStream = builder.stream(PERSON,
                Consumed.with(Serdes.String(), FactorySerde.getPersonSerde()));
        final KStream<String, DriverLicense> driverLicenseStream = builder.stream(DRIVERLICENSE,
                Consumed.with(Serdes.String(), FactorySerde.getDriverLicenseSerde()));

        KStream<String, Person> peekStream = personStream.peek(loggingForEach);

        KStream<String, PersonDriverLicense> joined = peekStream.join(driverLicenseStream,
                (person, license) -> new PersonDriverLicense(person, license),
                JoinWindows.of(Duration.of(5, ChronoUnit.MINUTES)), StreamJoined.with(
                        Serdes.String(), /* key */
                        FactorySerde.getPersonSerde(),   /* left value */
                        FactorySerde.getDriverLicenseSerde()  /* right value */
                ));

        joined.to(ALL, Produced.with(Serdes.String(), FactorySerde.getPersonDriverLicenseSerde()));

        Topology build = builder.build();
        logger.info(build.describe().toString());
        return build;
    }

    @Produces
    public Topology appJoinPersonAndDriverLicenseAndFee() {

        final KStream<String, Person> personStream = builder.stream(PERSON,
                Consumed.with(Serdes.String(), FactorySerde.getPersonSerde()));
        final KStream<String, DriverLicense> driverLicenseStream = builder.stream(DRIVERLICENSE,
                Consumed.with(Serdes.String(), FactorySerde.getDriverLicenseSerde()));


        KStream<String, Person> peekStream = personStream.peek(loggingForEach);

        KStream<String, PersonDriverLicense> joinedPersonDriverLicense = peekStream.join(driverLicenseStream,
                (person, license) -> new PersonDriverLicense(person, license),
                JoinWindows.of(Duration.of(5, ChronoUnit.MINUTES)), StreamJoined.with(
                        Serdes.String(),
                        FactorySerde.getPersonSerde(),
                        FactorySerde.getDriverLicenseSerde()
                ));
        joinedPersonDriverLicense.to(ALL, Produced.with(Serdes.String(), FactorySerde.getPersonDriverLicenseSerde()));

        final KStream<String, Fee> feeStream = builder.stream(FEE,
                Consumed.with(Serdes.String(), FactorySerde.getFeeSerde()));

        KeyValueBytesStoreSupplier feeBySsnStoreSupplier = Stores.persistentKeyValueStore("fee-by-ssn-store");

        final Materialized<String, FeeBySsn, KeyValueStore<Bytes, byte[]>> materialized =
                Materialized.<String, FeeBySsn>as(feeBySsnStoreSupplier)
                        .withKeySerde(Serdes.String())
                        .withValueSerde(FactorySerde.getFeeBySsnSerde());


        KTable<String, FeeBySsn> feeBySSnTable = feeStream
                .selectKey((key, value) -> value.getSsn(), Named.as("fee-by-ssn-select-key"))
                .groupByKey(Grouped.with(Serdes.String(), FactorySerde.getFeeSerde()))
                //.groupBy((key, value) -> value.getSsn())
                .aggregate(FeeBySsn::new, (key, value, aggregate) -> {
                    aggregate.setSsn(key);
                    aggregate.getFeeList().add(value);
                    return aggregate;
                }, Named.as("fee-by-ssn-table"), materialized);

        KStream<String, PersonDriverLicenseFee> joinedPersonDriverLicenseFee = joinedPersonDriverLicense.join(feeBySSnTable,
                (readOnlyKey, personDriverLicense, feeBySsn) -> new PersonDriverLicenseFee(personDriverLicense.getPerson(), personDriverLicense.getDriverLicense(), feeBySsn));


        joinedPersonDriverLicenseFee.to(ALL_ENHANCEMENT, Produced.with(Serdes.String(), FactorySerde.getPersonDriverLicenseFeeSerde()));

        Topology build = builder.build();
        logger.info(build.describe().toString());
        return build;
    }
}
