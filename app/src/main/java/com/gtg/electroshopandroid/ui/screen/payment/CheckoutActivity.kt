package com.gtg.electroshopandroid.ui.screen.payment

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class CheckoutActivity : ComponentActivity() {
    companion object {
        private const val BACKEND_URL = "http://10.0.2.2:4242"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val amount = intent.getDoubleExtra("amount", 0.0)

        setContent {
            CheckoutScreen(amount)
        }
    }

    @Composable
    private fun CheckoutScreen(amount:Double) {
        var paymentIntentClientSecret by remember { mutableStateOf<String?>(null) }

        var error by remember { mutableStateOf<String?>(null) }

        val paymentSheet = rememberPaymentSheet { paymentResult ->
            when (paymentResult) {
                is PaymentSheetResult.Completed -> {
                    showToast("Payment complete!")
                    finish() // Quay lại màn hình trước
                }
                is PaymentSheetResult.Canceled -> {showToast("Payment canceled!")
                finish()}
                is PaymentSheetResult.Failed -> {
                    error = paymentResult.error.localizedMessage ?: paymentResult.error.message
                }
            }
        }

        error?.let { errorMessage ->
            ErrorAlert(
                errorMessage = errorMessage,
                onDismiss = {
                    error = null
                }
            )
        }

        LaunchedEffect(amount) {
            fetchPaymentIntent(amount).onSuccess { clientSecret ->
                paymentIntentClientSecret = clientSecret
            }.onFailure { paymentIntentError ->
                error = paymentIntentError.localizedMessage ?: paymentIntentError.message
            }
        }

        androidx.compose.foundation.layout.Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            PayButton(
                enabled = paymentIntentClientSecret != null,
                onClick = {
                    paymentIntentClientSecret?.let {
                        onPayClicked(
                            paymentSheet = paymentSheet,
                            paymentIntentClientSecret = it,
                        )
                    }
                }
            )
        }
    }

    @Composable

    private fun PayButton(
        enabled: Boolean,
        onClick: () -> Unit
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nhấn để thanh toán đơn hàng của bạn",
                color = Color.Black,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách giữa Text và Button

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                onClick = onClick
            ) {
                Text("Pay now")
            }
        }
    }

    @Composable
    private fun ErrorAlert(
        errorMessage: String,
        onDismiss: () -> Unit
    ) {
        AlertDialog(
            title = {
                Text(text = "Error occurred during checkout")
            },
            text = {
                Text(text = errorMessage)
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onDismiss) {
                    Text(text = "Ok")
                }
            }
        )
    }

    private suspend fun fetchPaymentIntent(amount:Double): Result<String> = suspendCoroutine { continuation ->
        val url = "$BACKEND_URL/create-payment-intent"
        val amountInCents = (amount/25000 * 100).toInt()  // Stripe cần integer (cent)

        val shoppingCartContent = """
        {
            "items": [
                {"id":"xl-tshirt", "amount": $amountInCents}
            ]
        }
    """

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val body = shoppingCartContent.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        OkHttpClient()
            .newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resume(Result.failure(e))
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        continuation.resume(Result.failure(Exception(response.message)))
                    } else {
                        val clientSecret = extractClientSecretFromResponse(response)

                        clientSecret?.let { secret ->
                            continuation.resume(Result.success(secret))
                        } ?: run {
                            val error = Exception("Could not find payment intent client secret in response!")

                            continuation.resume(Result.failure(error))
                        }
                    }
                }
            })
    }

    private fun extractClientSecretFromResponse(response: Response): String? {
        return try {
            val responseData = response.body?.string()
            val responseJson = responseData?.let { JSONObject(it) } ?: JSONObject()

            responseJson.getString("clientSecret")
        } catch (exception: JSONException) {
            null
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this,  message, Toast.LENGTH_LONG).show()
        }
    }

    private fun onPayClicked(
        paymentSheet: PaymentSheet,
        paymentIntentClientSecret: String,
    ) {
        val configuration = PaymentSheet.Configuration.Builder(merchantDisplayName = "Example, Inc.")
            .build()

        // Present Payment Sheet
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration)
    }
}