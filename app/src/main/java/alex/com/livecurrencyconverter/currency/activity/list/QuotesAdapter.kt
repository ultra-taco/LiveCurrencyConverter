package alex.com.livecurrencyconverter.currency.activity.list

import alex.com.livecurrencyconverter.currency.database.quote.QuoteEntity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Alex Doub on 11/14/2019.
 */

class QuotesAdapter : RecyclerView.Adapter<QuoteItemViewHolder>() {

    private var items: List<QuoteItemViewModel>? = null

    fun setData(entities: List<QuoteEntity>?) {
        items = entities?.map { QuoteItemViewModel((it)) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteItemViewHolder {
        return QuoteItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: QuoteItemViewHolder, position: Int) {
        holder.bind(items!![position])
    }

    override fun getItemCount() = items?.size ?: 0
}