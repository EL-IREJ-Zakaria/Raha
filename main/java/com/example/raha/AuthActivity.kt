package com.example.raha

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.raha.databinding.ActivityAuthBinding
import com.example.raha.data.DatabaseHelper
import com.example.raha.data.User

private var Any.error: String
    get() {
        TODO()
    }
    set(value) {
        TODO()
    }
private val ActivityAuthBinding.editTextPoids: Any
    get() {
        TODO()
    }
private val ActivityAuthBinding.editTextAge: Any
    get() {
        TODO()
    }
var Any.text: Any
    get() {
        TODO()
        }
    set(value) {
        TODO()
    }


private val ActivityAuthBinding.editTextNom: Any
    get() {
        TODO()
    }

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        binding.buttonLogin.setOnClickListener {
            performLogin()
        }

        binding.buttonRegister.setOnClickListener {
            performRegistration()
        }

        binding.textViewForgotPassword.setOnClickListener {
            Toast.makeText(
                this,
                "Fonctionnalité de réinitialisation de mot de passe à implémenter",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: CharSequence): Boolean {
        return password.length >= 6
    }

    private fun performLogin() {
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (!isValidEmail(email)) {
            binding.editTextEmail.error = "Veuillez entrer une adresse e-mail valide."
            return
        }

        if (!isValidPassword(password)) {
            binding.editTextPassword.error = "Le mot de passe doit contenir au moins 6 caractères."
            return
        }

        val user = dbHelper.getUserByEmail(email)

        if (user != null && user.motDePasse == password) {
            saveAuthState(true)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            Toast.makeText(this, "Connexion réussie!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Email ou mot de passe incorrect.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performRegistration() {
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        val nom = binding.editTextNom.text.toString().trim()
        val age = binding.editTextAge.text.toString().trim().toIntOrNull()
        val poids = binding.editTextPoids.text.toString().trim().toDoubleOrNull()

        if (!isValidEmail(email)) {
            binding.editTextEmail.error = "Veuillez entrer une adresse e-mail valide."
            return
        }

        if (!isValidPassword(password)) {
            binding.editTextPassword.error = "Le mot de passe doit contenir au moins 6 caractères."
            return
        }

        if (nom.isEmpty()) {
            binding.editTextNom.error = "Veuillez entrer votre nom."
            return
        }

        if (age == null || age <= 0) {
            binding.editTextAge.error = "Veuillez entrer un âge valide."
            return
        }

        if (poids == null || poids <= 0) {
            binding.editTextPoids.error = "Veuillez entrer un poids valide."
            return
        }

        // Vérifier si l'utilisateur existe déjà
        if (dbHelper.getUserByEmail(email) != null) {
            Toast.makeText(this, "Cet e-mail est déjà enregistré.", Toast.LENGTH_SHORT).show()
            return
        }

        val newUser = User(id = 0, email = email, motDePasse = password, nom = nom, age = age, poids = poids)
        val userId = dbHelper.addUser(newUser)

        if (userId != -1L) {
            saveAuthState(true)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            Toast.makeText(this, "Inscription réussie!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Échec de l'inscription.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveAuthState(isAuthenticated: Boolean) {
        getSharedPreferences("auth_pref", MODE_PRIVATE).edit()
            .putBoolean("is_authenticated", isAuthenticated)
            .apply()
    }
}