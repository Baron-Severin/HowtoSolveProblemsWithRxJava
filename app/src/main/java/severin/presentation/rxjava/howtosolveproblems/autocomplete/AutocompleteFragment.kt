package severin.presentation.rxjava.howtosolveproblems.autocomplete

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_autocomplete.*
import severin.presentation.rxjava.howtosolveproblems.R
import java.util.concurrent.TimeUnit

typealias SearchResults = List<String>

class AutocompleteFragment : Fragment() {

    val hintApi = HintApi()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_autocomplete, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        autocomplete_go_button.setOnClickListener { fragmentManager!!.popBackStack() }
        autocompleteEditText.threshold = 0
    }

    @SuppressLint("CheckResult")
    override fun onStart() {
        super.onStart()
        /*
         *  REQUIREMENTS
         *  Make network call for suggestions when user input changes
         *  Update autocomplete
         */
    }

    /**
     * @return an Observable that will emit the EditText text as a String
     * every time they change
     */
    private fun EditText.textEvents(): Observable<String> = RxTextView.afterTextChangeEvents(this)
        .map { it.editable().toString() }

    private fun setSuggestions(suggestions: SearchResults) {
        val adapter = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_dropdown_item_1line,
            suggestions
        )
        autocompleteEditText.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }

    private fun showProgress(show: Boolean) {
        progress_bar.post { progress_bar.isVisible = show }
    }
}
