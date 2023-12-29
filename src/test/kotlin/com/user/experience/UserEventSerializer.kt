package com.user.experience

import com.fasterxml.jackson.databind.ObjectMapper
import com.user.experience.model.UserEvent
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Serializer

class UserEventSerializer : Serializer<UserEvent> {
    private val objectMapper = ObjectMapper()
    override fun serialize(topic: String?, data: UserEvent?): ByteArray {
        return objectMapper.writeValueAsBytes(data?: throw SerializationException("Error while serialization of UserEvent"))
    }

    override fun close() {}
}