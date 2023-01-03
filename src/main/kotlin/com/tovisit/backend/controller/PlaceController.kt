package com.tovisit.backend.controller

import com.tovisit.backend.model.Place
import com.tovisit.backend.model.Searched
import com.tovisit.backend.service.PlaceService
import com.tovisit.backend.service.RecommendationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jboss.logging.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.web.bind.annotation.*
import java.net.InetAddress
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/place")
@CrossOrigin(origins = ["http://localhost:3000"])
class PlaceController(
    val placeService: PlaceService,
    val recommendationService: RecommendationService,
    @Autowired
    private val kafkaTemplate: KafkaTemplate<String, Searched>,
    @Value("\${kafka.topic}")
    private val TOPIC: String
) {

    val log: Logger = Logger.getLogger(this.javaClass)

    @PostMapping("/add")
    fun add(@RequestBody place: Place): HashMap<String, String> {
        val hashMap: HashMap<String, String> = HashMap()
        var exceptionOccurred = false
        log.info(" place $place")
        if (place.city.isNotEmpty() && place.attraction.isNotEmpty() && place.month.isNotEmpty()) {
            try {
                placeService.create(place)
            } catch (exception: Exception) {
                exceptionOccurred = true
                log.info(" exception occurred " + exception.message)
            }
            if (exceptionOccurred) {
                hashMap.set(key = "message", value = "could not create place, exception occurred")
                hashMap.set(key = "status", value = "Failure")
            } else {
                hashMap.set(key = "message", value = "created place ${place.city}")
                hashMap.set(key = "status", value = "Success")
                log.info(" created ${place.city} successfully ")
            }
        } else {
            hashMap.set(key = "message", value = "could not create place with empty data")
            hashMap.set(key = "status", value = "Failure")
            log.info("could not create place  with empty data")
        }
        return hashMap
    }

    @GetMapping("/list")
    fun list(): List<Place> {
        val places: List<Place> = placeService.findAll()
        log.info(" retrieved places successfully ")
        return places
    }

    @DeleteMapping("/remove")
    fun remove(@RequestParam id: String): HashMap<String, String> {
        placeService.delete(id)
        val hashMap: HashMap<String, String> = HashMap()
        hashMap.set(key = "message", value = "deleted place $id")
        hashMap.set(key = "status", value = "Success")
        log.info(" deleted place $id successfully ")
        return hashMap
    }

    @PutMapping("/update")
    fun update(@RequestBody place: Place): HashMap<String, String> {
        log.info(" place $place")
        val isUpdated = placeService.update(place)
        val hashMap: HashMap<String, String> = HashMap()
        if (isUpdated) {
            hashMap.set(key = "message", value = "updated place ${place.city}")
            hashMap.set(key = "status", value = "Success")
            log.info(" updated ${place.city} successfully ")
        } else {
            hashMap.set(key = "message", value = "place not found ")
            hashMap.set(key = "status", value = "Failed")
        }
        return hashMap
    }

    @GetMapping("/get")
    fun get(@RequestParam id: String): ResponseEntity<Any> {
        val place: Optional<Place> = placeService.find(id)
        return if (place.isPresent) {
            log.info(" retrieved place $id successfully ")
            ResponseEntity.status(HttpStatus.OK).body(place.get())
        } else {
            log.info(" place not found ")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(" Place Not found")
        }
    }

    @GetMapping("/search")
    suspend fun search(@RequestParam("place") place: String): ResponseEntity<String> {
        var returnMsg = "";
        try {
            val searched = Searched(
                city = place,
                sourceIP = withContext(Dispatchers.IO) {
                    InetAddress.getLocalHost()
                }.hostAddress,
                searchedAt = Instant.now().toEpochMilli()
            )
            //launch a separate coroutine
            coroutineScope {
                launch {
                    val lf: ListenableFuture<SendResult<String, Searched>> = kafkaTemplate.send(TOPIC, searched)
                    val sendResult: SendResult<String, Searched> =
                        withContext(Dispatchers.IO) {
                            lf.get()
                        }
                    returnMsg = sendResult.producerRecord.value().city + " send to topic"
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            log.info(" exception occurred " + ex.message)
        }
        return ResponseEntity.ok(returnMsg)
    }

    @GetMapping("/recommendations")
    fun recommendations(): ResponseEntity<Any> {
        val recommendations = recommendationService.fetch()
        return ResponseEntity.ok(recommendations)
    }

}