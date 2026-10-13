package com.example.comunicados

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

class QuickPhraseFragment : Fragment() {

    // Listener //
    var onPhraseSelectedListener: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quick_phrase, container, false)
        val containerPhrases = view.findViewById<LinearLayout>(R.id.container_phrases)

        val phrases = loadPhrasesFromContentProvider()

        phrases.forEach { phrase ->
            val button = Button(requireContext()).apply {
                text = phrase.texto
                setOnClickListener {
                    onPhraseSelectedListener?.invoke(phrase.texto)
                }
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 16, 0)
                }
                layoutParams = params
            }
            containerPhrases.addView(button)
        }

        return view
    }

    private fun loadPhrasesFromContentProvider(): List<QuickPhrase> {
        val list = mutableListOf<QuickPhrase>()
        val cursor = requireContext().contentResolver.query(
            QuickPhraseContentProvider.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use { c ->
            val idIndex = c.getColumnIndex("_id")
            val textoIndex = c.getColumnIndex("texto")

            while (c.moveToNext()) {
                val id = if (idIndex != -1) c.getLong(idIndex) else 0L
                val texto = if (textoIndex != -1) c.getString(textoIndex) else ""
                if (texto.isNotEmpty()) {
                    list.add(QuickPhrase(id, texto))
                }
            }
        }

        return list
    }
}
