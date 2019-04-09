/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package severin.presentation.rxjava.howtosolveproblems.autocomplete

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class HintApi {
    fun getHints(query: String): Observable<SearchResults> {
        println("Getting suggestions for query: $query")
        val delay = ((.5 + Math.random()) * 1_000).toLong()
        return Observable.timer(delay, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .map { listOf("query: $query", "some", "suggested", "searches") }
    }
}
