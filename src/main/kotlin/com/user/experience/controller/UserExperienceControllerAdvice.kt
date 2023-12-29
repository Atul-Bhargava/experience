package com.user.experience.controller

import com.user.experience.error.ErrorResponse
import org.apache.kafka.common.errors.AuthenticationException
import org.apache.kafka.common.errors.AuthorizationException
import org.apache.kafka.common.errors.DisconnectException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessResourceFailureException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.time.format.DateTimeParseException

@ControllerAdvice(annotations = [RestController::class])
class UserExperienceControllerAdvice {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(
        HttpClientErrorException.BadRequest::class,
        HttpRequestMethodNotSupportedException::class,
        MethodArgumentNotValidException::class,
        MissingServletRequestParameterException::class,
        IllegalArgumentException::class,
        DateTimeParseException::class,
        MethodArgumentTypeMismatchException::class
    )
    fun constraintViolationException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.BAD_REQUEST, "Bad request", e)
    }

    @ExceptionHandler(AuthorizationException::class)
    fun unauthorizedException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.FORBIDDEN, "You are not authorized to do this operation", e)
    }

    @ExceptionHandler(AuthenticationException::class)
    fun forbiddenException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.UNAUTHORIZED, "You are not allowed to do this operation", e)
    }

    @ExceptionHandler(
        NoSuchElementException::class,
        EmptyResultDataAccessException::class,
        IndexOutOfBoundsException::class,
        KotlinNullPointerException::class
    )
    fun notFoundException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.NOT_FOUND, "Resource not found", e)
    }

    @ExceptionHandler(
        Exception::class,
        DataAccessResourceFailureException::class,
        DisconnectException::class
    )
    fun internalServerErrorException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Generic internal error", e)
    }

    private fun generateErrorResponse(
        status: HttpStatus,
        message: String,
        e: Exception
    ): ResponseEntity<ErrorResponse> {
        log.error(e.javaClass.name, e.localizedMessage)
        return ResponseEntity(ErrorResponse(status, message, e.localizedMessage), status)
    }

}