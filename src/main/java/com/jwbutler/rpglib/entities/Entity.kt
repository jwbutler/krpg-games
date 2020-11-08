package com.jwbutler.rpglib.entities

import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.graphics.Renderable
import com.jwbutler.rpglib.graphics.sprites.Sprite

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

    fun update() {}
    fun render(): Renderable
}