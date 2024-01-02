package it.paspaola.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import it.paspaola.quarkus.dto.DriverLicense;
import it.paspaola.quarkus.dto.Fee;
import it.paspaola.quarkus.dto.Person;
import it.paspaola.quarkus.dto.PersonDriverLicense;
import it.paspaola.quarkus.dto.PersonDriverLicenseFee;
import it.paspaola.quarkus.dto.Sim;
import it.paspaola.quarkus.dto.serde.FactorySerde;
import jakarta.inject.Inject;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@QuarkusTest
public class TestTopology {

    static final Logger log = Logger.getLogger(TestTopology.class);

    @Inject
    Topology topology;

    TopologyTestDriver testDriver;

    TestInputTopic<String, Person> inputTopicPerson;
    TestInputTopic<String, Fee> inputTopicFee;
    TestInputTopic<String, DriverLicense> inputTopicDriverLicense;
    TestInputTopic<String, Sim> inputTopicSim;

    TestOutputTopic<String, PersonDriverLicense> outputTopic;
    TestOutputTopic<String, PersonDriverLicenseFee> outputTopicEnhancement;


    @BeforeEach
    public void init() throws IOException {


        // Dummy properties needed for test diver
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        props.put(StreamsConfig.STATE_DIR_CONFIG, "target/kafka-streams-1");

        // Create test driver
        testDriver = new TopologyTestDriver(topology, props);

        // Define input topics to use in tests
        inputTopicPerson = testDriver.createInputTopic(
                "person",
                Serdes.String().serializer(),
                FactorySerde.getPersonSerde().serializer()
        );

        inputTopicFee = testDriver.createInputTopic(
                "fee",
                Serdes.String().serializer(),
                FactorySerde.getFeeSerde().serializer()
        );

        inputTopicDriverLicense = testDriver.createInputTopic(
                "driverlicense",
                Serdes.String().serializer(),
                FactorySerde.getDriverLicenseSerde().serializer()
        );
        inputTopicSim = testDriver.createInputTopic(
                "sim",
                Serdes.String().serializer(),
                FactorySerde.getSimSerde().serializer()
        );


        outputTopic = testDriver.createOutputTopic(
                "alltogether",
                Serdes.String().deserializer(),
                FactorySerde.getPersonDriverLicenseSerde().deserializer()
        );

        outputTopicEnhancement = testDriver.createOutputTopic(
                "alltogether-enhancement",
                Serdes.String().deserializer(),
                FactorySerde.getPersonDriverLicenseFeeSerde().deserializer()
        );

    }

    @AfterEach
    public void tearDown() {
        if (testDriver != null)
            testDriver.close();
    }

    @Test
    public void testTopology() throws InterruptedException {

        Thread.sleep(1000);
        List result = outputTopic.readRecordsToList();
        Assertions.assertNotNull(result, "Check is not null");
        Assertions.assertEquals(1, result.size(), "Check the size of the result");


    }

}
