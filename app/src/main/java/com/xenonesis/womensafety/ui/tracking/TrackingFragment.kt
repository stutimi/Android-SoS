package com.xenonesis.womensafety.ui.tracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xenonesis.womensafety.databinding.FragmentTrackingBinding

class TrackingFragment : Fragment() {
    
    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // TODO: Implement tracking functionality
        binding.tvTrackingPlaceholder.text = "Safety Tracking\n\nComing Soon!"
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}