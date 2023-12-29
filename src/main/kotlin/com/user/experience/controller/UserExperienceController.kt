package com.user.experience.controller

import com.user.experience.model.UserExperience
import com.user.experience.service.UserExperienceService
import jakarta.validation.constraints.PastOrPresent
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class UserExperienceController(private val userExperienceService: UserExperienceService) {

    @GetMapping("/report/{date}")
    fun reportByDate(
        @PathVariable("date")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @PastOrPresent
        localDate: LocalDate
    ) : List<UserExperience>  // use the return object instead of Any as you are returning only a single entity
        = userExperienceService.getUserReportByDate(localDate)

}