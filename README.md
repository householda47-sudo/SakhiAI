# Sakhi AI — Android starter project

A Hindi-first anime-style AI companion starter for Android.

## Included
- Jetpack Compose home/chat UI
- Hindi-friendly chat interface and friendly character persona
- Android speech recognition (tap mic) and Android text-to-speech
- Learning prompt shortcuts
- Basic child-safety system prompt and visible safety note
- No analytics, ads, account system, or conversation upload by default

## Important: this is source code, not a ready APK
An APK must be built and tested with Android Studio/Gradle. The AI reply function is a safe placeholder until you connect a backend. **Do not put a secret AI API key in the Android app.** Add a small server endpoint and call it from `AiRepository.kt`.

## Build
1. Install Android Studio (current stable) and Android SDK.
2. Open this folder in Android Studio.
3. Let Gradle sync.
4. Build > Build Bundle(s) / APK(s) > Build APK(s).

The app uses Android's on-device/system speech services where available. Speech recognition may send audio to the selected Android speech provider depending on device settings. TTS availability and voice quality vary by device.

## Connecting an AI model
Implement `AiRepository.reply()` to call your own HTTPS backend. Your backend should:
- keep API keys server-side;
- enforce age-appropriate moderation and rate limits;
- avoid storing chat/audio unless clearly disclosed and consented to;
- return short Hindi responses;
- reject sexual, exploitative, self-harm facilitation, dangerous, or privacy-invasive requests;
- encourage a trusted adult for serious or unsafe situations.

For real child-directed distribution, add a privacy policy, parental consent where required, data deletion controls, and review Google Play Families policies before publishing.
