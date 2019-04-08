package severin.presentation.rxjava.howtosolveproblems

import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class AutoCompleteActivityTest {


    @Test
    fun t() {
        RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.trampoline() }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://suggestqueries.google.com")
            .client(OkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val hintApi = HintApi(retrofit.create(HintApiRaw::class.java))
        hintApi.getHints("my query!")
            .subscribe { println(it) }
    }

}