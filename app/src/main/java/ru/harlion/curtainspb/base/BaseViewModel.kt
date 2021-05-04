package ru.harlion.curtainspb.base

import androidx.lifecycle.ViewModel
import ru.harlion.curtainspb.repo.data.DataRepository
import java.io.Closeable
import java.io.File

open class BaseViewModel : ViewModel() {

    private val closeables = ArrayList<Closeable>()

    protected operator fun Closeable.unaryPlus() {
        closeables += this
    }

    fun sendOnBdPick(file: File) {
        +DataRepository.templates(
            {

            }, {

            }
        )
    }
}