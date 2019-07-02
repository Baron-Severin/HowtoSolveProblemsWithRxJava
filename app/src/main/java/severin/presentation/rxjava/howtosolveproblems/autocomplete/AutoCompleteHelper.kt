/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package severin.presentation.rxjava.howtosolveproblems.autocomplete

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class AutoCompleteHelper(private val hintApi: HintApi) {

    fun getAutocomplete(source: Observable<String>, showProgress: (Boolean) -> Unit): Observable<List<String>> =
            source
                .filter { it.length >= 3 }
                .debounce(200, TimeUnit.MILLISECONDS)
                .doOnNext { showProgress(true) }
                .switchMap { query -> hintApi.getHints(query) }
                .doOnNext { showProgress(false) }

}
