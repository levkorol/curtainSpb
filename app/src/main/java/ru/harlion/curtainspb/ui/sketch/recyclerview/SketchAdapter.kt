package ru.harlion.curtainspb.ui.sketch.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_sketch.view.*
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.models.data.Template
import ru.harlion.curtainspb.utils.downloadAndSetImage

class SketchAdapter :
    RecyclerView.Adapter<SketchAdapter.SketchHolder>() {

    var templates: List<Template> = listOf()
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
        val item = templates[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = templates.size

    class SketchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageCv: CardView = itemView.imageView_sketch
        private val image: ImageView = itemView.image_img

        fun bind(item: Template) {

            image.downloadAndSetImage(item.imageUrl.toString())

            itemView.setOnClickListener {
                //todo  взять картинку по айди
            }
        }
    }
}


