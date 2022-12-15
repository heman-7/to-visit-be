package com.tovisit.backend.controller

import com.tovisit.backend.model.Searched
import lombok.extern.slf4j.Slf4j
import org.jboss.logging.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.InetAddress

@RestController
@RequestMapping("/kafka")
@CrossOrigin(origins = ["https://localhost:3000"])
@Slf4j
class KafkaController(
    @Autowired
    private val kafkaTemplate: KafkaTemplate<String, Searched>,
    @Value("\${kafka.topic}")
    private val TOPIC: String
) {

    val log: Logger = Logger.getLogger(this.javaClass)

    @GetMapping("/send")
    fun add(@RequestParam("place") place: String): ResponseEntity<String> {
        var returnMsg = "";
        try {
            val searched = Searched(place = place, sourceIP = InetAddress.getLocalHost().hostAddress)
            val lf: ListenableFuture<SendResult<String, Searched>> = kafkaTemplate.send(TOPIC, searched)
            val sendResult: SendResult<String, Searched> = lf.get()
            returnMsg = sendResult.producerRecord.value().place + " send to topic"
        } catch (ex: Exception) {
            ex.printStackTrace()
            log.info(" exception occurred " + ex.message)
        }
        return ResponseEntity.ok(returnMsg)
    }

}