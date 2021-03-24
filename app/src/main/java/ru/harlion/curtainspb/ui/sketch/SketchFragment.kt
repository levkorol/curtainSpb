package ru.harlion.curtainspb.ui.sketch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_scetch.*
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.ui.save_project.SaveProjectFragment
import ru.harlion.curtainspb.utils.replaceFragment

class SketchFragment : Fragment(), SketchPresenter.View {

    private lateinit var presenter: SketchPresenter
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = SketchPresenter()
    }
    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scetch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attach(this)

        cardView_save_project.setOnClickListener {
            presenter.onSaveClicked()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detach()
    }

    override fun goToSave() {
        replaceFragment(SaveProjectFragment())
    }

    companion object {

//        fun newInstance(image: Bitmap): SketchFragment {
//            val fragment = SketchFragment()
//            val arguments = Bundle()
//            arguments.apply {
//
//            }
//        }
    }

}