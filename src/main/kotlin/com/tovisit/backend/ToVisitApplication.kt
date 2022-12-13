package com.tovisit.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ToVisitApplication

fun main(args: Array<String>) {
	runApplication<ToVisitApplication>(*args)
}
