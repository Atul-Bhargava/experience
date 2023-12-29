package com.user.experience

import com.user.experience.kafka.config.UserEventDeserializer
import com.user.experience.model.UserEvent
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.EmbeddedKafkaBroker

@TestConfiguration
class TestConfig {

    val topicName = "test.user.events"

    @Bean
    fun userEventConsumer(
        properties: KafkaProperties,
        embeddedKafkaBroker: EmbeddedKafkaBroker
    ): Consumer<String, UserEvent> {
        val configs = properties.buildConsumerProperties()
        configs[ConsumerConfig.GROUP_ID_CONFIG] = "userGroup"
        val consumerFactory = DefaultKafkaConsumerFactory(
            configs,
            StringDeserializer(),
            UserEventDeserializer()
        )
        val consumer: Consumer<String, UserEvent> =
            consumerFactory.createConsumer()
        embeddedKafkaBroker
            .consumeFromAnEmbeddedTopic(consumer, topicName)
        return consumer
    }

    @Bean
    fun userEventProducerFactory(properties: KafkaProperties) =
        DefaultKafkaProducerFactory(
            properties.buildProducerProperties(),
            StringSerializer(),
            UserEventSerializer()
        )

    @Bean
    fun userEventKafkaTemplate(userEventProducerFactory:
                          DefaultKafkaProducerFactory<String, UserEvent>) =
        KafkaTemplate(userEventProducerFactory)
}