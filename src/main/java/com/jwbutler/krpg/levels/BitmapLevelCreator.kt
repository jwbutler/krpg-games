package com.jwbutler.krpg.levels

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.objects.GameObject
import com.jwbutler.krpg.entities.objects.Tree
import com.jwbutler.krpg.entities.objects.Wall
import com.jwbutler.krpg.entities.tiles.Tile
import com.jwbutler.krpg.entities.tiles.TileType
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.entities.units.ZombieUnit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.images.Colors
import com.jwbutler.krpg.graphics.images.ImageLoader
import com.jwbutler.krpg.players.Player
import java.awt.Color

// TODO: Lots of hardcoded color values here.
// In the future these will be specified individually for each level.
private object LevelColors
{
    val DIRT = Colors.BROWN
    val ENEMY = Colors.ORANGE
    val GRASS = Colors.GREEN
    val STONE = Colors.DARK_GRAY
    val TREE = Colors.DARK_GREEN
    val WALL = Colors.LIGHT_GRAY
}

object BitmapLevelCreator
{
    fun loadLevel(filename: String): Level
    {
        val fullFilename = "levels/${filename}"
        val image = ImageLoader.getInstance().loadImage(fullFilename)

        val tiles = mutableMapOf<Coordinates, Tile>()
        val objects = mutableMapOf<Coordinates, Collection<GameObject>>()
        val units = mutableMapOf<Player, MutableMap<Coordinates, Unit>>()

        for (y in 0 until image.height)
        {
            for (x in 0 until image.width)
            {
                val coordinates = Coordinates(x, y)
                val color = Color(image.getRGB(x, y))
                val tileType = _getTile(color)
                if (tileType != null)
                {
                    tiles[coordinates] = Tile(tileType, coordinates)
                }

                val `object` = _getObject(color)
                if (`object` != null)
                {
                    objects[coordinates] = listOf(`object`)
                }

                val playerUnitPair = _getUnit(color)
                if (playerUnitPair != null)
                {
                    val (player, unit) = playerUnitPair
                    units.computeIfAbsent(player) { mutableMapOf() }[coordinates] = unit
                }
            }
        }

        return Level(
            tiles = tiles,
            units = listOf(),
            objects = mapOf(),
            startPosition = Coordinates(0, 0),
            victoryCondition = VictoryCondition.NONE
        )
    }

    private fun _getTile(color: Color): TileType?
    {
        return when (color)
        {
            LevelColors.GRASS -> TileType.GRASS
            LevelColors.DIRT  -> TileType.DIRT
            LevelColors.STONE -> TileType.STONE
            else              -> TileType.GRASS
        }
    }

    private fun _getObject(color: Color): GameObject?
    {
        return when (color)
        {
            LevelColors.TREE  -> Tree()
            LevelColors.WALL  -> Wall()
            else              -> null
        }
    }

    private fun _getUnit(color: Color): Pair<Player, Unit>?
    {
        val enemyPlayer = GameState.getInstance()
            .getPlayers()
            .filterNot(Player::isHuman)
            .first()

        return when (color)
        {
            LevelColors.ENEMY -> enemyPlayer to ZombieUnit(50)
            else              -> null
        }
    }
}