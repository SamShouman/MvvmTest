package azka.tech.mvvmtest.Data

import java.util.*
import kotlin.random.Random.Default.nextInt

/*
*   SUPPOSING THAT THIS CLASS IS ACTING LIKE A DATABASE
* */
class Words {
    private val words: List<String> = Arrays.asList(
        "aim",
        "air",
        "aircraft",
        "airline",
        "airport",
        "album",
        "alcohol",
        "alive",
        "all",
        "alliance",
        "allow",
        "ally",
        "almost",
        "alone",
        "prompt",
        "proof",
        "proper",
        "properly",
        "property",
        "proportion",
        "proposal",
        "propose",
        "proposed",
        "regional",
        "register",
        "regular",
        "regularly",
        "regulate",
        "regulation",
        "seven",
        "several",
        "severe",
        "tell",
        "sam",
        "parag",
        "azka",
        "tech",
        "hello",
        "world",
        "actually",
        "wonderland"
    )

    fun generateWords(): String {
        /* generate random string using the words found in  {words} lst
        *  @return string containing random selected words
        * */
        var randomlySelectedItems: MutableList<String> = mutableListOf()
        var randomIndex: Int

        for (i in 1..words.size) {
            randomIndex = nextInt(words.size)
            randomlySelectedItems.add(words[randomIndex])
        }
        return randomlySelectedItems.joinToString(separator = " ")
    }
}
