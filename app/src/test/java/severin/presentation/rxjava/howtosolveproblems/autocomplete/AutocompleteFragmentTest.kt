package severin.presentation.rxjava.howtosolveproblems.autocomplete

import org.junit.Assert.*
import org.junit.Test

class AutocompleteFragmentTest {

    @Test
    fun scratchPaper() {

        assertEquals(
            listOf("", "h", "i", ""),
            "hi".split("")
        )

        val textChunks = listOf("hi", "I'm", "Severin")

        val mapped: List<List<String>> = textChunks.map { it.split("") }
        val flatMapped: List<String>   = textChunks.flatMap { it.split("") }

        // T -> R
        println("Mapped: $mapped")
        // T -> Iterable<R>
        println("FlatMapped: $flatMapped")
    }
}
