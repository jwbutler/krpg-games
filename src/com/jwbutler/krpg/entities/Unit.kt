package com.jwbutler.krpg.entities

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.behavior.Command
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.sprites.Sprite

interface Unit : Entity
{
    fun getCommand(): Command
    fun getActivity(): Activity
    fun getDirection(): Direction
    fun getFrameNumber(): Int

    fun moveTo(coordinates: Coordinates)

    companion object
    {
        fun create(sprite: Sprite, coordinates: Coordinates): Unit = UnitImpl(sprite, coordinates)
    }
}

private class UnitImpl(private val sprite: Sprite, coordinates: Coordinates) : Unit
{
    private var command: Command
    private var activity: Activity
    private var direction: Direction
    private var frameNumber: Int

    init
    {
        GameState.getInstance().addUnit(this, coordinates)
        command = Command.STAY
        activity = Activity.STANDING
        direction = Direction.SE
        frameNumber = 1
    }

    override fun getSprite() = sprite
    override fun getCoordinates() = GameState.getInstance().getCoordinates(this)
    override fun getCommand() = command
    override fun getActivity() = activity
    override fun getDirection() = direction
    override fun getFrameNumber() = frameNumber

    override fun moveTo(coordinates: Coordinates)
    {
        val state = GameState.getInstance()
        require(state.getUnit(coordinates) == null)
        state.removeUnit(this)
        state.addUnit(this, coordinates)
    }
}