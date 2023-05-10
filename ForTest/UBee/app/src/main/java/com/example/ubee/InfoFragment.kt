package com.example.ubee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ubee.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater)

        // 뒤로 가기 버튼 표기
        binding.toolbarMap.setNavigationIcon(R.drawable.ic_back_arrow)

        // 뒤로 가기 동작
        binding.toolbarMap.setNavigationOnClickListener{
            findNavController().popBackStack()
        }

        return binding.root
    }
}