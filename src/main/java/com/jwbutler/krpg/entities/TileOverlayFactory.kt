package com.jwbutler.krpg.entities

import com.jwbutler.rpglib.entities.TileOverlay
import com.jwbutler.rpglib.geometry.Coordinates
import java.awt.Color

object TileOverlayFactory
{
    fun playerOverlay(coordinates: Coordinates, selected: Boolean): TileOverlay
    {
        if (selected)
        {
            return TileOverlay(
                coordinates,
                Color(0, 255, 0, 255),
                Color(0, 255, 0, 128)
            )
        }
        else
        {
            return TileOverlay(
                coordinates,
                Color(0, 255, 0, 128),
                Color(0, 255, 0, 64)
            )
        }
    }

    fun enemyOverlay(coordinates: Coordinates, targeted: Boolean): TileOverlay
    {
        if (targeted)
        {
            return TileOverlay(
                coordinates,
                Color(255, 0, 0, 255),
                Color(255, 0, 0, 128)
            )
        }
        else
        {
            return TileOverlay(
                coordinates,
                Color(255, 0, 0, 128),
                Color(255, 0, 0, 64)
            )
        }
    }

    fun positionOverlay(coordinates: Coordinates, targeted: Boolean): TileOverlay
    {
        return TileOverlay(
            coordinates,
            Color(0, 255, 255, 255),
            Color(0, 255, 255, 128)
        )
    }
}