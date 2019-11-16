package alex.com.livecurrencyconverter.currency.activity

import alex.com.livecurrencyconverter.R
import alex.com.livecurrencyconverter.app.LiveCurrencyConverterApp
import alex.com.livecurrencyconverter.currency.activity.list.CurrencyAdapter
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

    @Inject
    lateinit var currencyAPIClient: CurrencyAPIClient

    private val adapter = CurrencyAdapter()
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
        binding.viewModel = viewModel
    }

    private fun onViewCreated() {
        // Setup list
        binding.contentMain.recyclerView.adapter = adapter
        binding.contentMain.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.contentMain.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getData()
        }

        // Setup spinner
        binding.contentMain.currencySelectorSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }
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
        //@@TODO: Loading spinner?
        viewModel.finishedLoadingEvent.observe(this, Observer {
            binding.contentMain.swipeRefreshLayout.isRefreshing = false
        })
        viewModel.currencyData.observe(this, Observer { data ->
            adapter.setData(data)
        })
    }
}
