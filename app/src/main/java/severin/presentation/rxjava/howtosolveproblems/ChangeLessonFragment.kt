package severin.presentation.rxjava.howtosolveproblems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_change_lesson.*
import severin.presentation.rxjava.howtosolveproblems.autocomplete.AutocompleteFragment

class ChangeLessonFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_change_lesson, container, false)

    override fun onStart() {
        super.onStart()

        button_go_to_lesson_autocomplete.setOnClickListener {
            fragmentManager!!.beginTransaction()
                .replace(R.id.container, AutocompleteFragment())
                .addToBackStack(null)
                .commit()
        }

    }
}
