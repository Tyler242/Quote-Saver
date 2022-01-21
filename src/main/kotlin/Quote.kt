import java.time.LocalDate

data class Quote(
//    Quote class for each quote object
    var text: String,
    var source: String,
    var date: LocalDate,
    var keywords: List<String>
    )