class Input {
    var isDone = false
    fun inService(): String? {
        val input = readLine()
        if (input == "Q" || input == "q") {
            isDone = true
        }
        return input
    }
}