class Director {
    private val outputService = Output()
    private val inputService = Input()
    private val fileService = FileIO()
    var quotes = fileService.readQuoteFile()

    fun start() {
        programLoop()
        fileService.writeQuoteFile(quotes)
    }

    private fun programLoop() {
        while (!inputService.isDone) {
            outputService.launchOptions()
            when (inputService.inService()) {
                "1" -> println("1")
                "2" -> searchResultControl()
                "3" -> addQuote()
                "Q", "q" -> inputService.isDone = true
            }
        }
    }

    private fun searchResultControl() {
        outputService.searchResult(quotes)
        when (inputService.inService()) {
            "1" -> viewQuote(0)
            "2" -> viewQuote(1)
            "3" -> viewQuote(2)
            ">" -> {outputService.scrollStart += 3
            searchResultControl()}
            "<" -> {outputService.scrollStart -= 3
            searchResultControl()}
            "R", "r" -> return
            else -> searchResultControl()
        }
    }

    private fun addQuote() {

        outputService.addText()
        val text = inputService.inService()

        outputService.addSource()
        val source = inputService.inService()

        outputService.addDate()
        val date = inputService.inService()

        outputService.addTags()
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
        outputService.viewSingleQuote(quotes[index + outputService.scrollStart])
        when (inputService.inService()) {
            "E", "e" -> editQuote(index + outputService.scrollStart)
            "D", "d" -> deleteQuote(index + outputService.scrollStart)
            "R", "r" -> searchResultControl()
            else -> viewQuote(index)
        }
    }

    private fun editQuote(index: Int) {
        var doneEdit = false
        val quote = quotes[index]

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

//        check the text, source, keywords, and then the different combos
        val updatedQuote = quote.copy(
            text = if (newText == "") quote.text else newText,
            source = if (newSource == "") quote.source else newSource,
            keywords = newKeywords.ifEmpty { quote.keywords }
        )

        quotes[index] = updatedQuote
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
            quotes.removeAt(index)
        }
        return
    }
}