package lm;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.List;

@SpringBootTest(
        properties = {
                "spring.embedded.kafka.brokers.property=spring.kafka.bootstrap-servers",
                "spring.kafka.properties.schema.registry.url=mock://localhost"
        }
)
@Import({KafkaTestConfiguration.class})
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {
                "auto.create.topics.enable=false",
                "delete.topic.enable=true"
        },
        topics = {
                "topic1",
                "topic2"
        }
)
public class ListenerTest {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void test_single() throws InterruptedException {
        RecordHeader header1 = new RecordHeader("header1", "headerValue1".getBytes());
        ProducerRecord<String, String> record = new ProducerRecord<>("topic1", 0, "key1", "value1", List.of(header1));
        kafkaTemplate.send(record);
        Thread.sleep(20000);
    }
}
