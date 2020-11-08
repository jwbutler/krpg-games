package com.jwbutler.rpglib.core

import com.jwbutler.rpglib.entities.TileOverlay
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.geometry.Dimensions
import com.jwbutler.rpglib.graphics.Renderable

/**
 * This is used to declare client-specific rendering options.
 *
 * This lets clients decide on rendering parameters, and also lets them specify
 * additional info for rendering the game (such as various overlays).
 *
 * This ends up being somewhat of a grab bag of functionality.
 */
interface GameView
{
    val tileDimensions: Dimensions
    val gameDimensions: Dimensions
    val initialWindowDimensions: Dimensions

    /**
     * TODO this is likely only used within [getRenderables()]
     */
    fun getTileOverlays(): Map<Coordinates, TileOverlay>
    fun getUIOverlays(): Collection<Renderable>
    fun getRenderables(): List<Renderable>
    fun getCameraCoordinates(): Coordinates

    companion object : SingletonHolder<GameView>()
}