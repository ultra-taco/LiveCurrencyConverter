package alex.com.livecurrencyconverter.currency.activity.list

import alex.com.livecurrencyconverter.databinding.ViewQuoteItemBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Alex Doub on 11/14/2019.
 */

class QuoteItemViewHolder(
    parent: ViewGroup,
    private val binding: ViewQuoteItemBinding = ViewQuoteItemBinding.inflate(
        LayoutInflater.from(
            parent.context
        ), parent, false
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: QuoteItemViewModel) {
        binding.viewModel = data
        binding.executePendingBindings()
    }
}