class Director {
    private val outputService = Output()
    private val inputService = Input()
    private val fileService = FileIO()
    private val sortService = Sorter()
    private var quotes = fileService.readQuoteFile()

    fun start() {
        programLoop()
        fileService.writeQuoteFile(quotes)
    }

    private fun programLoop() {
        while (!inputService.isDone) {
            outputService.launchOptions()
            when (inputService.inService()) {
                "1" -> {
                    sortService.quotesToSearch = quotes
                    search()
                }
                "2" -> {
                    outputService.searchType = "View All"
                    sortService.sortedQuotes = quotes
                    searchResultControl()
                }
                "3" -> addQuote()
                "Q", "q" -> inputService.isDone = true
                else -> programLoop()
            }
        }
    }

    private fun search() {
        outputService.searchMenu()
        when (inputService.inService()) {
            "1" -> {
                outputService.searchType = "Word"
                searchWord()
            }
            "2" -> {
                outputService.searchType = "Source"
                searchSource()
            }
            "3" -> {
                outputService.searchType = "Keyword"
                searchKeyword()

            }
            "R", "r" -> return
            else -> search()
        }
    }

    private fun searchWord() {
        outputService.searchQuery()
        val word = inputService.inService()
        if (word != null) {
            sortService.sortWord(word)
            searchResultControl()
        } else {
            outputService.creationError()
        }
    }

    private fun searchSource() {
        outputService.searchQuery()
        val source = inputService.inService()
        if (source != null) {
            sortService.sortSource(source)
            searchResultControl()
        } else {
            outputService.creationError()
        }
    }

    private fun searchKeyword() {
        outputService.searchQuery()
        val keywordToSearch = inputService.inService()
        if (keywordToSearch != null) {
            sortService.sortKeyword(keywordToSearch)
            searchResultControl()
        } else {
            outputService.creationError()
        }
    }

    private fun searchResultControl() {
        outputService.searchResult(sortService.sortedQuotes)
        when (inputService.inService()) {
            "1" -> viewQuote(0)
            "2" -> viewQuote(1)
            "3" -> viewQuote(2)
            ">" -> {
                outputService.scrollStart += 3
                searchResultControl()
            }
            "<" -> {
                outputService.scrollStart -= 3
                searchResultControl()
            }
            "R", "r" -> {
                outputService.scrollStart = 0
                return
            }
            else -> searchResultControl()
        }
    }

    private fun addQuote() {

        outputService.editText()
        val text = inputService.inService()

        outputService.editSource()
        val source = inputService.inService()

        outputService.editKeywords()
        val keywordsString = inputService.inService()
        val keywords: List<String> = keywordsString?.split(",") ?: listOf()

        if (text == null || source == null) {
            outputService.creationError()
            return
        }

        quotes.add(Quote(text, source, 12345, keywords))
    }

    private fun viewQuote(index: Int) {
        """Control the single quote menu"""
        outputService.viewSingleQuote(sortService.sortedQuotes[index + outputService.scrollStart])
        when (inputService.inService()) {
            "E", "e" -> editQuote(index + outputService.scrollStart)
            "D", "d" -> deleteQuote(index + outputService.scrollStart)
            "R", "r" -> searchResultControl()
            else -> viewQuote(index)
        }
    }

    private fun editQuote(index: Int) {
        var doneEdit = false
        val quote = sortService.sortedQuotes[index]
//        this is the index of the quote being
//        edited in the main quotes list
        val quoteMainIndex = quotes.indexOf(quote)

        var newText = ""
        var newSource = ""
        var newKeywords: List<String> = listOf()

        while (!doneEdit) {
            outputService.editQuoteMenu()
            when (inputService.inService()) {
                "1" -> newText = getQuoteText(quote)
                "2" -> newSource = getQuoteSource(quote)
                "3" -> newKeywords = getQuoteTags(quote)
                "S", "s" -> doneEdit = true
            }
        }

        val updatedQuote = quote.copy(
            text = if (newText == "") quote.text else newText,
            source = if (newSource == "") quote.source else newSource,
            keywords = newKeywords.ifEmpty { quote.keywords }
        )

        quotes[quoteMainIndex] = updatedQuote
    }

    private fun getQuoteText(quote: Quote): String {
        outputService.editText(quote)
        val newText = inputService.inService()
        if (newText == null) {
            outputService.creationError()
            return ""
        }
        return newText
    }

    private fun getQuoteSource(quote: Quote): String {
        outputService.editSource(quote)
        val newSource = inputService.inService()
        if (newSource == null) {
            outputService.creationError()
            return ""
        }
        return newSource
    }

    private fun getQuoteTags(quote: Quote): List<String> {
        outputService.editKeywords(quote)
        val newKeywords = inputService.inService()
        if (newKeywords == null) {
            outputService.creationError()
            return listOf()
        }
        return newKeywords.split(',')
    }

    private fun deleteQuote(index: Int) {
        outputService.deleteConfirmation()
        val input = inputService.inService()
        if (input == "Y" || input == "y") {
            sortService.sortedQuotes.removeAt(index)
        }
        return
    }
}