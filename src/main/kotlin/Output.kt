class Output {

//    control the scroller
    var scrollStart = 0

//    used to specify the search being performed
    var searchType: String = "View All"

    fun launchOptions() {
        """ Display the main menu options """
        println("\nQuote Saver")
        println("Select Option Below:")
        println("1. Search")
        println("2. View All")
        println("3. Add Quote")
        println("Q  to Quit")
        print("> ")
    }

    fun searchMenu() {
        """Display the search menu options"""
        println("\nSearch Options")
        println("1. Search by word")
        println("2. Search by source")
        println("3. Search by keywords")
        println("R to return")
        print("> ")
    }

    fun searchQuery() {
        """Display the search prompt"""
        println("\nEnter $searchType to search for: ")
        print("> ")
    }

    fun searchResult(quotes: MutableList<Quote>) {
        """Display the search result menu"""

        val lenQuotes = quotes.size

//        control how the scroller wraps
        if (scrollStart >= lenQuotes) {
            scrollStart = 0
        } else if (scrollStart < 0) {
            scrollStart = lenQuotes - 1
        }

        println("\n$lenQuotes results found for $searchType search:")

//        display three quotes at a time
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
        """Display input error"""
        println("\nError with quote creation. Please try again.")
    }

    fun viewSingleQuote(quote:Quote) {
        """Display single quote menu"""
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
        """Display edit menu"""
        println("\nWhat would you like to edit?")
        println("1. Text")
        println("2. Source")
        println("3. Keywords")
        println("S to Save")
        print("> ")
    }

    fun editText(quote: Quote? = null) {
        """Display text edit prompt"""
        println("\n")
//        if we are editing an existing quote,
//        display the current text for the quote
        if (quote != null) {
            println(quote.text)
        }
        println("Enter text (Do not include '/', '>', or '{'):")
        print("> ")
    }

    fun editSource(quote: Quote? = null) {
        """Display source edit prompt"""
        println("\n")
//        if we are editing an existing quote,
//        display the current source for the quote
        if (quote != null) {
            println(quote.source)
        }
        println("Enter source (Do not include '/', '>', or '{'):")
        print("> ")
    }

    fun editKeywords(quote: Quote? = null) {
        """Display keyword edit prompt"""
        println("\n")
//        if we are editing an existing quote,
//        display the current keywords for the quote
        if (quote != null) {
            println("${quote.keywords}")
        }
        println("Enter keywords (Do not include '/', '>', or '{'):")
        println("Use ',' to separate different tags (eg tag 1,tag 2,tag 3)")
        print("> ")
    }

    fun deleteConfirmation() {
        """Display delete confirmation prompt"""
        println("\nAre you sure you want to delete this quote? (Y/N)")
        print("> ")
    }

//    fun deleteSuccessful() {
//        """Display a successful deletion message"""
//        println("\nDeleted successfully.")
//    }
//
//    fun deleteError() {
//        """Display a successful deletion message"""
//        println("\nCould not delete quote.")
//    }
}