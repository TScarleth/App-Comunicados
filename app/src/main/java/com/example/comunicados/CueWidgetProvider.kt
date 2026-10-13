package com.example.comunicados

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

// AppWidgetProvider para el widget compacto de Cue :) //
class CueWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Actualizar cada instancia del widget //
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.cue_widget)
            views.setTextViewText(R.id.tv_widget_title, "Cue :)")
            views.setTextViewText(R.id.tv_widget_phrase, "Necesito ayuda")

            // Intent para abrir MainActivity al pulsar el widget //
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Asignar el clic al contenedor principal del widget //
            views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
