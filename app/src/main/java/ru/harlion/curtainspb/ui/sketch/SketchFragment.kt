package ru.harlion.curtainspb.ui.sketch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.harlion.curtainspb.R

class SketchFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scetch, container, false)
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