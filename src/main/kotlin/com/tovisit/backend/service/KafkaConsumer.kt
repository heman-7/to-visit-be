package com.tovisit.backend.service

import com.tovisit.backend.model.Searched
import org.jboss.logging.Logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumer(
) {
    val log: Logger = Logger.getLogger(this.javaClass)

    @KafkaListener(topics = ["\${kafka.topic}"], groupId = "\${kafka.group.id}")
    fun consume(message: Searched) {
        log.info("Consumed message: $message")
    }

}