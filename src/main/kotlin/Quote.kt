import java.time.LocalDate

data class Quote(
//    Quote class for each quote object
    var id: Int,
    var text: String,
    var source: String,
    var tags: List<String>
    )