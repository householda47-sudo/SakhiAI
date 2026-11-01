package com.example.sakhiai

/**
 * Replace this placeholder with a request to YOUR HTTPS backend.
 * Never embed a private model/API key in the APK.
 */
class AiRepository {
    suspend fun reply(userText: String): String {
        val text = userText.trim()
        if (text.isBlank()) return "कुछ पूछो ना! 😊"
        val lower = text.lowercase()
        if (listOf("खुदकुशी", "आत्महत्या", "मर जाना", "खुद को नुकसान").any { lower.contains(it) }) {
            return "मुझे दुख है कि तुम ऐसा महसूस कर रहे हो। अभी किसी भरोसेमंद बड़े व्यक्ति को बताओ और उनके साथ रहो। अगर तुरंत खतरा है, तो स्थानीय आपातकालीन सहायता लो।"
        }
        if (listOf("पासवर्ड", "घर का पता", "फोन नंबर", "स्कूल का पता").any { lower.contains(it) }) {
            return "अपनी निजी जानकारी, पासवर्ड या घर का पता चैट में साझा मत करो। 😊"
        }
        return when {
            lower.contains("नमस्ते") || lower.contains("hello") || lower.contains("हाय") ->
                "नमस्ते दोस्त! 💖 मैं सखी हूँ। आज तुम क्या सीखना या बात करना चाहोगे?"
            lower.contains("गणित") || lower.contains("math") ->
                "चलो गणित को छोटे-छोटे steps में सीखते हैं! अपना सवाल भेजो। ✏️"
            lower.contains("अंग्रेज़ी") || lower.contains("english") ->
                "बिल्कुल! आज हम 3 आसान English words सीख सकते हैं। कौन-सा विषय पसंद है?"
            else -> "मैं सुन रही हूँ! 😊 अभी मेरा AI कनेक्शन सेट नहीं हुआ है, इसलिए मैं सीमित जवाब दे सकती हूँ। ऐप के डेवलपर को सुरक्षित AI backend जोड़ना होगा।"
        }
    }
}
