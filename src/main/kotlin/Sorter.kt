class Sorter {

//    store both a list of quotes that need to be
//    searched and the list of sorted quotes
    var quotesToSearch = mutableListOf<Quote>()
    var sortedQuotes = mutableListOf<Quote>()

    fun sortWord(word: String) {
        """Sort by word"""
        sortedQuotes = mutableListOf()

//        if the quote contains the word, add it to the list of sorted quotes
        quotesToSearch.forEach { quote -> if (quote.text.contains(word)) {
            sortedQuotes.add(quote)
        }
        }
    }

    fun sortSource(source: String) {
        """Sort by source"""
        sortedQuotes = mutableListOf()

//        if the source matches the source entered by the user,
//        add it to the list of sorted quotes
        for (quote in quotesToSearch) {
            if (quote.source == source) {
                sortedQuotes.add(quote)
            }
        }
    }

    fun sortKeyword(keyword: String) {
        """Sort by tag"""
        sortedQuotes = mutableListOf()

//        if one of the quote tags matches the one
//        entered by the user, add the quote to the list of
//        sorted quotes
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