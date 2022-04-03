package com.moneybox.minimb.data.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.moneybox.minimb.R
import com.moneybox.minimb.data.ui.login.LoginViewModel
import com.moneybox.minimb.databinding.FragmentAccountsBinding
import com.moneybox.minimb.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FragmentAccounts : Fragment(R.layout.fragment_accounts){

    val viewModel: AccountsViewModel by viewModel{ parametersOf(arguments?.get("accessToken")) }

    private var _binding: FragmentAccountsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel(){

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.products.collect { product ->

                    binding.progressBarLoading.isVisible = product.isLoading()
                    binding.textViewPlanValue.isVisible = product.isLoading().not()
                    binding.textViewPlanValueTitle.isVisible = product.isLoading().not()

                    product.data?.totalPlanValue?.let {

                        binding.textViewPlanValue.text = it.toString()
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