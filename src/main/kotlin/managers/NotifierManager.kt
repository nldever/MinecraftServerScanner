package managers

import java.awt.Image
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.net.URL
import javax.imageio.ImageIO

object NotifierManager {
    fun show(title: String, message: String) {
        if (!SystemTray.isSupported()) {
            println("SystemTray не поддерживается")
            return
        }

        val tray = SystemTray.getSystemTray()

        val image: Image = try {
            val url: URL? = NotifierManager::class.java.getResource("/icon.png")
            if (url != null) ImageIO.read(url)
            else Toolkit.getDefaultToolkit().createImage(ByteArray(0))
        } catch (e: Exception) {
            println("Ошибка загрузки icon.png: ${e.message}")
            Toolkit.getDefaultToolkit().createImage(ByteArray(0))
        }

        val trayIcon = TrayIcon(image, "Minecraft Scanner").apply {
            isImageAutoSize = true
            toolTip = "Minecraft Scanner"
        }

        try {
            tray.add(trayIcon)
            trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO)
            Thread.sleep(2000)
            tray.remove(trayIcon)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
