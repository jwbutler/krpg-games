package com.jwbutler.rpglib.sounds

import com.jwbutler.rpglib.core.SingletonHolder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineEvent

class SoundPlayer
(
    private val filenamePattern: (String) -> String
)
{
    fun playSoundAsync(filename: String) = GlobalScope.launch { _playSound(filename) }

    private fun _playSound(filename: String)
    {
        val clip = AudioSystem.getClip()
        val fullFilename = filenamePattern(filename)
        val url = _getFileURL(fullFilename)
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

    companion object : SingletonHolder<SoundPlayer>()

    private fun _getFileURL(filename: String): URL
    {
        // I hate resources
        return object {}::class.java.getResource(filename) ?: error("Could not open filename ${filename}")
    }
}