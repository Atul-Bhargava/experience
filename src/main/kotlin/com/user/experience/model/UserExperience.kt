package com.user.experience.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class UserExperience(
    @Id
    val id: ObjectId = ObjectId.get(), // can be null val id: ObjectId? = null
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val userEvent: UserEvent? // this will create an attribute userEvent but I think we need to persist the entire object
    // instead as nested
)
