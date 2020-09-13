package com.jwbutler.krpg.graphics

/**
 * This is a wrapper for any components that go into a frame filename.
 * So FrameKey("standing", "N", "1") -> "standing_N_1"
 */
class FrameKey(private vararg val keys: Any)
{
    override fun toString(): String = keys.joinToString("_")
}