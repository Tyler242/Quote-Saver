class Input {

//    if q is pressed we will quit the program
    var isDone = false

    fun inService(): String? {
        """Get input from the user"""

        val input = readLine()

//        if the input is the letter Q, set the program to Quit
        if (input == "Q" || input == "q") {
            isDone = true
        }

        return input
    }
}