import java.awt.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.swing.*
import javax.swing.text.BadLocationException
import javax.swing.text.DefaultCaret
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument

object LogConsole {
    private var frame: JFrame? = null
    private val textPane = JTextPane()
    private val doc: StyledDocument = textPane.styledDocument

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun show() {
        if (frame != null) {
            frame!!.isVisible = true
            frame!!.toFront()
            return
        }

        // Стилизация текстовой области
        textPane.isEditable = false
        textPane.background = Color(30, 30, 30)
        textPane.foreground = Color.WHITE
        textPane.font = Font("JetBrains Mono", Font.PLAIN, 14)
        textPane.margin = Insets(10, 10, 10, 10)
        (textPane.caret as? DefaultCaret)?.updatePolicy = DefaultCaret.ALWAYS_UPDATE

        val scrollPane = JScrollPane(textPane).apply {
            border = BorderFactory.createEmptyBorder()
            viewportBorder = BorderFactory.createEmptyBorder()
            viewport.background = textPane.background
        }

        // Создание и показ окна
        frame = JFrame("Логи").apply {
            defaultCloseOperation = WindowConstants.HIDE_ON_CLOSE
            setSize(850, 600)
            minimumSize = Dimension(600, 400)
            contentPane.background = Color(40, 40, 40)
            contentPane.layout = BorderLayout()
            contentPane.add(scrollPane, BorderLayout.CENTER)
            isVisible = true
            setLocationRelativeTo(null)
        }

        info("Логгер запущен")
    }

    private fun log(level: String, message: String, color: Color) {
        val timestamp = LocalDateTime.now().format(formatter)
        appendFormatted("[$timestamp] [$level] $message", color)
    }

    fun info(message: String) = log("INFO", message, Color(0, 190, 255))            // Голубой
    fun warn(message: String) = log("WARN", message, Color(255, 170, 0))            // Жёлто-оранжевый
    fun error(message: String) = log("ERROR", message, Color(255, 80, 80))          // Красный
    fun success(message: String) = log("SUCCESS", message, Color(80, 255, 140))     // Мятный зелёный
    fun debug(message: String) = log("DEBUG", message, Color(170, 140, 255))        // Сиреневый
    fun trace(message: String) = log("TRACE", message, Color(100, 200, 255))        // Голубой
    fun offline(message: String) = log("OFFLINE", message, Color(255, 80, 80))      // Красный
    fun online(message: String) = log("ONLINE", message, Color(80, 255, 140))      // Мятный зелёный
    private fun appendFormatted(message: String, levelColor: Color) {
        SwingUtilities.invokeLater {
            val regex = Regex("""(\[.*?]) (\[.*?]) (.+)""")
            val match = regex.matchEntire(message)
            if (match != null) {
                val (timestamp, level, content) = match.destructured
                appendSegment(timestamp + " ", Color(160, 160, 160))
                appendSegment(level + " ", levelColor)
                appendSegment(content + "\n", Color.WHITE)
            } else {
                appendSegment(message + "\n", Color.LIGHT_GRAY)
            }
        }
    }

    private fun appendSegment(text: String, color: Color) {
        val style = doc.addStyle("ColorStyle", null)
        StyleConstants.setForeground(style, color)
        try {
            doc.insertString(doc.length, text, style)
        } catch (_: BadLocationException) {
        }
    }
}