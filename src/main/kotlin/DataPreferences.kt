import java.util.prefs.Preferences

object DataPreferences {
    private val prefs: Preferences = Preferences.userRoot().node("org.nllexeu.settings")

    // --- Write ---
    fun putInt(key: String, value: Int) = prefs.putInt(key, value)
    fun putLong(key: String, value: Long) = prefs.putLong(key, value)
    fun putBoolean(key: String, value: Boolean) = prefs.putBoolean(key, value)
    fun putString(key: String, value: String) = prefs.put(key, value)
    fun putFloat(key: String, value: Float) = prefs.putFloat(key, value)
    fun putDouble(key: String, value: Double) = prefs.putDouble(key, value)

    // --- Read ---
    fun getInt(key: String, default: Int = 0): Int = prefs.getInt(key, default)
    fun getLong(key: String, default: Long = 0L): Long = prefs.getLong(key, default)
    fun getBoolean(key: String, default: Boolean = false): Boolean = prefs.getBoolean(key, default)
    fun getString(key: String, default: String = ""): String = prefs.get(key, default)
    fun getFloat(key: String, default: Float = 0f): Float = prefs.getFloat(key, default)
    fun getDouble(key: String, default: Double = 0.0): Double = prefs.getDouble(key, default)

    // --- Misc ---
    fun remove(key: String) = prefs.remove(key)
    fun clearAll() = prefs.clear()
    fun contains(key: String): Boolean = prefs.get(key, null) != null
}