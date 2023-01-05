package com.tovisit.backend.controller

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
class AuthenticationController {

    @GetMapping("/basicauth")
    fun authenticate(): String {
        return "You are authenticated"
    }

}