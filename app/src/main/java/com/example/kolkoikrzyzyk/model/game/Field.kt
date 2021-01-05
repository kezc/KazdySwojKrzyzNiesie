package com.example.kolkoikrzyzyk.model.game

class Field(val x: Int, val y: Int, val z: Int) {
    var type = FieldType.Empty
        private set
    var value = 0
        private set

    fun placeCross(): Boolean {
        if (type == FieldType.Empty) {
            type = FieldType.Cross
            value = 1
            return true
        }
        return false
    }

    fun placeNought(): Boolean {
        if (type == FieldType.Empty) {
            type = FieldType.Nought
            value = -1
            return true
        }
        return false
    }

    fun placeEmpty() {
        type = FieldType.Empty
        value = 0
    }

    override fun toString(): String {
        return "${
            when (type) {
                FieldType.Nought -> "O"
                FieldType.Empty -> " "
                FieldType.Cross -> "X"
            }
        } $x $y $z"
    }
}