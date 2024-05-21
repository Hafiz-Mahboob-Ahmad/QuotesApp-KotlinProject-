package com.example.quote4.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quote4.model.QuoteData
import com.google.gson.Gson

class QuoteViewModel(val context: Context) : ViewModel() {

    var quoteList: Array<QuoteData> = emptyArray()
    var indexQuote: Int = 0
    val showToastMessage = MutableLiveData<String>()

    init {
        quoteList = LoadQuotesFromAsset()
    }

    private fun LoadQuotesFromAsset(): Array<QuoteData> {

        val inputStream = context.assets.open("quotes.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        val json = String(buffer, Charsets.UTF_8)
        val gson = Gson()
        return gson.fromJson(json, Array<QuoteData>::class.java)

    }

    fun getQuote() = quoteList[indexQuote]

    fun getPreviousQuote(): QuoteData {
        return if (indexQuote > 0) {
            quoteList[--indexQuote]
        } else {
            displayToastMessage("First quote!")
            quoteList[0]
        }
    }

    fun getNextQuote(): QuoteData {
        return if (indexQuote == quoteList.size - 1) {
            displayToastMessage("Last quote!")
            quoteList[quoteList.size - 1]
        } else {
            quoteList[++indexQuote]
        }
    }

    fun displayToastMessage(mes: String) {
        showToastMessage.value = mes
    }

}



// *******************************************************
//fun nextQuote(): QuoteData {
//    return if (quoteCurrentIndex < quoteList.size - 1) {
//        quoteList[++quoteCurrentIndex]
//    } else {
//        displayToastMessage("Last quote!")
//        quoteList[quoteCurrentIndex] // Return the last quote without incrementing index
//    }
//}