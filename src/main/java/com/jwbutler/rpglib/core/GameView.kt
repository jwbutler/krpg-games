package com.jwbutler.rpglib.core

import com.jwbutler.rpglib.entities.TileOverlay
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.geometry.Dimensions
import com.jwbutler.rpglib.graphics.Renderable

interface GameView
{
    val tileDimensions: Dimensions
    val gameDimensions: Dimensions
    val initialWindowDimensions: Dimensions

    fun getTileOverlays(): Map<Coordinates, TileOverlay>
    fun getUIOverlays(): Collection<Renderable>
    fun getCameraCoordinates(): Coordinates

    companion object : SingletonHolder<GameView>()
}