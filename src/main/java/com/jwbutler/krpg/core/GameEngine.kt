package com.jwbutler.krpg.core

import com.jwbutler.krpg.core.GameEngine.UnitData
import com.jwbutler.krpg.entities.equipment.Equipment
import com.jwbutler.krpg.entities.equipment.EquipmentSlot
import com.jwbutler.krpg.entities.units.Unit
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
    fun startGame(initialLevel: Level, units: List<UnitData>)
    fun pause()
    fun unpause()
    fun togglePause()
    fun isPaused(): Boolean
    fun doLoop()

    companion object : SingletonHolder<GameEngine>(::GameEngineImpl)

    data class UnitData
    (
        val unit: Unit,
        val equipment: Map<EquipmentSlot, Equipment>
    )
}

private class GameEngineImpl : GameEngine
{
    private var isPaused = false
    private var initialized = false

    override fun startGame(initialLevel: Level, units: List<UnitData>)
    {
        check(!initialized)
        val state = GameState.getInstance()
        state.loadLevel(initialLevel)

        for (unitData in units)
        {
            val (unit, equipmentMap) = unitData
            state.addPlayerUnit(unit, state.getHumanPlayer(), initialLevel.startPosition, equipmentMap)
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
        for (entity in state.getEntities())
        {
            // Unfortunately we have to do this superfluous-looking check here
            // because the entity could have been killed during a previous update() method
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

        _checkVictory()
    }

    private fun _checkVictory()
    {
        val state = GameState.getInstance()
        val level = state.getLevel()
        if (level.isComplete())
        {
            level.onComplete()
        }
    }
}