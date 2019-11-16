package alex.com.livecurrencyconverter.currency.activity.list

import alex.com.livecurrencyconverter.currency.entity.CurrencyEntity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Alex Doub on 11/14/2019.
 */

class CurrencyAdapter : RecyclerView.Adapter<CurrencyItemViewHolder>() {

    private var items: List<CurrencyItemViewModel> = emptyList()

    fun setData(entities: List<CurrencyEntity>) {
        items = entities.map { CurrencyItemViewModel((it)) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyItemViewHolder {
        return CurrencyItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CurrencyItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}