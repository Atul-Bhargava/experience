package com.user.experience.mongo

import com.user.experience.model.UserExperience
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface UserExperienceRepo : MongoRepository<UserExperience, String> {
    @Query("{'createdDate' : { \$gt : ?0,  \$lt : ?1} }")
    fun findByCreatedDateBetween(startDate: LocalDate, endDate: LocalDate): List<UserExperience>
}