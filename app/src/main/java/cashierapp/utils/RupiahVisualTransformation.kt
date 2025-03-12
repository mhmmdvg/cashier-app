package cashierapp.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class RupiahVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text

        // Filter to only include digits
        val digitsOnly = originalText.filter { it.isDigit() }

        // Create the formatted text with Rupiah prefix
        val formattedText = if (digitsOnly.isEmpty()) {
            "Rp 0"
        } else {
            val number = digitsOnly.toLongOrNull() ?: 0
            "Rp ${number.toDecimalFormat()}"
        }

        // Create a more precise offset mapping
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Map the original cursor position to the transformed text
                if (originalText.isEmpty()) return "Rp 0".length

                // Count dots added before the cursor position
                val digitsBeforeCursor = originalText.take(offset).count { it.isDigit() }
                val dotsAdded = if (digitsOnly.isNotEmpty()) {
                    (digitsOnly.length - 1) / 3
                } else {
                    0
                }

                // Calculate position: "Rp " prefix (3 chars) + digits + dots
                return 3 + digitsBeforeCursor + (dotsAdded.coerceAtMost(digitsBeforeCursor / 3))
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Map transformed cursor position back to original text
                if (offset <= 3) return 0 // "Rp " prefix
                if (originalText.isEmpty()) return 0

                // Adjust for the prefix and dots
                val cursorAfterPrefix = (offset - 3).coerceAtLeast(0)

                // Count dots before the cursor in the transformed text
                val dotsBeforeCursor = formattedText
                    .substring(3, minOf(offset, formattedText.length))
                    .count { it == '.' }

                // Original position = position after prefix - dots added
                val originalPos = cursorAfterPrefix - dotsBeforeCursor

                // Ensure the position is within the bounds of the original text
                return originalPos.coerceIn(0, originalText.length)
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}

// Extension function to format numbers with thousand separators
fun Long.toDecimalFormat(): String {
    return String.format("%,d", this).replace(",", ".")
}