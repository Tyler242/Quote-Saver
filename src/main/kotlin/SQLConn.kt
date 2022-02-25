import java.sql.*

class SQLConn {
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

    fun readAllQuotes() {
        /**
         * Read all the quotes in from the database
         */
        var stmt: Statement? = null
        var resultset: ResultSet? = null
        val queryString = "SELECT * FROM quotetable;"

        try {
            stmt = conn.createStatement()
            resultset = stmt!!.executeQuery(queryString)

            if (stmt.execute(queryString)) {
                resultset = stmt.resultSet
            }

            while (resultset!!.next()) {
//                create quote instances from each row in the db
                
            }

        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }
}