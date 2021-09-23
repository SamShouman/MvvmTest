package azka.tech.mvvmtest.Repositories

import azka.tech.mvvmtest.Data.Words

class MainActivityRepository {
    private val words = Words()

    // pretend getting data from database
    fun  generateWords(): String {
        return words.generateWords();
    }
}