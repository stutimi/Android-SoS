package com.xenonesis.womensafety.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.PhoneAuthProvider
import com.xenonesis.womensafety.databinding.ActivityAuthBinding
import com.xenonesis.womensafety.ui.MainActivity
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAuthBinding
    private lateinit var viewModel: AuthViewModel
    
    private var verificationId: String? = null
    
    private val phoneAuthCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: com.google.firebase.auth.PhoneAuthCredential) {
            // Auto-verification completed
            lifecycleScope.launch {
                viewModel.signInWithCredential(credential)
            }
        }
        
        override fun onVerificationFailed(e: com.google.firebase.FirebaseException) {
            Toast.makeText(this@AuthActivity, "Verification failed: ${e.message}", Toast.LENGTH_LONG).show()
            viewModel.setLoading(false)
        }
        
        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            this@AuthActivity.verificationId = verificationId
            viewModel.setCodeSent(true)
            Toast.makeText(this@AuthActivity, "Verification code sent", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        
        setupUI()
        observeViewModel()
    }
    
    private fun setupUI() {
        binding.apply {
            btnSendCode.setOnClickListener {
                val phoneNumber = etPhoneNumber.text.toString().trim()
                if (validatePhoneNumber(phoneNumber)) {
                    sendVerificationCode(phoneNumber)
                }
            }
            
            btnVerifyCode.setOnClickListener {
                val code = etVerificationCode.text.toString().trim()
                if (validateVerificationCode(code)) {
                    verifyCode(code)
                }
            }
            
            btnSkipAuth.setOnClickListener {
                // For demo purposes - sign in anonymously
                lifecycleScope.launch {
                    viewModel.signInAnonymously()
                }
            }
            
            tvResendCode.setOnClickListener {
                val phoneNumber = etPhoneNumber.text.toString().trim()
                if (validatePhoneNumber(phoneNumber)) {
                    sendVerificationCode(phoneNumber)
                }
            }
        }
    }
    
    private fun observeViewModel() {
        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthViewModel.AuthState.Loading -> {
                    binding.progressBar.visibility = if (state.isLoading) 
                        android.view.View.VISIBLE else android.view.View.GONE
                    
                    binding.btnSendCode.isEnabled = !state.isLoading
                    binding.btnVerifyCode.isEnabled = !state.isLoading
                }
                
                is AuthViewModel.AuthState.CodeSent -> {
                    binding.layoutVerificationCode.visibility = android.view.View.VISIBLE
                    binding.tvResendCode.visibility = android.view.View.VISIBLE
                    binding.btnSendCode.text = "Resend Code"
                }
                
                is AuthViewModel.AuthState.Authenticated -> {
                    // Navigate to main activity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                
                is AuthViewModel.AuthState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
                
                is AuthViewModel.AuthState.Idle -> {
                    // Initial state
                }
            }
        }
    }
    
    private fun sendVerificationCode(phoneNumber: String) {
        lifecycleScope.launch {
            viewModel.sendVerificationCode(phoneNumber, this@AuthActivity, phoneAuthCallback)
        }
    }
    
    private fun verifyCode(code: String) {
        verificationId?.let { id ->
            lifecycleScope.launch {
                viewModel.verifyPhoneNumber(id, code)
            }
        } ?: run {
            Toast.makeText(this, "Verification ID not found. Please resend code.", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        if (phoneNumber.isEmpty()) {
            binding.tilPhoneNumber.error = "Phone number is required"
            return false
        }
        
        val digitsOnly = phoneNumber.replace(Regex("[^0-9]"), "")
        if (digitsOnly.length < 10) {
            binding.tilPhoneNumber.error = "Please enter a valid phone number"
            return false
        }
        
        binding.tilPhoneNumber.error = null
        return true
    }
    
    private fun validateVerificationCode(code: String): Boolean {
        if (code.isEmpty()) {
            binding.tilVerificationCode.error = "Verification code is required"
            return false
        }
        
        if (code.length != 6) {
            binding.tilVerificationCode.error = "Please enter the 6-digit code"
            return false
        }
        
        binding.tilVerificationCode.error = null
        return true
    }
}