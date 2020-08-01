package com.jwbutler.krpg.players

import com.jwbutler.krpg.entities.Overlay
import com.jwbutler.krpg.geometry.Coordinates

abstract class HumanPlayer : AbstractPlayer()
{
    abstract fun getOverlays(): Map<Coordinates, Overlay>
}