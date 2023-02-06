package com.aemm.libreria.view.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.aemm.libreria.R
import com.aemm.libreria.databinding.ActivityLoginBinding
import com.aemm.libreria.util.Constants
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

    // Campos para validar
    private var email: String = ""
    private var contrasenia: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Instanciando mi objeto de firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

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
                        val dialog = AlertDialog.Builder(it.context)
                        user_fb?.sendEmailVerification()?.addOnSuccessListener {
                            dialog.setMessage(getString(R.string.dialog_create_sucess))
                            dialog.setPositiveButton(
                                getString(R.string.dialog_btn_positive_agreed),
                                DialogInterface.OnClickListener { _, _ ->
                                    val intent = Intent(this, LibraryActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            )
                            dialog.create().show()
                        }?.addOnFailureListener {
                            dialog.setMessage(getString(R.string.dialog_create_failure))
                            dialog.setPositiveButton(
                                getString(R.string.dialog_btn_positive_agreed),
                                DialogInterface.OnClickListener { _, _ ->
                                }
                            )
                            dialog.create().show()
                        }

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

        if (this.contrasenia.isEmpty() || this.contrasenia.length < 6) {
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

        val errorFirebaseDialog = AlertDialog.Builder(this)
        errorFirebaseDialog.setTitle(getString(R.string.ERROR_FIREBASE_TITLE))

        when (errorCode) {
            EnumFirebaseCode.ERROR_INVALID_EMAIL -> {
                errorFirebaseDialog.setMessage(getString(R.string.ERROR_INVALID_EMAIL))
                binding.formUser.error = getString(R.string.message_form_error_user)
                binding.formUser.requestFocus()
            }
            EnumFirebaseCode.ERROR_WRONG_PASSWORD -> {
                errorFirebaseDialog.setMessage(getString(R.string.ERROR_WRONG_PASSWORD))
                binding.formPassword.error = getString(R.string.ERROR_WRONG_PASSWORD)
                binding.formPassword.requestFocus()
                binding.formPassword.setText("")
            }
            EnumFirebaseCode.ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL -> {
                errorFirebaseDialog.setMessage(getString(R.string.ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL))
            }
            EnumFirebaseCode.ERROR_EMAIL_ALREADY_IN_USE -> {
                errorFirebaseDialog.setMessage(getString(R.string.ERROR_EMAIL_ALREADY_IN_USE))
                binding.formUser.error = getString(R.string.ERROR_EMAIL_ALREADY_IN_USE)
                binding.formUser.requestFocus()
            }
            EnumFirebaseCode.ERROR_USER_TOKEN_EXPIRED -> {
                errorFirebaseDialog.setMessage(getString(R.string.ERROR_USER_TOKEN_EXPIRED))
            }
            EnumFirebaseCode.ERROR_USER_NOT_FOUND -> {
                errorFirebaseDialog.setMessage(getString(R.string.ERROR_USER_NOT_FOUND))
            }
            EnumFirebaseCode.ERROR_WEAK_PASSWORD -> {
                errorFirebaseDialog.setMessage(getString(R.string.ERROR_WEAK_PASSWORD))
                binding.formPassword.error = getString(R.string.ERROR_WEAK_PASSWORD)
                binding.formPassword.requestFocus()
            }
            EnumFirebaseCode.NO_NETWORK -> {
                errorFirebaseDialog.setMessage(getString(R.string.NO_NETWORK))
            }
            else -> {
                errorFirebaseDialog.setMessage(getString(R.string.ERROR_DEFAULT))
            }
        }

        errorFirebaseDialog.setPositiveButton(
            getString(R.string.dialog_btn_positive_agreed),
            DialogInterface.OnClickListener { dialog, which ->

            }
        ).show()

    }

}