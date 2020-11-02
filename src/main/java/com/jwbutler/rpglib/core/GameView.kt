package com.jwbutler.rpglib.core

import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.geometry.Dimensions

interface GameView
{
    val tileDimensions: Dimensions
    val gameDimensions: Dimensions
    val initialWindowDimensions: Dimensions

    companion object : SingletonHolder<GameView>()
}