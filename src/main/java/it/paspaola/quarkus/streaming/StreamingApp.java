package it.paspaola.quarkus.streaming;

import it.paspaola.quarkus.dto.DriverLicense;
import it.paspaola.quarkus.dto.Fee;
import it.paspaola.quarkus.dto.FeeBySsn;
import it.paspaola.quarkus.dto.Person;
import it.paspaola.quarkus.dto.PersonDriverLicense;
import it.paspaola.quarkus.dto.PersonDriverLicenseFee;
import it.paspaola.quarkus.dto.Sim;
import it.paspaola.quarkus.dto.serde.FactorySerde;
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
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
                KafkaProducer kafkaPersonProducer = new KafkaProducer<>(props, Serdes.String().serializer(), FactorySerde.getPersonSerde().serializer());
                KafkaProducer kafkaFeeProducer = new KafkaProducer<>(props, Serdes.String().serializer(), FactorySerde.getFeeSerde().serializer());
                KafkaProducer kafkaSimProducer = new KafkaProducer<>(props, Serdes.String().serializer(), FactorySerde.getSimSerde().serializer());
                KafkaProducer kafkaDriverLicenseProducer = new KafkaProducer<>(props, Serdes.String().serializer(), FactorySerde.getDriverLicenseSerde().serializer());

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

                ProducerRecord<String, DriverLicense> driverLicenseProducerRecord1 = new ProducerRecord<>(DRIVERLICENSE, "456", new DriverLicense("456", "CAR", "1213245768"));
                ProducerRecord<String, DriverLicense> driverLicenseProducerRecord2 = new ProducerRecord<>(DRIVERLICENSE, "567", new DriverLicense("567", "CAR", "2213245768"));
                ProducerRecord<String, DriverLicense> driverLicenseProducerRecord3 = new ProducerRecord<>(DRIVERLICENSE, "678", new DriverLicense("678", "CAR", "3213245768"));
                ProducerRecord<String, DriverLicense> driverLicenseProducerRecord4 = new ProducerRecord<>(DRIVERLICENSE, "789", new DriverLicense("789", "CAR", "4213245768"));

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
            }
        });

    }


    final StreamsBuilder builder = new StreamsBuilder();

    final ForeachAction<String, Person> loggingForEach = (key, person) -> {
        if (person != null)
            logger.info("Key: {}, Value: {}", key, person);
    };

    // @Produces
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

        final KStream<String, Fee> feeStream = builder.stream(FEE,
                Consumed.with(Serdes.String(), FactorySerde.getFeeSerde()));

        KeyValueBytesStoreSupplier feeBySsnTableSupplier = Stores.persistentKeyValueStore("feeBySsnTableSupplier");

        final Materialized<String, FeeBySsn, KeyValueStore<Bytes, byte[]>> materialized =
                Materialized.<String, FeeBySsn>as(feeBySsnTableSupplier)
                        .withKeySerde(Serdes.String())
                        .withValueSerde(FactorySerde.getFeeBySsnSerde());


        KStream<String, Person> peekStream = personStream.peek(loggingForEach);

        KStream<String, PersonDriverLicense> joinedPersonDriverLicense = peekStream.join(driverLicenseStream,
                (person, license) -> new PersonDriverLicense(person, license),
                JoinWindows.of(Duration.of(5, ChronoUnit.MINUTES)), StreamJoined.with(
                        Serdes.String(), /* key */
                        FactorySerde.getPersonSerde(),   /* left value */
                        FactorySerde.getDriverLicenseSerde()  /* right value */
                ));


        joinedPersonDriverLicense.to(ALL, Produced.with(Serdes.String(), FactorySerde.getPersonDriverLicenseSerde()));

        KStream<String, PersonDriverLicenseFee> joinedPersonDriverLicenseFee = feeStream
                .selectKey((key, value) -> value.getSsn())
                .groupByKey(Grouped.with(Serdes.String(), FactorySerde.getFeeSerde()))
                .aggregate(() -> new FeeBySsn(), (key, value, aggregate) -> {
                    aggregate.setSsn(key);
                    aggregate.getFeeList().add(value);
                    return aggregate;
                }, materialized)
                .toStream()
                .join(joinedPersonDriverLicense,
                        (feeBySsn, personDl) -> new PersonDriverLicenseFee(personDl.getPerson(), personDl.getDriverLicense(), feeBySsn),
                        JoinWindows.of(Duration.of(5, ChronoUnit.MINUTES)), StreamJoined.with(
                                Serdes.String(), /* key */
                                FactorySerde.getFeeBySsnSerde(),   /* left value */
                                FactorySerde.getPersonDriverLicenseSerde()  /* right value */
                        ));
        joinedPersonDriverLicenseFee.to(ALL_ENHANCEMENT, Produced.with(Serdes.String(), FactorySerde.getPersonDriverLicenseFeeSerde()));

        Topology build = builder.build();
        logger.info(build.describe().toString());
        return build;
    }
}
