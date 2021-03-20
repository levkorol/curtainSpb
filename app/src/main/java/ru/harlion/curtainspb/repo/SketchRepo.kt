package ru.harlion.curtainspb.repo

import ru.harlion.curtainspb.models.Sketch

class SketchRepo {

    val list: List<Sketch> = emptyList()

    fun getListSketch(): List<Sketch> {
        return listOf(
            Sketch(
                1,
                "",
                true
            ),
            Sketch(
                2,
                "",
                true
            ),
            Sketch(
                3,
                "",
                true
            ),
            Sketch(
                4,
                "",
                true
            )
        )
    }

    fun findSketchById(id: Int): Sketch? {
        return getListSketch().firstOrNull { id == it.id }
    }
}