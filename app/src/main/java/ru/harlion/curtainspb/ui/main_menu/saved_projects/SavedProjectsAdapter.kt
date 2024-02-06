package ru.harlion.curtainspb.ui.main_menu.saved_projects

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import okhttp3.HttpUrl
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.base.CommonDialog
import ru.harlion.curtainspb.models.data.SavedProject
import ru.harlion.curtainspb.utils.downloadAndSetImage

class SavedProjectsAdapter(
    private val imageClicked: Context.(HttpUrl) -> Unit
) : RecyclerView.Adapter<SavedProjectsAdapter.SavedProjectHolder>() {

    var savedProjects: List<SavedProject> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedProjectHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_saved_projects, parent, false)
        return SavedProjectHolder(view)
    }

    override fun onBindViewHolder(holder: SavedProjectHolder, position: Int) {
        val item = savedProjects[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = savedProjects.size

    inner class SavedProjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val image: ImageView = itemView.findViewById(R.id.saved_project_image)

        fun bind(item: SavedProject) {
            image.downloadAndSetImage(item.imageUrl.toString())

            itemView.setOnClickListener {
                val dialog = CommonDialog(it.context)
                dialog.setMessage("Переместить в конструктор этот проект?")
                dialog.setNegativeButton("Да") { view ->
                    view.context.imageClicked(item.imageUrl)
                }
                dialog.setPositiveButton("Отмена") {}
                dialog.show()
            }
        }
    }
}