package com.jwbutler.krpg.core

import com.jwbutler.krpg.graphics.GameRenderer
import com.jwbutler.krpg.levels.Level
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val FRAME_INTERVAL = 83 // ~12 FPS

/**
 * This class is responsible for executing the main loop,
 * including rendering
 */
interface GameEngine
{
    fun startGame(initialLevel: Level, units: Collection<Level.UnitData>)
    fun pause()
    fun unpause()
    fun togglePause()
    fun isPaused(): Boolean
    fun doLoop()

    companion object : SingletonHolder<GameEngine>(::GameEngineImpl)
}

private class GameEngineImpl : GameEngine
{
    private var isPaused = false
    private var initialized = false

    override fun startGame(initialLevel: Level, units: Collection<Level.UnitData>)
    {
        check(!initialized)
        val state = GameState.getInstance()
        state.loadLevel(initialLevel)

        for (unitData in units)
        {
            val (unit, coordinates, player, equipmentMap) = unitData
            state.addUnit(unit, coordinates, player)
            for ((slot, equipment) in equipmentMap)
            {
                state.addEquipment(equipment, unit)
            }
        }

        GlobalScope.launch {
            while (true)
            {
                doLoop()
                delay(FRAME_INTERVAL.toLong())
            }
        }
        initialized = true
    }

    override fun pause()
    {
        isPaused = true
    }

    override fun unpause()
    {
        isPaused = false
    }

    override fun togglePause()
    {
        if (isPaused) unpause() else pause()
    }

    override fun isPaused() = isPaused

    override fun doLoop()
    {
        _update()
        _render()
        _afterRender()
    }

    private fun _update()
    {
        val state = GameState.getInstance()
        val entities = state.getEntities()
        for (entity in entities)
        {
            // Unfortunately we have to do this superfluous-looking check here
            // because they could die (or kill each other) during update() methods
            if (entity.exists())
            {
                entity.update()
            }
        }
    }

    private fun _render()
    {
        GameRenderer.getInstance().render()
    }

    private fun _afterRender()
    {
        if (isPaused)
        {
            return
        }

        val state = GameState.getInstance()
        state.getEntities().forEach { it.afterRender() }
    }
}