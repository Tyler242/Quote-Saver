import java.time.LocalDate

class Director {

//    create objects of the classes we will use
    private val quoteManager = QuoteManager()
    private val outputService = Output()
    private val inputService = Input()


    fun start() {
        /**
         * begins the program
         */

        programLoop()
    }

    private fun programLoop() {
        """Controls the main program loop"""

        while (!inputService.isDone) {

//            display main menu options
            outputService.launchOptions()

//            input control
            when (inputService.inService()) {
//                search option
                "1" -> {
                    search()
                }
//                view all option
                "2" -> {
                    outputService.searchType = "View All"
                    quoteManager.findAll()
                    searchResultControl()
                }
//                add quote option
                "3" -> addQuote()
//                quit option
                "Q", "q" -> inputService.isDone = true
//                anything else will rerun this function
                else -> programLoop()
            }
        }
    }

    private fun search() {
        """Controls the search menu"""

//        display the search menu
        outputService.searchMenu()

//        input control
        when (inputService.inService()) {
//            search by word option
            "1" -> {
                outputService.searchType = "Word"
                searchWord()
            }
//            search by source option
            "2" -> {
                outputService.searchType = "Source"
                searchSource()
            }
//            search by keyword option
            "3" -> {
                outputService.searchType = "Keyword"
//                searchKeyword()
                println("Search by tag currently not implemented")

            }
//            return to the previous menu
            "R", "r" -> return
//            anything else will rerun this function
            else -> search()
        }
    }

    private fun searchWord() {
        """Search by word"""
//        display the search prompt
        outputService.searchQuery()

//        get input and validate it
        val word = inputService.inService()
        if (word != null) {

//            sort the list of quotes by word
            quoteManager.searchWord(word)
            searchResultControl()
        } else {
            outputService.creationError()
        }
    }

    private fun searchSource() {
        """Search by source"""
//        display search prompt
        outputService.searchQuery()

//        get input and validate it
        val source = inputService.inService()
        if (source != null) {

//            sort the list of quotes by source
            quoteManager.searchSource(source)
            searchResultControl()
        } else {
            outputService.creationError()
        }
    }

    private fun searchKeyword() {
        """Search by keyword"""

//        display search prompt
        outputService.searchQuery()

//        get input and validate it
        val keywordToSearch = inputService.inService()
        if (keywordToSearch != null) {

//            sort the list of quotes by keyword
//            TODO: (Implement query function for all quotes with tag)
            searchResultControl()
        } else {
            outputService.creationError()
        }
    }

    private fun searchResultControl() {
        """Control the search result menu"""
//        display the search result menu
        outputService.searchResult(quoteManager.quoteList)

//        input control
        when (inputService.inService()) {
//            view the specified quote
            "1" -> viewQuote(0)
            "2" -> viewQuote(1)
            "3" -> viewQuote(2)
//            view next or previous three quotes
            ">" -> {
                outputService.scrollStart += 3
                searchResultControl()
            }
            "<" -> {
                outputService.scrollStart -= 3
                searchResultControl()
            }
//            return to the main menu
            "R", "r" -> {
                outputService.scrollStart = 0
                return
            }
//            anything else will rerun this function
            else -> searchResultControl()
        }
    }

    private fun addQuote() {
        """Add a quote"""

//        get the quote text
        outputService.editText()
        val text = inputService.inService()

//        get the source
        outputService.editSource()
        val source = inputService.inService()

//        get the tags
        val tags = mutableListOf<String>()

        var done = false
        while (!done) {
            println("Current tags: $tags")
            println("Would you like to add a new tag? (Y/N)")
            print("> ")
            val input = inputService.inService()
            if (input != null) {
                if (input.uppercase() != "Y") {
                    done = true
                } else {
                    println("\nEnter a new tag:")
                    print("> ")
                    val tag = inputService.inService()
                    if (tag != null) {
                        if (tag in tags) {
                            println("Tag already entered")
                        } else {
                            tags.add(tag)
                        }
                    }
                }
            }
        }
//        simple validation of text and source
        if (text == null || source == null) {
            outputService.creationError()
            return
        }
//        create new quote
        quoteManager.createQuote(text, source, tags)
    }

    private fun viewQuote(index: Int) {
        """Control the single quote menu"""
//        display the quote
        outputService.viewSingleQuote(quoteManager.quoteList[index + outputService.scrollStart])

//        input control
        when (inputService.inService()) {
//            edit the quote
            "E", "e" -> {
                editQuote(index + outputService.scrollStart)
                outputService.scrollStart = 0
            }
//            delete the quote
            "D", "d" -> {
                deleteQuote(index + outputService.scrollStart)
                outputService.scrollStart = 0
            }
//            return to the search menu
            "R", "r" -> searchResultControl()
//            anything else will rerun this function
            else -> viewQuote(index)
        }
    }

    private fun editQuote(index: Int) {
        """Edit a quote"""

//        get the quote
        val quote = quoteManager.quoteList[index]

//        variables for holding the new data
        var newText = ""
        var newSource = ""
        var newKeywords: List<String> = listOf()

//        conditional for determining when the user is done editing the quote
        var doneEdit = false

//        while loop enables the user to edit what they want instead of everything
        while (!doneEdit) {
//            display the edit menu
            outputService.editQuoteMenu()

//            input control
            when (inputService.inService()) {
//                edit the text
                "1" -> newText = getQuoteText(quote)
//                edit the source
                "2" -> newSource = getQuoteSource(quote)
//                edit the keywords
                "3" -> newKeywords = getQuoteTags(quote)
//                done editing
                "S", "s" -> doneEdit = true
            }
        }

//        create a copy of the quote and update the variables if they were changed
        val updatedQuote = quote.copy(
            id = quote.id,
            text = if (newText == "") quote.text else newText,
            source = if (newSource == "") quote.source else newSource,
            tags = newKeywords.ifEmpty { quote.tags }
        )

//        replace the old quote with an updated copy
        quoteManager.quoteList[index] = updatedQuote
        quoteManager.updateQuote(updatedQuote)
    }

    private fun getQuoteText(quote: Quote): String {
        """Get the quote text"""
//        display the edit text menu
        outputService.editText(quote)
//        get and validate input
        val newText = inputService.inService()
        if (newText == null) {
            outputService.creationError()
            return ""
        }
        return newText
    }

    private fun getQuoteSource(quote: Quote): String {
        """Get the quote source"""
//        display the edit source menu
        outputService.editSource(quote)
//        get and validate input
        val newSource = inputService.inService()
        if (newSource == null) {
            outputService.creationError()
            return ""
        }
        return newSource
    }

    private fun getQuoteTags(quote: Quote): List<String> {
        """Get the quote keywords"""
//        display the edit keywords menu
        outputService.editTags(listOf())
//        get and validate input
        val newKeywords = inputService.inService()
        if (newKeywords == null) {
            outputService.creationError()
            return listOf()
        }
        return newKeywords.split(',')
    }

    private fun deleteQuote(index: Int) {
        """Delete a quote"""

//        get the quote object to remove
        val quote = quoteManager.quoteList[index]

//        confirm the user wants to delete the quote
        outputService.deleteConfirmation()
        val input = inputService.inService()
        if (input == "Y" || input == "y") {
//            delete the quote from the main list of quotes
            quoteManager.quoteList.remove(quote)
            quoteManager.removeQuote(quote)
        }
        return
    }
}