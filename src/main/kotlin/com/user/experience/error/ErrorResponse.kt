package com.user.experience.error

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import java.util.*

class ErrorResponse(
    status: HttpStatus,
    val message: String,
    localMessage: String
) {

    val code: Int = status.value()
    val detailedMessage : String = localMessage
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss")
    val timestamp: Date = Date()
}