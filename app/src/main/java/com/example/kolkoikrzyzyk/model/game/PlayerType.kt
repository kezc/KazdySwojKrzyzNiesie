package com.example.kolkoikrzyzyk.model.game

enum class PlayerType {
    Nought, Cross;

    fun getOther() = when (this) {
        Nought -> Cross
        Cross -> Nought
    }
}