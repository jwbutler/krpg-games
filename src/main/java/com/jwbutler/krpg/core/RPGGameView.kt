package com.jwbutler.krpg.core

import com.jwbutler.rpglib.geometry.Dimensions
import com.jwbutler.rpglib.core.GameView

class RPGGameView : GameView
{
    override val tileDimensions = Dimensions(24, 12)
    override val gameDimensions = Dimensions(320, 180)
    override val initialWindowDimensions = Dimensions(1280, 720)
}