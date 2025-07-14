import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice

object TTSManager {
    private const val PREFS_NAME = "tts_prefs"
    private const val VOICE_KEY = "selected_voice"
    private const val PITCH_KEY = "tts_pitch"
    private const val RATE_KEY = "tts_rate"

    private var tts: TextToSpeech? = null

    fun init(context: Context, onInit: (() -> Unit)? = null) {
        if (tts != null) {
            onInit?.invoke()
            return
        }

        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val savedVoiceName = prefs.getString(VOICE_KEY, null)
                val savedPitch = prefs.getFloat(PITCH_KEY, 1.0f)
                val savedRate = prefs.getFloat(RATE_KEY, 1.0f)

                val availableVoices = tts?.voices
                val matchedVoice = availableVoices?.find { it.name == savedVoiceName }
                if (matchedVoice != null) {
                    tts?.voice = matchedVoice
                }

                tts?.setPitch(savedPitch)
                tts?.setSpeechRate(savedRate)

                onInit?.invoke()
            }
        }
    }

    fun getTTS(): TextToSpeech? = tts

    fun setVoice(context: Context, voice: Voice) {
        tts?.voice = voice
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(VOICE_KEY, voice.name).apply()
    }

    fun setPitch(context: Context, pitch: Float) {
        tts?.setPitch(pitch)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putFloat(PITCH_KEY, pitch).apply()
    }

    fun setSpeechRate(context: Context, rate: Float) {
        tts?.setSpeechRate(rate)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putFloat(RATE_KEY, rate).apply()
    }

    fun getSavedPitch(context: Context): Float {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getFloat(PITCH_KEY, 1.0f)
    }

    fun getSavedSpeechRate(context: Context): Float {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getFloat(RATE_KEY, 1.0f)
    }

    fun getSavedVoice(context: Context, availableVoices: Collection<Voice>): Voice? {
        val savedVoiceName = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(VOICE_KEY, null)
        return availableVoices.find { it.name == savedVoiceName }
    }

    fun shutdown() {
        tts?.shutdown()
        tts = null
    }
}
