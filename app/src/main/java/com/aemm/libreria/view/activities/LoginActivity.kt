package com.aemm.libreria.view.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.aemm.libreria.R
import com.aemm.libreria.databinding.ActivityLoginBinding
import com.aemm.libreria.util.Constants
import com.aemm.libreria.util.Constants.ACOUNT_FILE
import com.aemm.libreria.util.EnumFirebaseCode
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import java.io.IOException
import java.security.GeneralSecurityException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    //Para Firebase
    private lateinit var firebaseAuth: FirebaseAuth

    // Shared preferences encriptadas
    private lateinit var encryptedSharedPreferences: EncryptedSharedPreferences
    private lateinit var encryptedSharedPrefsEditor: SharedPreferences.Editor

    // Shared Preferences
    private var usuarioSp: String? = ""
    private var contraseniaSp: String? = ""

    // Campos para validar
    private var email: String = ""
    private var contrasenia: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Instanciando mi objeto de firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        try {
            //Creando la llave para encriptar
            val masterKeyAlias = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            encryptedSharedPreferences = EncryptedSharedPreferences
                .create(
                    this,
                    Constants.ACOUNT_FILE,
                    masterKeyAlias,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                ) as EncryptedSharedPreferences
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        this.encryptedSharedPrefsEditor = this.encryptedSharedPreferences.edit()
        this.usuarioSp = this.encryptedSharedPreferences.getString("usuarioSp", "0")
        this.contraseniaSp = this.encryptedSharedPreferences.getString("contraseniaSp", "0")

        binding.formBtnLogin.setOnClickListener {
            if (!validaCampos()) return@setOnClickListener

            binding.progressBar.visibility = View.VISIBLE
            autenticaUsuario(this.email, this.contrasenia)
        }

        binding.formBtnRegistro.setOnClickListener {
            if (!validaCampos()) return@setOnClickListener

            binding.progressBar.visibility = View.VISIBLE

            //Registrando al usuario
            firebaseAuth.createUserWithEmailAndPassword(email, contrasenia)
                .addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        //Enviar correo para verificación de email
                        var user_fb = firebaseAuth.currentUser
                        user_fb?.sendEmailVerification()?.addOnSuccessListener {
                            Toast.makeText(this, getString(R.string.dialog_create_sucess), Toast.LENGTH_SHORT).show()
                        }?.addOnFailureListener {
                            Toast.makeText(this, getString(R.string.dialog_create_failure), Toast.LENGTH_SHORT).show()
                        }

                        Toast.makeText(this, getString(R.string.dialog_create_message), Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, LibraryActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        binding.progressBar.visibility = View.GONE
                        manejaErrores(authResult)
                    }
                }
        }

        binding.forgotPassword.setOnClickListener { it ->
            val resetMail = EditText(it.context)

            resetMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            val passwordResetDialog = AlertDialog.Builder(it.context)
            passwordResetDialog.setTitle(getString(R.string.dialog_recovery_title))
            passwordResetDialog.setMessage(getString(R.string.dialog_recovery_message))
            passwordResetDialog.setView(resetMail)

            passwordResetDialog.setPositiveButton(
                getString(R.string.dialog_btn_positive),
                DialogInterface.OnClickListener { dialog, which ->
                    val mail = resetMail.text.toString()
                    if (mail.isNotEmpty()) {
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                            Toast.makeText(this, getString(R.string.dialog_recovery_sucess), Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener { error ->
                            Toast.makeText(this, getString(R.string.dialog_recovery_failure, error.message), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.message_form_error_user), Toast.LENGTH_SHORT).show()
                    }
                })

            passwordResetDialog.setNegativeButton(getString(R.string.dialog_btn_negative), DialogInterface.OnClickListener { _, _ ->
                })

            passwordResetDialog.create().show()
        }
    }

    private fun validaCampos(): Boolean {
        this.email = binding.formUser.text.toString().trim()
        this.contrasenia = binding.formPassword.text.toString().trim()

        if (this.email.isEmpty()) {
            binding.formUser.error = getString(R.string.message_form_error_user)
            binding.formUser.requestFocus()
            return false
        }

        if (this.contrasenia.isEmpty() || this.contrasenia.length < 8) {
            binding.formPassword.error = getString(R.string.message_form_error_password)
            binding.formPassword.requestFocus()
            return false
        }

        return true
    }

    private fun autenticaUsuario(usr: String, psw: String) {

        firebaseAuth.signInWithEmailAndPassword(usr, psw).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {

                val intent = Intent(this, LibraryActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                binding.progressBar.visibility = View.GONE
                manejaErrores(authResult)
            }
        }
    }

    private fun manejaErrores(task: Task<AuthResult>) {
        var errorCode = EnumFirebaseCode.NO_NETWORK

        errorCode = try {
            val error = (task.exception as FirebaseAuthException).errorCode
            EnumFirebaseCode.valueOf(error)
        } catch (e: Exception) {
            EnumFirebaseCode.NO_NETWORK
        }

        when (errorCode) {
            EnumFirebaseCode.ERROR_INVALID_EMAIL -> {
                Toast.makeText(this, getString(R.string.ERROR_INVALID_EMAIL), Toast.LENGTH_SHORT)
                    .show()
                //binding.tietEmail.error = "Error: El correo electrónico no tiene un formato correcto"
                //binding.tietEmail.requestFocus()
            }
            EnumFirebaseCode.ERROR_WRONG_PASSWORD -> {
                Toast.makeText(this, getString(R.string.ERROR_WRONG_PASSWORD), Toast.LENGTH_SHORT)
                    .show()
                //binding.tietContrasenia.error = "La contraseña no es válida"
                //binding.tietContrasenia.requestFocus()
                //binding.tietContrasenia.setText("")

            }
            EnumFirebaseCode.ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                Toast.makeText(this, getString(R.string.ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL), Toast.LENGTH_SHORT).show()
            }
            EnumFirebaseCode.ERROR_EMAIL_ALREADY_IN_USE -> {
                Toast.makeText(this, getString(R.string.ERROR_EMAIL_ALREADY_IN_USE), Toast.LENGTH_LONG).show()
                //binding.tietEmail.error = ("Error: el correo electrónico ya está en uso con otra cuenta.")
                //binding.tietEmail.requestFocus()
            }
            EnumFirebaseCode.ERROR_USER_TOKEN_EXPIRED -> {
                Toast.makeText(this, getString(R.string.ERROR_USER_TOKEN_EXPIRED), Toast.LENGTH_LONG).show()
            }
            EnumFirebaseCode.ERROR_USER_NOT_FOUND -> {
                Toast.makeText(this, getString(R.string.ERROR_USER_NOT_FOUND), Toast.LENGTH_LONG).show()
            }
            EnumFirebaseCode.ERROR_WEAK_PASSWORD -> {
                Toast.makeText(this, getString(R.string.ERROR_WEAK_PASSWORD), Toast.LENGTH_LONG).show()
                //binding.tietContrasenia.error = "La contraseña debe de tener por lo menos 6 caracteres"
                //binding.tietContrasenia.requestFocus()
            }
            EnumFirebaseCode.NO_NETWORK -> {
                Toast.makeText(this, getString(R.string.NO_NETWORK), Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this, getString(R.string.ERROR_DEFAULT), Toast.LENGTH_SHORT).show()
            }
        }

    }

}