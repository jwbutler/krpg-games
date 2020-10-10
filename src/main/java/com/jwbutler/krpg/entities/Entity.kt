package com.jwbutler.krpg.entities

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.Renderable
import com.jwbutler.krpg.graphics.sprites.Sprite

interface Entity
{
    val sprite: Sprite
    /**
     * Implementations of this will differ:
     * Units, tiles, etc. will delegate this to [GameState],
     * while dependent entities such as equipment will delegate to their owner
     */
    fun getCoordinates(): Coordinates
    fun getX() = getCoordinates().x
    fun getY() = getCoordinates().y
    fun isBlocking(): Boolean = false
    /**
     * Syntactic sugar for [GameState.containsEntity].
     */
    fun exists(): Boolean = false

    /**
     * Increment the entity's frame number, and fire any on-complete handlers if an activity has ended.
     */
    fun endFrame() {}
    /**
     * If the entity is starting a new activity, perform any related logic (e.g. setting activity state).
     */
    fun startFrame() {}
    fun render(): Renderable
}