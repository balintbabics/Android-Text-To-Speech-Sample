package balintbabics.tts.sample;

interface TextToSpeechCallback {
    void onStart();
    void onCompleted();
    void onError();
}
