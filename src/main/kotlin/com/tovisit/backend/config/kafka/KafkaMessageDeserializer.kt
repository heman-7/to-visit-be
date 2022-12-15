package com.tovisit.backend.config.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.tovisit.backend.model.Searched
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer
import org.slf4j.LoggerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import kotlin.text.Charsets.UTF_8

class KafkaMessageDeserializer : ErrorHandlingDeserializer<Searched>(), Deserializer<Searched> {

    private val objectMapper = ObjectMapper()
    private val log = LoggerFactory.getLogger(javaClass)

    override fun deserialize(topic: String?, data: ByteArray?): Searched {
        log.info("Deserializing")
        return objectMapper.readValue(
            String(
                data ?: throw SerializationException("Error when deserializing byte[] to Searched"), UTF_8
            ),
            Searched::class.java
        )
    }

    override fun close() {
    }
}