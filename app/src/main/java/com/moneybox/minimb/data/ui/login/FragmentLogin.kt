package com.moneybox.minimb.data.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.moneybox.minimb.R
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.moneybox.minimb.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentLogin : Fragment(R.layout.fragment_login) {

    val viewModel: LoginViewModel by viewModel()

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        observeViewModel()
    }

    private fun setUpView(){

        //to show * instead of bullets in password edittext
        binding.editTextPass.transformationMethod = AsteriskPasswordTransformationMethod()
        binding.editTextEmail.addTextChangedListener {
            viewModel.emailEntered(it.toString())
        }
        binding.editTextPass.addTextChangedListener {
            viewModel.passEntered(it.toString())
        }
        binding.buttonContinue.setOnClickListener {
            viewModel.loginButtonClicked()
        }
    }

    private fun observeViewModel(){

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.continueButtonEnable.collect { isEnable ->
                    binding.buttonContinue.isEnabled = isEnable
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.login.collect { login ->

                    binding.buttonContinue.text = if(login.isLoading()) "" else getString(R.string.log_in)
                    binding.progressBarLoading.isVisible = login.isLoading()
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToAccounts.collect { toUserDetail ->

                    toUserDetail?.getContentIfNotHandled()?.let {

                        Toast.makeText(requireContext(), "welcome ${it.user.firstName}!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.fragment_login_to_fragment_accounts, bundleOf("accessToken" to it.session.bearerToken))
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collect { toUserDetail ->

                    toUserDetail?.getContentIfNotHandled()?.let {

                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}