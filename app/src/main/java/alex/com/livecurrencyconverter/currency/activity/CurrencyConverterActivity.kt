package alex.com.livecurrencyconverter.currency.activity

import alex.com.livecurrencyconverter.R
import alex.com.livecurrencyconverter.app.LiveCurrencyConverterApp
import alex.com.livecurrencyconverter.currency.activity.list.CurrencyAdapter
import alex.com.livecurrencyconverter.currency.activity.list.QuotesAdapter
import alex.com.livecurrencyconverter.currency.api.CurrencyAPIClient
import alex.com.livecurrencyconverter.databinding.ActivityMainBinding
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class CurrencyConverterActivity : AppCompatActivity() {

    companion object {
        private const val DESTINATION_CURRENCY_NONE_POSITION = 0
    }

    @Inject
    lateinit var currencyAPIClient: CurrencyAPIClient

    private val quotesAdapter = QuotesAdapter()
    private lateinit var sourceCurrencyAdapter: CurrencyAdapter
    private lateinit var destinationCurrencyAdapter: CurrencyAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CurrencyConverterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inject Components
        LiveCurrencyConverterApp.currencyConverterComponent.inject(this)

        // Create content view
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)
        onViewCreated()

        // Create & Set viewModel
        setupViewModel()
        binding.content.viewModel = viewModel
    }

    private fun onViewCreated() {
        // Setup list
        binding.content.recyclerView.adapter = quotesAdapter
        binding.content.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.content.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getQuotes()
        }

        // Setup source selector
        binding.content.sourceCurrencySelector.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedCurrency = viewModel.currenciesObservable.value!![position]
                    viewModel.setSourceCurrency(selectedCurrency)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }
        sourceCurrencyAdapter = CurrencyAdapter(this)
        binding.content.sourceCurrencySelector.adapter = sourceCurrencyAdapter

        // Setup Destination selector
        binding.content.destinationCurrencySelector.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedCurrency = if (position == DESTINATION_CURRENCY_NONE_POSITION) {
                        null
                    } else {
                        viewModel.currenciesObservable.value!![position - 1]
                    }
                    viewModel.setDestinationCurrency(selectedCurrency)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }
        destinationCurrencyAdapter = CurrencyAdapter(this)
        binding.content.destinationCurrencySelector.adapter = destinationCurrencyAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_database -> {
                viewModel.clearData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViewModel() {
        viewModel = CurrencyConverterViewModel(currencyAPIClient)
        viewModel.showErrorEvent.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        })
        viewModel.showSnackbarEvent.observe(this, Observer { message ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        })
        viewModel.isLoadingObservable.observe(this, Observer {
            binding.content.swipeRefreshLayout.isRefreshing = it
        })
        viewModel.quotesObservable.observe(this, Observer { data ->
            quotesAdapter.setData(data)
        })
        viewModel.currenciesObservable.observe(this, Observer { currencyList ->
            sourceCurrencyAdapter.clear()
            sourceCurrencyAdapter.addAll(currencyList)

            destinationCurrencyAdapter.clear()
            destinationCurrencyAdapter.addAll(currencyList)
            destinationCurrencyAdapter.insert("<All>", DESTINATION_CURRENCY_NONE_POSITION)
        })
    }
}