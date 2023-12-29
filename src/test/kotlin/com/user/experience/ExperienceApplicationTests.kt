package com.user.experience

import com.user.experience.mongo.UserExperienceRepo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EmbeddedKafka(
	brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"],
	topics = ["test.user.events"],
	partitions = 1,
	controlledShutdown = true
)
@Testcontainers
@Import(TestConfig::class)
abstract class ExperienceApplicationTests {

	@Autowired
	lateinit var userExperienceRepo: UserExperienceRepo

	@Autowired
	lateinit var restTemplate: TestRestTemplate

	@AfterEach
	fun cleanUp() {
		userExperienceRepo.deleteAll()
	}

	companion object {
		@Container
		@JvmField
		val mongoDBContainer: MongoDBContainer = MongoDBContainer("mongo:latest")
			.withExposedPorts(27017)
			.withReuse(true)

		init {
			mongoDBContainer.start()
		}

		@DynamicPropertySource
		@JvmStatic
		fun mongoDbProperties(registry: DynamicPropertyRegistry) {
			registry.add("spring.data.mongodb.uri") { mongoDBContainer.replicaSetUrl }
		}
	}
}
