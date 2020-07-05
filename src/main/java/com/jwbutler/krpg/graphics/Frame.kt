package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction

data class Frame(val activity: Activity, val direction: Direction, val key: String)