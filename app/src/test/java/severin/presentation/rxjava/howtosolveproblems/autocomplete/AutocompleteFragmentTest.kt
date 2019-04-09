package severin.presentation.rxjava.howtosolveproblems.autocomplete

import org.junit.Assert.*
import org.junit.Test

class AutocompleteFragmentTest {

    @Test
    fun scratchPaper() {

        val list = listOf("hi", "Bob")
        val split = list.flatMap { it.split("") }
        println(split)
    }


}