package com.example.quote4.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.quote4.databinding.ActivityMainBinding
import com.example.quote4.model.QuoteData
import com.example.quote4.viewmodel.QuoteViewModel
import com.example.quote4.viewmodel.QuoteViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var quoteViewModel: QuoteViewModel

    private val showToastMessageObserver: (String) -> Unit = { message ->
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        quoteViewModel =
            ViewModelProvider(this, QuoteViewModelFactory(application))[QuoteViewModel::class.java]

        quoteViewModel.showToastMessage.observe(this, showToastMessageObserver)

        setupUI()

    }

    private fun setupUI() {
        setQuoteOnScreen(quoteViewModel.getQuote())

        binding.tvPrevious.setOnClickListener {
            setQuoteOnScreen(quoteViewModel.getPreviousQuote())
        }

        binding.tvNext.setOnClickListener {
            setQuoteOnScreen(quoteViewModel.getNextQuote())
        }

        binding.btnFabShare.setOnClickListener {
            shareCurrentQuote()
        }

    }

    private fun setQuoteOnScreen(quote: QuoteData) {
        binding.tvQuoteText.text = quote.quote
        binding.tvQuoteAuthor.text = quote.author
        binding.tvQuoteCounter.text = (" ${quoteViewModel.indexQuote + 1} / ${quoteViewModel.quoteList.size}")
    }

    private fun shareCurrentQuote() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, quoteViewModel.getQuote().quote)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        quoteViewModel.showToastMessage.removeObserver(showToastMessageObserver) // Remove observers to prevent memory leaks
    }
}