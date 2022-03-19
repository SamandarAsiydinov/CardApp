package uz.context.cardapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.context.cardapplication.R
import uz.context.cardapplication.database.CardEntity
import uz.context.cardapplication.util.Utils

class MyAdapter2(
    var context: Context,
    private val lists: List<CardEntity>

) : RecyclerView.Adapter<MyAdapter2.ViewHolder>() {

    var text: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val card = lists[position]

        holder.apply {

            cardDate.text = "${card.cardDate1}/${card.cardDate2}"
            cardHolder.text = card.cardHolder
            cardNumber.text = card.cardNumber

            cardView.setCardBackgroundColor(ContextCompat.getColor(context, Utils.randomColor()))

            cardTextName.text = Utils.randomString()
        }
    }

    override fun getItemCount(): Int = lists.size
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardNumber: TextView = view.findViewById(R.id.textCardNumbers)
        val cardHolder: TextView = view.findViewById(R.id.textCardHolderName)
        val cardDate: TextView = view.findViewById(R.id.textCardDate)
        val cardView: CardView = view.findViewById(R.id.cardView)
        val cardTextName: TextView = view.findViewById(R.id.textCardName)
    }
}