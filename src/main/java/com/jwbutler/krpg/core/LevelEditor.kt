package com.jwbutler.krpg.core

import com.jwbutler.krpg.entities.Tile
import com.jwbutler.krpg.entities.objects.GameObject
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import java.awt.event.MouseAdapter

class LevelEditor
{
    fun getMouseListener(): MouseAdapter
    {
        return object : MouseAdapter()
        {
        }
    }
}