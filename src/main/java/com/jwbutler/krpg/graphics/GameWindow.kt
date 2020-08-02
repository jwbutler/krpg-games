package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.core.SingletonHolder
import com.jwbutler.krpg.geometry.GAME_HEIGHT
import com.jwbutler.krpg.geometry.GAME_WIDTH
import com.jwbutler.krpg.geometry.INITIAL_WINDOW_HEIGHT
import com.jwbutler.krpg.geometry.INITIAL_WINDOW_WIDTH
import com.jwbutler.krpg.graphics.ui.Window
import javax.swing.WindowConstants

class GameWindow : Window(GAME_WIDTH, GAME_HEIGHT, INITIAL_WINDOW_WIDTH, INITIAL_WINDOW_HEIGHT)
{
    init
    {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    }

    companion object : SingletonHolder<GameWindow>(::GameWindow)
}