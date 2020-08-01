package com.jwbutler.krpg.players

import com.jwbutler.krpg.entities.Overlay

abstract class HumanPlayer : AbstractPlayer()
{
    abstract fun getOverlays(): Collection<Overlay>
}