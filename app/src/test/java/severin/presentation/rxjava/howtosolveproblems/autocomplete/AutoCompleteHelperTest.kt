package severin.presentation.rxjava.howtosolveproblems.autocomplete

import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.util.concurrent.TimeUnit

class AutoCompleteHelperTest {

    @MockK private lateinit var hintApi: HintApi
    @MockK private lateinit var showProgress: (Boolean) -> Unit
    private lateinit var helper: AutoCompleteHelper
    private lateinit var source: Subject<String>
    private lateinit var testObserver: TestObserver<List<String>>

    companion object {
        private val testScheduler = TestScheduler()

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            RxJavaPlugins.setInitComputationSchedulerHandler { testScheduler }
            RxJavaPlugins.setInitIoSchedulerHandler { testScheduler }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { testScheduler }
        }
    }

    @Before
    fun setup() {
        testScheduler.advanceTimeTo(0, TimeUnit.MILLISECONDS)
        MockKAnnotations.init(this)
        helper = AutoCompleteHelper(hintApi)
        source = PublishSubject.create()
        testObserver = helper.getAutocomplete(source, showProgress).test()
        every { showProgress(any()) } just runs
    }

    @Test
    fun `WHEN a follow up response is faster THEN only the most recent response should be shown`() {
        val firstResponse = Observable.timer(2, TimeUnit.SECONDS).map { listOf("first") }
        val secondResponse = Observable.timer(1, TimeUnit.SECONDS).map { listOf("second") }

        every { hintApi.getHints(any()) } answers { firstResponse } andThen { secondResponse }

        source.onNext("hello")
        advancePastDebounce()
        source.onNext("hello ")
        advancePastDebounce()

        testScheduler.advanceTimeBy(3, TimeUnit.SECONDS)

        verify(exactly = 2) { hintApi.getHints(any()) }
        assertEquals(listOf("second"), testObserver.values().last())
    }

    @Test
    fun `WHEN values change quickly THEN only one request should be made`() {
        every { hintApi.getHints(any()) } answers { Observable.empty() }

        source.onNext("h")
        testScheduler.advanceTimeBy(50, TimeUnit.MILLISECONDS)
        source.onNext("he")
        testScheduler.advanceTimeBy(50, TimeUnit.MILLISECONDS)
        source.onNext("hel")
        testScheduler.advanceTimeBy(50, TimeUnit.MILLISECONDS)
        source.onNext("hell")
        testScheduler.advanceTimeBy(50, TimeUnit.MILLISECONDS)
        source.onNext("hello")
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        verify(exactly = 1) { hintApi.getHints(any()) }
    }

    @Test
    fun `WHEN text is very short THEN no calls should be made`() {
        every { hintApi.getHints(any()) } answers { Observable.empty() }

        source.onNext("")
        advancePastDebounce()
        source.onNext("h")
        advancePastDebounce()
        source.onNext("he")
        advancePastDebounce()
        source.onNext("h")
        advancePastDebounce()
        source.onNext("")
        advancePastDebounce()

        verify(exactly = 0) { hintApi.getHints(any()) }

        source.onNext("")
        advancePastDebounce()
        source.onNext("h")
        advancePastDebounce()
        source.onNext("he")
        advancePastDebounce()
        source.onNext("hel")
        advancePastDebounce()
        source.onNext("hell")
        advancePastDebounce()
        source.onNext("hello")
        advancePastDebounce()

        verify(exactly = 3) { hintApi.getHints(any()) }
    }

    @Test
    fun `WHEN a request is in progress THEN showProgress should be true`() {
        every { hintApi.getHints(any()) } answers { Observable.timer(1, TimeUnit.SECONDS).map { listOf<String>() } }

        source.onNext("hello")
        advancePastDebounce()

        verify(exactly = 1) { showProgress(true) }
        verify(exactly = 0) { showProgress(false) }

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        verify(exactly = 1) { showProgress(true) }
        verify(exactly = 1) { showProgress(false) }
    }

    private fun advancePastDebounce() {
        testScheduler.advanceTimeBy(250, TimeUnit.MILLISECONDS)
    }
}
