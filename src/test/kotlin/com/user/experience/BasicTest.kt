package com.user.experience

import com.user.experience.model.UserEvent
import com.user.experience.model.UserExperience
import com.user.experience.service.UserExperienceService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.utils.KafkaTestUtils

class BasicTest : ExperienceApplicationTests(){

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, UserEvent>

    private val topicName = "test.user.events"

    @Autowired
    lateinit var userEventConsumer: org.apache.kafka.clients.consumer.Consumer<String, UserEvent>

    @Autowired
    lateinit var userExperienceService: UserExperienceService

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun checkConsumer() {

        val userEvent = UserEvent(
            1,
            "20d092eb-biez-41ca-ad94-d2ce853e81bd",
            2,
            2,
            "keyboard",
            "1",
            "search",
            "515770"
        )

        kafkaTemplate.send(topicName, userEvent)

        val responseMessage: List<UserEvent> = KafkaTestUtils
            .getRecords(userEventConsumer).records(topicName).map { entity -> entity.value()!! }

        responseMessage.forEach {
            Assertions.assertTrue(it.auctionId == userEvent.auctionId)
        }
    }

    @Test
    fun checkDatabase() {
        val userEvent = UserEvent(
            1,
            "20d092eb-biez-41ca-ad94-d2ce853e81bd",
            2,
            2,
            "keyboard",
            "1",
            "search",
            "515770"
        )

        kafkaTemplate.send(topicName, userEvent)

        val responseMessage: List<UserEvent> = KafkaTestUtils
            .getRecords(userEventConsumer).records(topicName).map { entity -> entity.value()!! }

        responseMessage.forEach {
            userExperienceService.saveUserExperience(UserExperience(userEvent =it))
        }

        val userExperience: List<UserExperience> = userExperienceRepo.findAll()

        Assertions.assertEquals(userExperience.count(), 1)
    }

    @Test
    fun checkEventControllerForPost() {

        val dateString = "2023-09-15"

        val result = getResponseFromApi(dateString, "Post")

        Assertions.assertNotNull(result)
        if (result != null) {
            Assertions.assertEquals(HttpStatus.METHOD_NOT_ALLOWED, result.statusCode)
        }
    }

    @Test
    fun checkEventControllerForGetWithWrongDate() {

        val dateString = "2023-09-31"

        val result = getResponseFromApi(dateString, "Get")

        Assertions.assertNotNull(result)
        if (result != null) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
        }
    }

    @Test
    fun checkEventControllerForGetWithWrongDateFormat() {

        val dateString = "15-09-2023"

        val result = getResponseFromApi(dateString, "Get")

        Assertions.assertNotNull(result)
        if (result != null) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
        }
    }


    @Test
    fun checkEventControllerForGet() {

        val dateString = "2023-10-18"

        val userEvent = UserEvent(
            1,
            "20d092eb-biez-41ca-ad94-d2ce853e81bd",
            2,
            2,
            "keyboard",
            "1",
            "search",
            "515770"
        )

        kafkaTemplate.send(topicName, userEvent)

        val responseMessage: List<UserEvent> = KafkaTestUtils
            .getRecords(userEventConsumer).records(topicName).map { entity -> entity.value()!! }

        responseMessage.forEach {
            userExperienceService.saveUserExperience(UserExperience(userEvent =it))
        }

        val result = getResponseFromApi(dateString, "Get")

        Assertions.assertNotNull(result)
        if (result != null) {
            Assertions.assertEquals(HttpStatus.OK, result.statusCode)
            log.info(result.body.toString())
            val userExperience = result.body as  ArrayList<*>
            Assertions.assertEquals(1, userExperience.size)
        }
    }

    fun getResponseFromApi(dateString: String, type: String): ResponseEntity<Any>? {
        val result : ResponseEntity<Any>? = null
        when(type) {
            "Get" -> return restTemplate.getForEntity("/report/$dateString", Any::class.java)
            "Post" -> return restTemplate.postForEntity("/report/$dateString", UserExperience(userEvent = null), Any::class.java)
        }
        return result
    }
}