package ru.harlion.curtainspb

import org.junit.Test

import org.junit.Assert.*
import ru.harlion.curtainspb.ui.sketch.SketchPresenter

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun `sketch presenter triggers save screen`() {
        val presenter = SketchPresenter()
        var timesCalled = 0
        presenter.attach(object : SketchPresenter.View {
            override fun goToSave() {
                timesCalled++
            }
        })

        presenter.onSaveClicked()
        assertEquals(1, timesCalled)
    }
}