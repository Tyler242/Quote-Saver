import java.io.File
import java.io.InputStream
import java.io.PrintWriter

class FileIO {
    fun readQuoteFile(): MutableList<Quote> {
        """Creates quote objects from the contents of the quote file"""
//        define the input stream
        val inputStream: InputStream = File("src/assets/quoteFile").inputStream()
//        read the contents of the file
        val inputString = inputStream.bufferedReader().use { it.readText() }

//        split the contents into different quote blocks
        val list = inputString.split("{")
//        filter out any empty strings from the list
        val listQuoteBlocks = list.filter { it.isNotEmpty() }
//        list that will contain the quote objects
        val listOfQuotes = mutableListOf<Quote>()
//        create and append each quote object to the list
        listQuoteBlocks.forEach {
            listOfQuotes.add(configQuotes(it)) }

        return listOfQuotes
    }

    fun writeQuoteFile(quotes:MutableList<Quote>) {
        """Write the quote objects to the quote file"""

//        format the text to be written
        val formattedText = configText(quotes)

//        write the text to the file
        val writer = PrintWriter("src/assets/quoteFile")
        writer.append(formattedText)
        writer.close()
    }

    private fun configQuotes(quoteBlocks: String): Quote {
        """Create each quote object from each quote block"""
//        create list of the individual quote properties
        val keyValues = quoteBlocks.split('/')

        val quote = keyValues[0].split(">")[1]
        val source = keyValues[1].split(">")[1]
        val date = keyValues[2].split(">")[1]
        val keywords = keyValues[3].split(">")[1]
        val keywordsList = keywords.split(",")

        return Quote(quote, source, date.toInt(), keywordsList)
    }

    private fun configText(quotes: MutableList<Quote>): String {
        """Format the quote objects for writing them to the file"""

//        store the text to be written
        var textToWrite = ""

//        format the quote objects to text
        quotes.forEach {
            var quoteString = "{Text>${it.text}/Source>${it.source}/Date>${it.date}/Keyword>"
            it.keywords.forEach { tag -> quoteString += if (tag != it.keywords[it.keywords.size - 1]) "$tag," else "$tag" }
            textToWrite += quoteString
        }
        return textToWrite

    }
}