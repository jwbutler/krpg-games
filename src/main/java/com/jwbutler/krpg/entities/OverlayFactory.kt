package com.jwbutler.krpg.entities

import com.jwbutler.krpg.geometry.Coordinates
import java.awt.Color

object OverlayFactory
{
    fun createPlayerOverlay(coordinates: Coordinates, selected: Boolean): Overlay
    {
        return Overlay(coordinates, Color(0, 255, 0, 192), Color(0, 255, 0, 32))
    }

    fun createEnemyoverlay(coordinates: Coordinates, targeted: Boolean): Overlay
    {
        return Overlay(coordinates, Color(255, 0, 0, 192), Color(255, 0, 0, 32))
    }

    fun createPositionOverlay(coordinates: Coordinates, targeted: Boolean): Overlay
    {
        return Overlay(coordinates, Color(0, 255, 255, 192), Color(0, 255, 255, 32))
    }
}