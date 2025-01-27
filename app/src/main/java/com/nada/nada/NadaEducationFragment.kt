package com.nada.nada

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nada.nada.databinding.FragmentNadaEducationBinding

class NadaEducationFragment : Fragment() {
    private var _binding: FragmentNadaEducationBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNadaEducationBinding.inflate(inflater, container, false)
        val root: View = binding.root



        binding.educationConsultingCardView.setOnClickListener {
            val educationConsultingCardViewurl = "https://www.nada.org/nada/education-consulting\n"
            val educationConsultingCardViewintent = Intent(Intent.ACTION_VIEW, Uri.parse(educationConsultingCardViewurl))
            startActivity(educationConsultingCardViewintent)
        }

        binding.resourceCenterCardView.setOnClickListener {
            val resourceCenterCardViewurl = "https://www.nada.org/nada/education-consulting/resource-center"
            val resourceCenterCardViewintent = Intent(Intent.ACTION_VIEW, Uri.parse(resourceCenterCardViewurl))
            startActivity(resourceCenterCardViewintent)
        }

        binding.academyCardView.setOnClickListener {
            val academyCardViewurl = "https://www.nada.org/nada/education-consulting/nada-academy"
            val academyCardViewintent = Intent(Intent.ACTION_VIEW, Uri.parse(academyCardViewurl))
            startActivity(academyCardViewintent)
        }

        binding.twentyGroupCardView.setOnClickListener {
            val twentyGroupCardViewurl = "https://www.nada.org/index.php/nada/nada-20-group"
            val twentyGroupCardViewintent = Intent(Intent.ACTION_VIEW, Uri.parse(twentyGroupCardViewurl))
            startActivity(twentyGroupCardViewintent)
        }

        binding.consultingCardView.setOnClickListener {
            val consultingCardViewurl = "https://www.nada.org/index.php/nada/education-consulting/atd-education/dealership-consulting"
            val consultingCardViewintent = Intent(Intent.ACTION_VIEW, Uri.parse(consultingCardViewurl))
            startActivity(consultingCardViewintent)
        }







        return root
    }


}