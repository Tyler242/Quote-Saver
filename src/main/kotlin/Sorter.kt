class Sorter {

    var quotesToSearch = mutableListOf<Quote>()
    var sortedQuotes = mutableListOf<Quote>()

    fun sortWord(word: String) {
        sortedQuotes = mutableListOf<Quote>()
        quotesToSearch.forEach { quote -> if (quote.text.contains(word)) {
            sortedQuotes.add(quote)
        }
        }
    }

    fun sortSource(source: String) {
        sortedQuotes = mutableListOf<Quote>()
        for (quote in quotesToSearch) {
            if (quote.source == source) {
                sortedQuotes.add(quote)
            }
        }
    }

    fun sortKeyword(keyword: String) {
        sortedQuotes = mutableListOf<Quote>()
        for (quote in quotesToSearch) {
            quote.keywords.forEach { tag ->
                if (tag == keyword) {
                    sortedQuotes.add(quote)
                    return@forEach
                }
            }
        }
    }
}