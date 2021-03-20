package ru.harlion.curtainspb.ui.sketch.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_sketch.view.*
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.models.Sketch

class SketchAdapter :
    RecyclerView.Adapter<SketchAdapter.SketchHolder>() {

    var sketch: List<Sketch> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SketchHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_sketch, parent, false)
        return SketchHolder(view)
    }

    override fun onBindViewHolder(holder: SketchHolder, position: Int) {
        val item = sketch[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = sketch.size

    class SketchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val image: CardView = itemView.imageView_sketch

        fun bind(item: Sketch) {
            image.setBackgroundResource(R.drawable.test_pic_big) //todo временно для теста
        }
    }
}


