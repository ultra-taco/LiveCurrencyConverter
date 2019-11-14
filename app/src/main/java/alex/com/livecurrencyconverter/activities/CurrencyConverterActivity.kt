package alex.com.livecurrencyconverter.activities

import alex.com.livecurrencyconverter.R
import alex.com.livecurrencyconverter.app.LiveCurrencyConverterApp
import alex.com.livecurrencyconverter.activities.components.api.CurrencyAPIClient
import alex.com.livecurrencyconverter.databinding.ActivityMainBinding
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
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

        //Create view
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Create & Set viewmodel
        viewModel = CurrencyConverterViewModel(currencyAPIClient)
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
}
