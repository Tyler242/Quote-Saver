import java.time.LocalDate

class Director {

//    create objects of the classes we will use
    private val outputService = Output()
    private val inputService = Input()
    private val fileService = FileIO()
    private val sortService = Sorter()

//    read the quotes from the file
    private var quotes = fileService.readQuoteFile()

    fun start() {
        """begins the program"""

        programLoop()
        fileService.writeQuoteFile(quotes)
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
                    sortService.quotesToSearch = quotes
                    search()
                }
//                view all option
                "2" -> {
                    outputService.searchType = "View All"
                    sortService.sortedQuotes = quotes
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
                searchKeyword()

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
            sortService.sortWord(word)
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
            sortService.sortSource(source)
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
            sortService.sortKeyword(keywordToSearch)
            searchResultControl()
        } else {
            outputService.creationError()
        }
    }

    private fun searchResultControl() {
        """Control the search result menu"""
//        display the search result menu
        outputService.searchResult(sortService.sortedQuotes)

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

//        get the keywords
        outputService.editKeywords()
        val keywordsString = inputService.inService()
        val keywords: List<String> = keywordsString?.split(",") ?: listOf()

//        simple validation of text and source
        if (text == null || source == null) {
            outputService.creationError()
            return
        }

//        add a new quote object to the list
        quotes.add(Quote(text, source, LocalDate.now(), keywords))
    }

    private fun viewQuote(index: Int) {
        """Control the single quote menu"""
//        display the quote
        outputService.viewSingleQuote(sortService.sortedQuotes[index + outputService.scrollStart])

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
        val quote = sortService.sortedQuotes[index]

//        get the index of the quote within the main quotes array
        val quoteMainIndex = quotes.indexOf(quote)

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
            text = if (newText == "") quote.text else newText,
            source = if (newSource == "") quote.source else newSource,
            keywords = newKeywords.ifEmpty { quote.keywords }
        )

//        replace the old quote with an updated copy
        quotes[quoteMainIndex] = updatedQuote
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
        outputService.editKeywords(quote)
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
        val quote = sortService.sortedQuotes[index]

//        find the index of the quote within the quotes list
        val quoteMainIndex = quotes.indexOf(quote)

//        confirm the user wants to delete the quote
        outputService.deleteConfirmation()
        val input = inputService.inService()
        if (input == "Y" || input == "y") {
//            delete the quote from the main list of quotes
            quotes.remove(quote)
        }
        return
    }
}