package com.gtg.electroshopandroid

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return formatter.format(amount)
}

fun convertBaseUrl(
    url: String,
    oldHost: String = "https://localhost:7169",
    newHost: String = "http://10.0.2.2:5030"
) : String {
    return if (url.contains(oldHost)) {
        url.replace(oldHost, newHost)
    } else {
        url
    }
}