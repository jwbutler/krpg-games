package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction

/**
 * Slight misnomer - this is used for anything with frames corresponding to (activity/direction) pairs,
 * i.e. this includes equipment in addition to units.
 */
data class UnitFrame(val activity: Activity, val direction: Direction, val key: String)