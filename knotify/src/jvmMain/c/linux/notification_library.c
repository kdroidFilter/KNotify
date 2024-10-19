#include <libnotify/notify.h>
#include <glib.h>
#include <gdk-pixbuf/gdk-pixbuf.h>
#include <stdlib.h>
#include "notification_library.h"

// Callback par défaut pour le clic sur un bouton
void default_button_clicked_callback(NotifyNotification *notification, char *action, gpointer user_data) {
    g_print("Button clicked with action: %s\n", action);
}

// Création de la notification
Notification *create_notification(const char *summary, const char *body, const char *icon_path) {
    if (!notify_init("Notification Library")) {
        fprintf(stderr, "Failed to initialize notifications.\n");
        return NULL;
    }

    NotifyNotification *notification = notify_notification_new(summary, body, icon_path);
    return notification;
}

// Création de la notification avec une image GdkPixbuf
Notification *create_notification_with_pixbuf(const char *summary, const char *body, const char *image_path) {
    if (!notify_init("Notification Library")) {
        fprintf(stderr, "Failed to initialize notifications.\n");
        return NULL;
    }

    NotifyNotification *notification = notify_notification_new(summary, body, NULL);

    // Chargement de l'image GdkPixbuf à partir du fichier
    GdkPixbuf *pixbuf = gdk_pixbuf_new_from_file(image_path, NULL);
    if (pixbuf != NULL) {
        notify_notification_set_image_from_pixbuf(notification, pixbuf);
        g_object_unref(pixbuf); // Libérer le GdkPixbuf après utilisation
    } else {
        fprintf(stderr, "Failed to load image: %s\n", image_path);
    }

    return notification;
}

// Ajout d'un bouton à la notification
void add_button_to_notification(Notification *notification, const char *button_id, const char *button_label, NotifyActionCallback callback, gpointer user_data) {
    if (callback == NULL) {
        callback = default_button_clicked_callback;
    }
    notify_notification_add_action(notification, button_id, button_label, callback, user_data, NULL);
}

// Envoi de la notification
int send_notification(Notification *notification) {
    GError *error = NULL;
    if (!notify_notification_show(notification, &error)) {
        fprintf(stderr, "Failed to send notification: %s\n", error->message);
        g_error_free(error);
        notify_uninit();
        return EXIT_FAILURE;
    }
    return EXIT_SUCCESS;
}

// Libération des ressources
void cleanup_notification() {
    notify_uninit();
}

// Fonction pour démarrer la boucle principale GLib
void run_main_loop() {
    GMainLoop *loop = g_main_loop_new(NULL, FALSE);
    g_main_loop_run(loop);
}

/* Compilation en bibliothèque partagée (libnotification.so) */
// Pour compiler cette bibliothèque en un fichier .so (bibliothèque partagée), vous pouvez utiliser la commande suivante :
// gcc -shared -o libnotification.so -fPIC notification_library.c $(pkg-config --cflags --libs libnotify glib-2.0 gdk-pixbuf-2.0)