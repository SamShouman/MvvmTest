package azka.tech.mvvmtest.ViewModels

import azka.tech.mvvmtest.Repositories.MainActivityRepository

/*
* THIS IS A VIEWMODEL CLASS WHICH REQUESTS DATA FROM THE REPOSITORY AND RETURNS IT TO THE VIEW OR CONTAINS THE LOGIC (LIKE IF/ELSE, LOOPS)
* */
class MainActivityViewModel{

    fun generateWords(): String {
        /* generate string containing random words
        *  @return string randomly generated
        * */
        val repository = MainActivityRepository()
        return repository.generateWords()
    }
}