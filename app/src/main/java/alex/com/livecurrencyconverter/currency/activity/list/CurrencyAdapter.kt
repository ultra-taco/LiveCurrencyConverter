package alex.com.livecurrencyconverter.currency.activity.list

import android.content.Context
import android.widget.ArrayAdapter

class CurrencyAdapter(context: Context) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item) {
    init {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }
}