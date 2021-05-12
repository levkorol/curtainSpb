package ru.harlion.curtainspb.utils

import java.io.Closeable

class Pager<T>(
    private val pageSize: Int,
    private val load: Pager<T>.(page: Int, pageSize: Int) -> Closeable,
    private val loaded: (List<T>) -> Unit,
) {
    private val items = ArrayList<T>()
    private var nextPage = 1
    private var currentCall: Closeable? = null

    fun get() {
        if (items.isNotEmpty()) loaded.invoke(items)
        else load()
    }
    fun load() {
        if (currentCall == null && nextPage > 0) {
            currentCall = load.invoke(this, nextPage, pageSize)
        }
    }
    fun loaded(items: List<T>?) {
        if (items != null) {
            this.items += items
            loaded.invoke(this.items)
            if (items.size < pageSize) nextPage = -1 else nextPage++
        }
        currentCall = null
    }
    fun dispose() {
        currentCall?.close()
        currentCall = null
    }
}
