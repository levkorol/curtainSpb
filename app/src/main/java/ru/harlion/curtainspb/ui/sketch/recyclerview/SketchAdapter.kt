package ru.harlion.curtainspb.ui.sketch.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_sketch.view.*
import okhttp3.HttpUrl
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.base.CommonDialog
import ru.harlion.curtainspb.models.data.RequestPhoneBody
import ru.harlion.curtainspb.models.data.Template
import ru.harlion.curtainspb.repo.AuthPrefs
import ru.harlion.curtainspb.repo.data.DataRepository
import ru.harlion.curtainspb.ui.auth.registration.RegistrationFragment
import ru.harlion.curtainspb.utils.downloadAndSetImage
import ru.harlion.curtainspb.utils.replaceFragment

class SketchAdapter(
    private val imageClicked: Context.(HttpUrl) -> Unit,
    private val fragment: Fragment,
    private val endReached: () -> Unit,
) :
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

        if (position == templates.lastIndex) endReached()
    }

    override fun getItemCount(): Int = templates.size

    inner class SketchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val image: ImageView = itemView.image_img
        private val imageIsOpen: ImageView = itemView.image_isOpen

        fun bind(item: Template) {

            if (item.isOpen) imageIsOpen.visibility = View.GONE else imageIsOpen.visibility =
                View.VISIBLE

            image.downloadAndSetImage(item.imageUrl.toString())

            itemView.setOnClickListener { view ->
                val prefs = AuthPrefs(
                    view.context.getSharedPreferences(
                        "user",
                        AppCompatActivity.MODE_PRIVATE
                    )
                )
                if (item.isOpen) {
                    view.context.imageClicked(item.imageUrl)

                } else if (!item.isOpen && prefs.hasToken()) {
                    val dialog = CommonDialog(view.context)
                    dialog.setMessage("Для получения эскизов наш менеджер свяжется с вами по телефону, указанному при регистрации, если этого не произошло, позвоните нам \n +7(812) 213-35-55")
                    dialog.setPositiveButton("Отмена") {}
                    dialog.setNegativeButton("Перезвоните мне") {
                        DataRepository.requestPhone(
                            requestPhoneBody = RequestPhoneBody(
                                userId = prefs.getUserId().toString(),
                                name = prefs.getUserName(),
                                phone = prefs.getUserPhone(),
                                email = prefs.getUserEmail()
                            ),
                            {
                                Toast.makeText(
                                    it.context,
                                    "Наш менеджер скоро свяжется с вами",
                                    Toast.LENGTH_LONG
                                ).show()
                            },
                            {
                                Toast.makeText(
                                    view.context,
                                    it,
                                    Toast.LENGTH_LONG
                                ).show()
                            },
                            Throwable::printStackTrace
                        )
                    }
                    dialog.show()

                } else if (!item.isOpen && !prefs.hasToken()) {
                    val dialog = CommonDialog(view.context)
                    dialog.setMessage("Для получения эскизов зарегистрируйтесь")
                    dialog.setPositiveButton("Отмена") {}
                    dialog.setNegativeButton("Зарегистрироваться") {
                        fragment.replaceFragment(RegistrationFragment())
                    }
                    dialog.show()
                }
            }
        }
    }
}


