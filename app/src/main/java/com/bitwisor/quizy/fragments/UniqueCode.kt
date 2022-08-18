package com.bitwisor.quizy.fragments

import android.R.attr.label
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.bitwisor.quizy.MainActivity
import com.bitwisor.quizy.databinding.FragmentUniqueCodeBinding
import com.google.firebase.database.core.Context
import java.util.*


class UniqueCode : Fragment() {
    lateinit var binding: FragmentUniqueCodeBinding
    var uniqueCode = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUniqueCodeBinding.inflate(layoutInflater)
        return binding.root
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView:TextView=binding.joinCodetxt
        val args=this.arguments
        if (args!=null){
            uniqueCode= args.getString("uniqueId").toString()
        }
        textView.text = uniqueCode

        val whatsapp:ImageView=binding.shareviaWhatsapp
        val telegram:ImageView=binding.shareviaTelegram
        val instagram:ImageView=binding.shareviaInstagram
        val copy:ImageView=binding.copyToClipboard
        binding.BackQuizBtn.setOnClickListener {
        val i = Intent(requireActivity(),MainActivity::class.java)
        startActivity(i)
            requireActivity().finishAffinity()
        }
        whatsapp.setOnClickListener {

            openWhatsapp()
        }

        telegram.setOnClickListener {
            openTelegram()
        }

        instagram.setOnClickListener {
            openInstagram()
        }

        copy.setOnClickListener {
            val clipboard = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("quizCode","https://www.quizy.com/${uniqueCode}")
            clipboard!!.setPrimaryClip(clip)
            Toast.makeText(requireContext(),"Sharable link is copied to Clipboard",Toast.LENGTH_SHORT).show()
         //  var clipboard:ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        }





    }

    private fun openGbWhatsapp() {
        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/plain"
        whatsappIntent.setPackage("com.gbwhatsapp")
        whatsappIntent.putExtra(
            Intent.EXTRA_TEXT,
            "https://www.quizy.com/${uniqueCode}"
        )
        try {
            Objects.requireNonNull(requireActivity())?.startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=com.gbwhatsapp")
                )
            )
        }
    }

    private fun openInstagram() {
        val instagramIntent = Intent(Intent.ACTION_SEND)
        instagramIntent.type = "text/plain"
        instagramIntent.setPackage("com.instagram.android")
        instagramIntent.putExtra(
            Intent.EXTRA_TEXT,
            "https://www.quizy.com/${uniqueCode}"
        )
        try {
            Objects.requireNonNull(requireActivity())?.startActivity(instagramIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=com.instagram.android")
                )
            )
        }


    }

    private fun openTelegram() {
        val telegramIntent = Intent(Intent.ACTION_SEND)
        telegramIntent.type = "text/plain"
        telegramIntent.setPackage("org.telegram.messenger")
        telegramIntent.putExtra(
            Intent.EXTRA_TEXT,
            "https://www.quizy.com/${uniqueCode}"
        )
        try {
            Objects.requireNonNull(requireActivity())?.startActivity(telegramIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=org.telegram.messenger")
                )
            )
        }

    }

    private fun openWhatsapp() {
        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/plain"
        whatsappIntent.setPackage("com.whatsapp")
        whatsappIntent.putExtra(
            Intent.EXTRA_TEXT,
            "https://www.quizy.com/${uniqueCode}"
        )
        try {
            Objects.requireNonNull(requireActivity())?.startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")
                )
            )
        }
    }


}