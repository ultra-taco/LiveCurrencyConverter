package alex.com.livecurrencyconverter.other

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Created by Alex Doub on 11/16/2019.
 */

@BindingAdapter("visible")
fun visibleOrGone(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}
