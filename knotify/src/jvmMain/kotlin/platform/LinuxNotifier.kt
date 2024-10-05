package platform

import Notifier
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.interfaces.DBusInterface
import org.freedesktop.dbus.types.UInt32
import org.freedesktop.dbus.types.Variant
import java.io.File
import java.nio.file.Paths

internal class LinuxNotifier(private val appName: String) : Notifier {

    override fun notify(title: String, message: String, appIcon: String?): Boolean {
        val iconPath = appIcon?.let { pathAbs(it) }
        val errors = mutableListOf<String>()

        // Test with D-Bus
        try {
            val conn = DBusConnection.getConnection(DBusConnection.DBusBusType.SESSION)
            val notifications = conn.getRemoteObject(
                "org.freedesktop.Notifications",
                "/org/freedesktop/Notifications",
                Notifications::class.java
            )

            val appName = ""
            val replacesId = UInt32(0)
            val actions = emptyList<String>()
            val hints = emptyMap<String, Variant<*>>()
            val expireTimeout = -1

            if (iconPath != null) {
                notifications.Notify(appName, replacesId, iconPath, title, message, actions, hints, expireTimeout)
            } else {
                notifications.Notify(appName, replacesId, "", title, message, actions, hints, expireTimeout)
            }
            conn.disconnect()
            return true
        } catch (e: Exception) {
            errors.add("Erreur D-Bus : ${e.message}")
        }

        // Test with notify-send
        try {
            val sendCmd = findCommand("sw-notify-send")
                ?: findCommand("notify-send")
                ?: throw Exception("No notify-send command found")
            val process = ProcessBuilder(sendCmd, "-a", appName, title, message, "-i", iconPath).start()
            val exitCode = process.waitFor()
            if (exitCode == 0) {
                return true
            } else {
                throw Exception("notify-send completed with code $exitCode")
            }
        } catch (e: Exception) {
            errors.add("notify-send error: ${e.message}")
        }

        // Test with kdialog
        try {
            val sendCmd = findCommand("kdialog") ?: throw Exception("Aucune commande kdialog trouvée")
            val process = ProcessBuilder(sendCmd, "--title", title, "--passivepopup", message, "10", "--icon", iconPath).start()
            val exitCode = process.waitFor()
            if (exitCode == 0) {
                return true
            } else {
                throw Exception("kdialog exited with code $exitCode")
            }
        } catch (e: Exception) {
            errors.add("kdialog error: ${e.message}")
        }

        throw Exception("Notification failed: ${errors.joinToString("; ")}")
    }

    private fun pathAbs(name: String): String {
        return if (name.isEmpty()) "" else try {
            Paths.get(name).toAbsolutePath().toString()
        } catch (e: Exception) {
            name
        }
    }

    private fun findCommand(command: String): String? {
        val paths = System.getenv("PATH")?.split(File.pathSeparator) ?: emptyList()
        for (path in paths) {
            val cmd = File(path, command)
            if (cmd.exists() && cmd.canExecute()) {
                return cmd.absolutePath
            }
        }
        return null
    }

    // Definition of the D-Bus interface
    interface Notifications : DBusInterface {
        fun Notify(
            app_name: String,
            replaces_id: UInt32,
            app_icon: String,
            summary: String,
            body: String,
            actions: List<String>,
            hints: Map<String, Variant<*>>,
            expire_timeout: Int
        ): UInt32
    }
}
