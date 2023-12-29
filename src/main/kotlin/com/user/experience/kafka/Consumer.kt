package com.user.experience.kafka

import com.user.experience.model.UserEvent
import com.user.experience.model.UserExperience
import com.user.experience.service.UserExperienceService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component // can be @component
class Consumer<T, U>(
    private val userExperienceService: UserExperienceService // should use service rather than repo directly
    ){

    @KafkaListener(topics = ["\${topic.name.consumer}"], groupId = "group_id")
    fun listenGroupFoo(consumerRecord: ConsumerRecord<Any, Any>, ack: Acknowledgment) {
        userExperienceService.saveUserExperience(UserExperience(userEvent = consumerRecord.value() as UserEvent))
        ack.acknowledge()
    }
}