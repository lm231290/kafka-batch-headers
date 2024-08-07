package lm;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;


@Service
public class Listener {

    private static Logger log = Logger.getLogger(Listener.class.getName());

    @KafkaListener(topics = "topic1", groupId = "lm")
    public void consume(@Payload String message, @Headers MessageHeaders headers) {
        log.info("received message " + message);
        if (message.equals("value1")) {
            throw new RetryableException("simulated exception");
        }
    }
}
