/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package severin.presentation.rxjava.howtosolveproblems

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

@Suppress("UNCHECKED_CAST")
class HintApi(private val hintApiRaw: HintApiRaw) {
    // We're using http://suggestqueries.google.com to avoid having to do the set up to access the normal
    // search API. This API returns values in a format that doesn't convert well to Kotlin
    // (e.g., `["query", ["my", "query", "results"]]` ).  Instead of putting in effort, we just extract
    // and cast.
    fun getHints(query: String): Observable<List<String>> = hintApiRaw.getHints(query)
        .map { it[1] as List<String> }
}

interface HintApiRaw {
    @GET("complete/search?client=firefox")
    fun getHints(@Query("q") query: String): Observable<List<Any>>
    // http://suggestqueries.google.com/complete/search?client=firefox&q=superstar
}