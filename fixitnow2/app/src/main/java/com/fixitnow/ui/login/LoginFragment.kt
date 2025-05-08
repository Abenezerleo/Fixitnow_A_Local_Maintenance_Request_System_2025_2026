package com.fixitnow.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fixitnow.R
import com.fixitnow.data.AppDatabase
import com.fixitnow.data.User
import com.fixitnow.data.UserType
import com.fixitnow.databinding.DialogRegisterBinding
import com.fixitnow.databinding.FragmentLoginBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (validateInput(username, password)) {
                authenticateUser(username, password)
            }
        }

        binding.registerButton.setOnClickListener {
            showRegistrationDialog()
        }
    }

    private fun authenticateUser(username: String, password: String) {
        lifecycleScope.launch {
            val database = AppDatabase.getDatabase(requireContext(), lifecycleScope)
            val user = database.userDao().getUser(username, password)

            if (user != null) {
                when (user.userType) {
                    UserType.ADMIN -> {
                        findNavController().navigate(LoginFragmentDirections.actionLoginToAdmin())
                    }
                    UserType.REQUESTER -> {
                        findNavController().navigate(LoginFragmentDirections.actionLoginToRequester())
                    }
                    UserType.PROVIDER -> {
                        findNavController().navigate(LoginFragmentDirections.actionLoginToProvider())
                    }
                }
            } else {
                Toast.makeText(context, R.string.error_invalid_credentials, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showRegistrationDialog() {
        val dialogBinding = DialogRegisterBinding.inflate(layoutInflater)
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.register)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.register) { dialog, _ ->
                val username = dialogBinding.usernameEditText.text.toString()
                val password = dialogBinding.passwordEditText.text.toString()
                val confirmPassword = dialogBinding.confirmPasswordEditText.text.toString()
                val userType = when (dialogBinding.userTypeGroup.checkedRadioButtonId) {
                    R.id.requesterRadio -> UserType.REQUESTER
                    R.id.providerRadio -> UserType.PROVIDER
                    else -> null
                }

                if (validateRegistration(username, password, confirmPassword, userType)) {
                    registerUser(username, password, userType!!)
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun validateRegistration(
        username: String,
        password: String,
        confirmPassword: String,
        userType: UserType?
    ): Boolean {
        if (username.isEmpty()) {
            Toast.makeText(context, R.string.error_username_required, Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(context, R.string.error_password_required, Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(context, R.string.error_passwords_dont_match, Toast.LENGTH_SHORT).show()
            return false
        }
        if (userType == null) {
            Toast.makeText(context, R.string.error_user_type_required, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun registerUser(username: String, password: String, userType: UserType) {
        lifecycleScope.launch {
            val database = AppDatabase.getDatabase(requireContext(), lifecycleScope)
            
            // Check if username already exists
            if (database.userDao().getUserByUsername(username) != null) {
                Toast.makeText(context, R.string.error_username_exists, Toast.LENGTH_SHORT).show()
                return@launch
            }

            // Create new user
            val user = User(username, password, userType)
            database.userDao().insertUser(user)
            Toast.makeText(context, R.string.registration_success, Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            binding.usernameEditText.error = getString(R.string.error_username_required)
            return false
        }
        if (password.isEmpty()) {
            binding.passwordEditText.error = getString(R.string.error_password_required)
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 