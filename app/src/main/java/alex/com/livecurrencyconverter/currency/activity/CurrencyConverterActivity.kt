package alex.com.livecurrencyconverter.currency.activity

import alex.com.livecurrencyconverter.R
import alex.com.livecurrencyconverter.app.LiveCurrencyConverterApp
import alex.com.livecurrencyconverter.currency.api.CurrencyAPIClient
import alex.com.livecurrencyconverter.databinding.ActivityMainBinding
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class CurrencyConverterActivity : AppCompatActivity() {

    @Inject
    lateinit var currencyAPIClient: CurrencyAPIClient

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: CurrencyConverterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inject Components
        LiveCurrencyConverterApp.currencyConverterComponent.inject(this)

        //Create itemView
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)

        binding.contentMain.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getData()
        }

        // Create & Set viewmodel
        setupViewModel()
        binding.viewModel = viewModel
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
        viewModel =
            CurrencyConverterViewModel(
                currencyAPIClient
            )
        viewModel.showErrorEvent.observe(this, Observer{ message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        })
        viewModel.statusText.observe(this, Observer { message ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        })
        viewModel.finishedLoadingEvent.observe(this, Observer {
            binding.contentMain.swipeRefreshLayout.isRefreshing = false
        })
    }
}
