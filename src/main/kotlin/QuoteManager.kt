import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

class QuoteManager {
    var quoteList = mutableListOf<Quote>()
    lateinit var quoteToEdit: Quote
    private lateinit var conn : Connection

    init {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quotes", "root", "dev_app_56252")
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun findAll() {
//        clear the quoteList attribute if it isn't already empty
        if (quoteList.isNotEmpty()) {
            quoteList = mutableListOf()
        }
//        prepare a statement
        val findAllQuery = conn.prepareStatement("SELECT * FROM quotetable;")
//        execute the statement
        val result = findAllQuery.executeQuery()

//        convert the data into quote objects
        while(result.next()) {
//            get the id
            val id = result.getInt("id")
//            get the quote
            val quoteText = result.getString("quote")

//            get the source
            val source = result.getString("source")

//            for each quote, get the associated keywords
            val tags = getTags(id)

//            create a Quote object
            val quote = Quote(id, quoteText, source, tags)

//            add the quote to our data structure
            quoteList.add(quote)
        }
//        println(quoteList)
    }

    private fun getTags(quoteId: Int): MutableList<String> {
        val tags = mutableListOf<String>()

        val query = conn.prepareStatement("SELECT * FROM tags WHERE quoteid = ?;")
        val result: ResultSet

        with(query) {
            setInt(1, quoteId)
            result = query.executeQuery()
        }

        while (result.next()) {
            val tag = result.getString("tag")
            tags.add(tag)
        }

        return tags
    }

    fun findQuote(quote: Quote) {
//        prepare statement
        val query = conn.prepareStatement("SELECT * FROM quotetable WHERE quote = ? AND source = ?;")
        var result: ResultSet
//        execute query
        with(query) {
            setString(1, quote.text)
            setString(2, quote.source)
            result = query.executeQuery()
            close()
        }

//        get the quote data and create a Quote object
        while(result.next()) {
            val id = result.getInt("id")
            val quoteText = result.getString("quote")
            val source = result.getString("source")
            val tags = getTags(id)

//            create Quote object
            quoteToEdit = Quote(id, quoteText, source, tags)

        }
    }

//    fun search(searchType: String, searchKey: String) {
////        logic for handling different searches
//
//        when(searchType) {
//            "source" -> {
//                searchSource(searchKey)
//            }
//            "word" -> {
//                searchWord(searchKey)
//            }
//            else -> {
//                println("Invalid search type.")
//            }
//        }
//    }

    fun searchSource(sourceKey: String) {
//        find the rows with the matching source

//        clear the previously sorted quotes
        if (quoteList.isNotEmpty()) {
            quoteList = mutableListOf()
        }
//        prepare statement
        val query = conn.prepareStatement("SELECT * FROM quotetable WHERE source = ?;")
        var result: ResultSet

        with(query) {
            setString(1, sourceKey)
            result = query.executeQuery()
        }
//        execute statement

        while (result.next()) {
//            create the Quote object
            val id = result.getInt("id")
            val quoteText = result.getString("quote")
            val source = result.getString("source")
            val tags = getTags(id)

            quoteList.add(Quote(id, quoteText, source, tags))

        }
    }

    fun searchWord(wordKey: String) {
//        clear any previous search results
        if (quoteList.isNotEmpty()) {
            quoteList = mutableListOf()
        }
        val word = "%$wordKey%"
//        prepare statement
        val query = conn.prepareStatement("SELECT * FROM quotetable WHERE quote LIKE ?;")
        var result: ResultSet

        with(query) {
            setString(1, word)
            result = query.executeQuery()
        }

        while (result.next()) {
//            create the Quote object
            val id = result.getInt("id")
            val quoteText = result.getString("quote")
            val source = result.getString("source")
            val tags = getTags(id)

            quoteList.add(Quote(id, quoteText, source, tags))
        }
    }

    fun createQuote(text: String, source: String, tags: List<String>) {
//        create the text and source
        val query = conn.prepareStatement("INSERT INTO quotetable(quote, source) VALUES (?,?);")
        with(query) {
            setString(1, text)
            setString(2, source)
            executeUpdate()
            close()
        }
//        get the quote id for the quote we just wrote to the db
        val queryForId = conn.prepareStatement("SELECT id FROM quotetable WHERE quote = ? AND source = ?")
        val result: ResultSet
        with(queryForId) {
            setString(1, text)
            setString(2, source)
            result = executeQuery()
        }

        var quoteId: Int? = null
        while (result.next()) {
            quoteId = result.getInt("id")
        }

        if (quoteId == null) {
            error("quote id is null")
        }

//        write the tags to the database
        addTags(quoteId, tags)
    }

    fun removeQuote(quote: Quote) {
//        remove tags
        var query = conn.prepareStatement("DELETE FROM tags WHERE quoteid = ?;")
        with(query) {
            setInt(1, quote.id)
            executeUpdate()
            close()
        }
//        remove quote
        query = conn.prepareStatement("DELETE FROM quotetable WHERE id = ?;")
        with(query) {
            setInt(1, quote.id)
            executeUpdate()
            close()
        }
    }

    fun updateQuote(quote: Quote) {
        val query = conn.prepareStatement("UPDATE quotetable " +
                "SET quote = ?, source = ? " +
                "WHERE id = ?")

        with (query) {
            setString(1, quote.text)
            setString(2, quote.source)
            setInt(3, quote.id)
            executeUpdate()
            close()
        }
        addTags(quote.id, quote.tags)
    }

    fun removeTag(quoteId: Int, tag: String) {
        val query = conn.prepareStatement("DELETE FROM tags WHERE quoteid = ? AND tag = ?;")
        with(query) {
            setInt(1,quoteId)
            setString(2, tag)
            executeUpdate()
            close()
        }
    }

    fun addTags(quoteId: Int, tags: List<String>) {

        var queryString = "INSERT INTO tags(quoteid, tag) VALUES "
        tags.forEach {
            queryString += "($quoteId,\"$it\"),"
        }

//        remove the final comma
        queryString = queryString.dropLast(1)

        val queryAddTags = conn.prepareStatement(queryString)
        with(queryAddTags) {
            executeUpdate()
            close()
        }
    }
}