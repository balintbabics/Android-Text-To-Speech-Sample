package balintbabics.tts.sample;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.stop_button)
    Button cancelButton;
    @BindView(R.id.speech_button)
    Button speakButton;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindString(R.string.on_start)
    String onStart;
    @BindString(R.string.on_complete)
    String onCompleted;
    @BindString(R.string.on_error)
    String onError;
    @BindString(R.string.tag)
    String TAG;

    private Unbinder unbinder;
    private TextToSpeech textToSpeech;
    private Map<String, TextToSpeechCallback> textToSpeechCallbackMap = new HashMap<>();

    //You can use predefined locale as well: `Locale.ENGLISH`, etc...
    private Locale locale = new Locale("hu", "HU");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);
        initTextToSpeech(this);

        speakButton.setOnClickListener(v -> onSpeakClick());
        cancelButton.setOnClickListener(v -> onCancelClick());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder != null) {
            unbinder.unbind();
        }
    }

    private void initTextToSpeech(Context context) {
        if(textToSpeech == null) {
            float textToSpeechPitch = 1.0f;
            float textToSpeechRate = 1.0f;

            textToSpeech = new TextToSpeech(context, textToSpeechOnInitListener);
            textToSpeech.setSpeechRate(textToSpeechRate);
            textToSpeech.setPitch(textToSpeechPitch);
            textToSpeech.setLanguage(locale);
        }
    }

    private TextToSpeech.OnInitListener textToSpeechOnInitListener = status -> {
    };

    private void onSpeakClick() {
        if(editText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.input_something, Toast.LENGTH_LONG).show();
            return;
        }

        say(editText.getText().toString().trim(), new TextToSpeechCallback() {

            @Override
            public void onStart() {
                Log.d(TAG, onStart);
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, onCompleted);
            }

            @Override
            public void onError() {
                Log.d(TAG, onError);
            }
        });
    }

    private void onCancelClick() {
        if(textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    public void say(String message, TextToSpeechCallback callback) {
        int textToSpeechQueueMode = TextToSpeech.QUEUE_FLUSH;

        String utteranceId = UUID.randomUUID().toString();

        if (callback != null) {
            textToSpeechCallbackMap.put(utteranceId, callback);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(message, textToSpeechQueueMode, null, utteranceId);
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
            textToSpeech.speak(message, textToSpeechQueueMode, params);
        }
    }
}
