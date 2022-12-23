package com.tovisit.backend.service

import com.tovisit.backend.model.Place
import com.tovisit.backend.model.Searched
import com.tovisit.backend.repository.PlaceRepository
import org.jboss.logging.Logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.util.*

@Service
class KafkaConsumer(
    val placeRepository: PlaceRepository,
    val recommendationService: RecommendationService
) {
    val log: Logger = Logger.getLogger(this.javaClass)

    @KafkaListener(topics = ["\${kafka.topic}"], groupId = "\${kafka.group.id}")
    fun consume(searched: Searched) {
        log.info("Consumed message: $searched")

        val place: Optional<Place> = placeRepository.findByCity(searched.city)
//        log.info("place : $place")
        recommendationService.update(place, searched)
    }

}