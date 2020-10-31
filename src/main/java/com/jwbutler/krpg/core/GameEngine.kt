package com.jwbutler.krpg.core

import com.jwbutler.krpg.core.GameEngine.UnitData
import com.jwbutler.rpglib.core.BoundSingletonHolder
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.entities.equipment.Equipment
import com.jwbutler.rpglib.entities.equipment.EquipmentSlot
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.graphics.GameRenderer
import com.jwbutler.rpglib.graphics.GameWindow
import com.jwbutler.rpglib.levels.Level
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val FRAME_INTERVAL = 125L // 8 FPS
private const val RENDER_INTERVAL = 20L // 50 FPS

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

    companion object : BoundSingletonHolder<GameEngine>(::GameEngineImpl)

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
                synchronized(GameState.getInstance())
                {
                    doLoop()
                }
                delay(FRAME_INTERVAL)
            }
        }

        GlobalScope.launch {
            while (true)
            {
                synchronized(GameState.getInstance())
                {
                    GameRenderer.getInstance().render()
                    GameWindow.getInstance().render()
                }
                delay(RENDER_INTERVAL)
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
        val state = GameState.getInstance()

        if (!isPaused)
        {
            _checkVictory()
        }

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

    private fun _checkVictory()
    {
        val state = GameState.getInstance()
        val level = state.getLevel()
        if (level.checkVictory())
        {
            level.doVictory()
        }
    }
}