package com.tovisit.backend.config.kafka

import com.tovisit.backend.model.Searched
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer

@EnableKafka
@Configuration
class ConsumerConfiguration(
    @Value("\${kafka.bootstrap.address}")
    private val bootstrapAddress: String,
    @Value("\${kafka.topic}")
    private val topic: String
) {

    @Bean
    fun consumerFactory(): ConsumerFactory<String, Searched> {
        val config: HashMap<String, Any> = HashMap()
        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        config[ConsumerConfig.GROUP_ID_CONFIG] = topic
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        return DefaultKafkaConsumerFactory(
            config, JsonDeserializer(), ErrorHandlingDeserializer(
                JsonDeserializer(Searched::class.java)
            )
        )
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Searched> {
        val factory: ConcurrentKafkaListenerContainerFactory<String, Searched> =
            ConcurrentKafkaListenerContainerFactory()
        factory.consumerFactory = consumerFactory()
        return factory
    }

}