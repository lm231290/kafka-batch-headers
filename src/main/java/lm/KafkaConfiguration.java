package lm;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Map;

@Configuration
public class KafkaConfiguration {
    @Bean
    public CommonErrorHandler errorHandler() {
        DefaultErrorHandler defaultErrorHandler = new DefaultErrorHandler(new FixedBackOff(5000, 2));
        defaultErrorHandler.addRetryableExceptions(RetryableException.class);
        return defaultErrorHandler;
    }

    @Bean
    public DefaultKafkaConsumerFactory<String, String> defaultKafkaConsumerFactory(
            KafkaProperties kafkaProperties,
            ObjectProvider<SslBundles> sslBundles) {
        Map<String, Object> properties = kafkaProperties.buildConsumerProperties(sslBundles.getIfAvailable());
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            CommonErrorHandler commonErrorHandler,
            DefaultKafkaConsumerFactory<String, String> defaultKafkaConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(defaultKafkaConsumerFactory);
        factory.setCommonErrorHandler(commonErrorHandler);
        return factory;
    }
}
