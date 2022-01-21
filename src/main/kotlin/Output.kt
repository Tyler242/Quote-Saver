class Output {

    var scrollStart = 0
    var searchType: String = "View All"

    fun launchOptions() {
        """ Display the main menu options """
        println("\nQuote Saver")
        println("Select Option Below:")
        println("1. Search")
        println("2. View All")
        println("3. Add Quote")
        println("4. Quit")
        print("> ")
    }

    fun searchMenu() {
        println("\nSearch Options")
        println("1. Search by word")
        println("2. Search by source")
        println("3. Search by keywords")
        println("R to return")
        print("> ")
    }

    fun searchQuery() {
        println("\nEnter $searchType to search for: ")
        print("> ")
    }

    fun searchResult(quotes: MutableList<Quote>) {
        val lenQuotes = quotes.size
        if (scrollStart >= lenQuotes) {
            scrollStart = 0
        } else if (scrollStart < 0) {
            scrollStart = lenQuotes - 1
        }

        println("\n$lenQuotes results found for $searchType search:")
        for (x in 0..2) {
            if (x + scrollStart < lenQuotes) {
                println("\n${(x+1)}. Quote: ${quotes[x + scrollStart].text}")
                println("   Source: ${quotes[x + scrollStart].source}")
                println("   Keywords: ${quotes[x + scrollStart].keywords}")
            }
        }
        println("\n> to view next 3 quotes")
        println("< to view previous 3 quotes")
        println("R to return")
        print("> ")
    }

    fun creationError() {
        println("\nError with quote creation. Please try again.")
    }

    fun viewSingleQuote(quote:Quote) {
        println("\nQUOTE")
        println("Text: ${quote.text}")
        println("Source: ${quote.source}")
        println("Date Added: ${quote.date}")
        println("Keywords: ${quote.keywords}")
        println("\nE to Edit")
        println("D to Delete")
        println("R to Return")
        print("> ")
    }

    fun editQuoteMenu() {
        println("\nWhat would you like to edit?")
        println("1. Text")
        println("2. Source")
        println("3. Keywords")
        println("S to Save")
        print("> ")
    }

    fun editText(quote: Quote? = null) {
        println("\n")
        if (quote != null) {
            println(quote.text)
        }
        println("Enter text (Do not include '/', '>', or '{'):")
        print("> ")
    }
    fun editSource(quote: Quote? = null) {
        println("\n")
        if (quote != null) {
            println(quote.source)
        }
        println("Enter source (Do not include '/', '>', or '{'):")
        print("> ")
    }
    fun editKeywords(quote: Quote? = null) {
        println("\n")
        if (quote != null) {
            println("${quote.keywords}")
        }
        println("Enter keywords (Do not include '/', '>', or '{'):")
        println("Use ',' to separate different tags (eg tag 1,tag 2,tag 3)")
        print("> ")
    }

    fun deleteConfirmation() {
        println("\nAre you sure you want to delete this quote? (Y/N)")
        print("> ")
    }
}