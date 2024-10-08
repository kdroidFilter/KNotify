/**
 * Interface for sending native notifications.
 */
interface Notifier {

    /**
     * Sends a native notification with the specified title, message, and application icon.
     *
     * @param title The title of the notification.
     * @param message The message content of the notification.
     * @param appIcon The path to the application icon to display with the notification.
     *                Note: The `appIcon` parameter is not yet supported on macOS.
     * @return `true` if the notification was successfully sent, `false` otherwise.
     */
    fun notify(title: String, message: String, appIcon: String?): Boolean
}
