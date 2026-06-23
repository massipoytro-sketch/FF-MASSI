package com.example

object NameGenerator {

    private val prefixes = listOf(
        "꧁", "★", "☠", "♔", "ツ", "亗", "♡", "٭", "シ", "メ", "『", "【", "༺", "❖", "♣", "♦", "♠", "♥", "ッ", "〆", "〇", "࿐", "╰‿╯"
    )

    private val suffixes = listOf(
        "꧂", "★", "☠", "♔", "ツ", "亗", "♡", "٭", "シ", "メ", "』", "】", "༻", "❖", "♣", "♦", "♠", "♥", "ッ", "〆", "〇", "࿐", "╰‿╯"
    )
    
    private val matchedPairs = listOf(
        Pair("꧁", "꧂"),
        Pair("꧁༺", "༻꧂"),
        Pair("『", "』"),
        Pair("【", "】"),
        Pair("★", "★"),
        Pair("☠", "☠"),
        Pair("♔", "♔"),
        Pair("❖", "❖"),
        Pair("╰‿╯", "╰‿╯"),
        Pair("♡", "♡"),
        Pair("«", "»"),
        Pair("⫷", "⫸"),
        Pair("◥", "◤")
    )

    private val arMiddleDecorations = listOf("ـ", "ٰ", "ـہ", "ـہہـ", "๛", "彡")

    private val enCharMap = mapOf(
        'a' to listOf("α", "ａ", "å", "ä", "Λ", "∀", "卂"),
        'b' to listOf("ß", "ｂ", "♭", "в", "乃"),
        'c' to listOf("¢", "ｃ", "©", "☾", "匚"),
        'd' to listOf("∂", "ｄ", "Ð", "đ", "ᗪ"),
        'e' to listOf("є", "ｅ", "£", "é", "ê", "乇"),
        'f' to listOf("ƒ", "ｆ", "₣", "℉", "千"),
        'g' to listOf("ｇ", "ǥ", "ğ", "ɢ", "Ꮆ"),
        'h' to listOf("ｈ", "н", "ђ", "ɦ", "卄"),
        'i' to listOf("ι", "ｉ", "!", "ï", "丨"),
        'j' to listOf("ｊ", "נ", "ʝ", "ჟ", "ﾌ"),
        'k' to listOf("ｋ", "к", "₭", "κ", "Ҝ"),
        'l' to listOf("ｌ", "ℓ", "£", "|", "ㄥ"),
        'm' to listOf("ｍ", "м", "๓", "ʍ", "爪"),
        'n' to listOf("ｎ", "и", "ñ", "η", "几"),
        'o' to listOf("ｏ", "ø", "σ", "θ", "ㄖ"),
        'p' to listOf("ｐ", "ρ", "℗", "þ", "卩"),
        'q' to listOf("ｑ", "q", "ℚ", "ⓠ", "Ɋ"),
        'r' to listOf("ｒ", "я", "®", "ř", "尺"),
        's' to listOf("ｓ", "ѕ", "$", "§", "丂"),
        't' to listOf("ｔ", "т", "†", "τ", "ㄒ"),
        'u' to listOf("ｕ", "υ", "µ", "ü", "ㄩ"),
        'v' to listOf("ｖ", "ν", "v", "ⓥ", "ᐯ"),
        'w' to listOf("ｗ", "ω", "ш", "ώ", "山"),
        'x' to listOf("ｘ", "×", "χ", "x", "乂"),
        'y' to listOf("ｙ", "у", "¥", "ÿ", "ㄚ"),
        'z' to listOf("ｚ", "z", "ž", "ż", "乙")
    )

    fun generateNames(baseName: String, count: Int): List<String> {
        val generated = mutableSetOf<String>()
        val maxAttempts = count * 20
        var attempts = 0

        while (generated.size < count && attempts < maxAttempts) {
            val isEnglish = baseName.any { it in 'a'..'z' || it in 'A'..'Z' }
            var mutatedName = baseName

            if (isEnglish) {
                val sb = StringBuilder()
                for (char in baseName.lowercase()) {
                    if (enCharMap.containsKey(char) && Math.random() > 0.4) {
                        val replacements = enCharMap[char]!!
                        sb.append(replacements.random())
                    } else {
                        sb.append(char)
                    }
                }
                mutatedName = sb.toString()
                
                // Capitalize randomly
                if (Math.random() > 0.5) {
                    mutatedName = mutatedName.uppercase()
                }
            } else {
                // Mutate Arabic by adding middle decorations
                if (mutatedName.length > 2 && Math.random() > 0.5) {
                    val insertPos = (1 until mutatedName.length).random()
                    mutatedName = mutatedName.substring(0, insertPos) + arMiddleDecorations.random() + mutatedName.substring(insertPos)
                }
            }

            // Mix in some inner symbols occasionally
            if (Math.random() > 0.7) {
                 val innerSymbol = listOf("ツ", "メ", "〆", "×").random()
                 mutatedName = "$mutatedName $innerSymbol"
            }

            val finalName = if (Math.random() > 0.5) {
                val pair = matchedPairs.random()
                "${pair.first}$mutatedName${pair.second}"
            } else {
                val p = if (Math.random() > 0.3) prefixes.random() else ""
                val s = if (Math.random() > 0.3) suffixes.random() else ""
                "$p$mutatedName$s".trim()
            }

            if (finalName.isNotBlank() && finalName != baseName) {
                generated.add(finalName)
            }
            attempts++
        }

        // Fill up remaining if unique generations exhausted (fallback)
        while(generated.size < count) {
            val p = prefixes.random()
            val s = suffixes.random()
            val fallbackName = "$p$baseName$s"
            generated.add(fallbackName + " ".repeat((generated.size % 10) + 1))
        }

        return generated.toList()
    }
}
