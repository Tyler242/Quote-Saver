import java.time.LocalDate

data class Quote(var text: String, var source: String, var date: LocalDate, var keywords: List<String>)