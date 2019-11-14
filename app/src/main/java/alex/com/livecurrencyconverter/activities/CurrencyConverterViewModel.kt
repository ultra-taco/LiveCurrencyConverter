package alex.com.livecurrencyconverter.activities

import alex.com.livecurrencyconverter.activities.components.api.CurrencyAPIClient
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * Created by Alex Doub on 11/13/2019.
 */

class CurrencyConverterViewModel(val currencyAPIClient: CurrencyAPIClient) : ViewModel() {

    // Observables
    val currencyListObservable = BehaviorSubject.create<Map<String, Double>>()
    val statusText = ObservableField<String>()

    // Events
    val showErrorEvent = PublishSubject.create<String>()

    private val disposables = CompositeDisposable()

    init {
        //@@TODO: Load data from storage. API goes thru DB

        getData()
    }

    fun clearData() {
        //@@TODO: Provide DB, clear data
    }

    fun getData() {

        statusText.set("Fetching data from server... ")

        val fetchDataSubscription = currencyAPIClient.getLiveCurrencyList()
            .subscribeBy(
                onNext = { apiResponse ->
                    currencyListObservable.onNext(apiResponse.quotes)
                    statusText.set("Loaded data from ${apiResponse.timestamp}")
                },
                onError = {
                    showErrorEvent.onNext("An API Error has occurred")
                })
        disposables.add(fetchDataSubscription)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}