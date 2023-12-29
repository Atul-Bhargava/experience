package com.user.experience.kafka.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.user.experience.model.UserEvent
import org.apache.kafka.common.serialization.Deserializer
import org.slf4j.LoggerFactory
import kotlin.text.Charsets.UTF_8

class UserEventDeserializer : Deserializer<UserEvent> {
    private val objectMapper = ObjectMapper()
    private val log = LoggerFactory.getLogger(javaClass)

    override fun deserialize(topic: String?, data: ByteArray?): UserEvent? {
        log.info("Deserializing...")
        return objectMapper.readValue(
            String(
                data ?: throw RuntimeException("Error when deserializing byte[] to Product"), UTF_8
            ), UserEvent::class.java
        )
    }

    override fun close() {}

}