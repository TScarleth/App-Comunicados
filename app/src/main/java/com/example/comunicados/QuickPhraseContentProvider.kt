package com.example.comunicados

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri

//frases rápidas //
class QuickPhraseContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.comunicados.quickphraseprovider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/phrases")
    }

    private val phrases = listOf(
        "Necesito ayuda",
        "Sí",
        "No",
        "Gracias"
    )

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        val cursor = MatrixCursor(arrayOf("_id", "texto"))
        phrases.forEachIndexed { index, phrase ->
            cursor.addRow(arrayOf(index.toLong(), phrase))
        }
        return cursor
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.dir/vnd.com.example.comunicados.phrases"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("Operación no soportada")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("Operación no soportada")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        throw UnsupportedOperationException("Operación no soportada")
    }
}
