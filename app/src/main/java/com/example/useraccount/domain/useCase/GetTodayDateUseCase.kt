package com.example.useraccount.domain.useCase

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetTodayDateUseCase @Inject constructor() {
    operator fun invoke() = LocalDate.now().format(DateTimeFormatter.ISO_DATE).toString()
}
