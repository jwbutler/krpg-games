package com.jwbutler.krpg.sounds

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineEvent

private const val SOUNDS_FOLDER = "sounds"

object SoundPlayer
{
    fun playSoundAsync(filename: String) = GlobalScope.launch { _playSound(filename) }

    private fun _playSound(filename: String)
    {
        val clip = AudioSystem.getClip()
        val fullFilename = "/${SOUNDS_FOLDER}/${filename}"
        val url = SoundPlayer.javaClass.getResource(fullFilename)
        val stream = AudioSystem.getAudioInputStream(url)
        clip.open(stream)
        clip.loop(0)

        clip.addLineListener { e: LineEvent ->
            if (e.getType() === LineEvent.Type.STOP)
            {
                clip.close()
                stream.close()
            }
        }
    }
}