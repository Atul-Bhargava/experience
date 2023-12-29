package com.user.experience.service

import com.user.experience.model.UserExperience
import com.user.experience.mongo.UserExperienceRepo
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class UserExperienceService (
 private val repo: UserExperienceRepo
    ) {

    // Date object should be used instead of String this will reduce the declaration of two variables
    fun getUserReportByDate( dateParam : LocalDate) : List<UserExperience> {
            return repo.findByCreatedDateBetween(dateParam, dateParam.plusDays(1))// same here
        // Response entity should be used in controllers rather than in exception block . You should define
        // Controller advise and throw the exception and use controller advice to propagate the error
    }

    fun saveUserExperience (userExperience: UserExperience) {
            repo.save(userExperience)
    }
}