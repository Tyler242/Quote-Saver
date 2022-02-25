/**
 * This class will hold test code for
 * the QuoteManager class
 */
class Tests {
    private val inService = Input()
    private val quoteManager = QuoteManager()

    fun quoteCreation() {
        /**
         * Test the creation of database entries
         */
//        get the new quote
        println("Enter a new quote")
        print("> ")
        val text = inService.inService()

        println("Enter the source")
        print("> ")
        val source = inService.inService()

//        get the tags
        val tags = mutableListOf<String>()

        var done = false
        while (!done) {
            println("Current tags: $tags")
            println("Would you like to add a new tag? (Y)")
            print("> ")
            val input = inService.inService()
            if (input != null) {
                if (input.uppercase() != "Y") {
                    done = true
                } else {
                    println("\nEnter a new tag:")
                    print("> ")
                    val tag = inService.inService()
                    if (tag != null) {
                        if (tag in tags) {
                            println("Tag already entered")
                        } else {
                            tags.add(tag)
                        }
                    }
                }
            }
            println()
        }

//        add the quote to the database
        if (text != null && source != null) {
            quoteManager.createQuote(text, source, tags)
        } else {
            error("text or source is null")
        }
    }

    fun getAll() {
        /**
         * Test reading all the quotes from the database
         */
        println("Read all quotes from database\n")
        quoteManager.findAll()
        quoteManager.quoteList.forEach {
            println("Quote id: ${it.id}")
            println("\"${it.text}\"")
            println(it.source)
            println(it.tags.toString())
            println()
        }
        println("\nAll quotes read")
    }

    fun deleteQuote() {
        /**
         * Test the removeQuote() method
         */
        getAll()
        println("Enter quote id to delete the quote")
        print("> ")
        val id = inService.inService()
        if (id != null) {
            val quote = quoteManager.quoteList.filter { it.id.toString() == id }
            if (quote.isEmpty()) {
                error("No quote matches that id")
            } else if (quote.size > 1) {
                error("More than one quote found with that id")
            } else {
                quoteManager.removeQuote(quote[0])
            }
        }
        getAll()
    }
}