class Sort {

    fun sortWord(quotes: MutableList<Quote>, word: String): MutableList<Quote> {
        var sortedQuotes = mutableListOf<Quote>()
        quotes.forEach { quote -> if (quote.text.contains(word)) {
            sortedQuotes.add(quote)
        }
        }
        return sortedQuotes
    }

    fun sortSource(quotes: MutableList<Quote>, source: String): MutableList<Quote> {
        var sortedQuotes = mutableListOf<Quote>()
        for (quote in quotes) {
            if (quote.source == source) {
                sortedQuotes.add(quote)
            }
        }
        return sortedQuotes
    }
}