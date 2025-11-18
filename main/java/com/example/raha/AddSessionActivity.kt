package com.example.raha

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.raha.databinding.ActivityAddSessionBinding

private lateinit var onClickListener: () -> Unit
private val ActivityAddSessionBinding.buttonSave: Any
    get() {
        TODO()
    }

class AddSessionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSessionBinding

    companion object {
        private const val MIN_QUALITY = 1
        private const val MAX_QUALITY = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSave.equals {
            saveSession()
        }
    }

    private fun saveSession() {
        // Réinitialiser les erreurs précédentes
        binding.editTextDuration.error = null
        binding.editTextQuality.error = null

        val isDurationValid = validateDuration()
        val isQualityValid = validateQuality()

        if (isDurationValid && isQualityValid) {
            val duration = binding.editTextDuration.text.toString().toDouble()
            val quality = binding.editTextQuality.text.toString().toInt()

            // TODO: Enregistrer les données de la session (par ex., dans une base de données ou SharedPreferences)
            Toast.makeText(this, "Session enregistrée : Durée = $duration h, Qualité = $quality/5", Toast.LENGTH_LONG).show()
            finish() // Ferme l'activité et retourne à la précédente
        }
    }

    private fun validateDuration(): Boolean {
        val durationText = binding.editTextDuration.text.toString()
        if (durationText.isBlank()) {
            binding.editTextDuration.error = "Le champ durée ne peut pas être vide."
            return false
        }
        if (durationText.toDoubleOrNull() == null || durationText.toDouble() <= 0) {
            binding.editTextDuration.error = "Veuillez entrer une durée valide (nombre positif)."
            return false
        }
        return true
    }

    private fun validateQuality(): Boolean {
        val qualityText = binding.editTextQuality.text.toString()
        if (qualityText.isBlank()) {
            binding.editTextQuality.error = "Le champ qualité ne peut pas être vide."
            return false
        }
        if (qualityText.toIntOrNull() == null || qualityText.toInt() !in MIN_QUALITY..MAX_QUALITY) {
            binding.editTextQuality.error = "Note entre $MIN_QUALITY et $MAX_QUALITY."
            return false
        }
        return true
    }
}
