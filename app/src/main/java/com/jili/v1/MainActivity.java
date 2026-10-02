package com.jili.v1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {
    private TextView status, conversation;
    private EditText input;
    private TextToSpeech tts;
    private static final int REQ_VOICE = 100;
    private static final int REQ_PERM = 101;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        status = findViewById(R.id.status);
        conversation = findViewById(R.id.conversation);
        input = findViewById(R.id.input);

        tts = new TextToSpeech(this, r -> {
            if (r == TextToSpeech.SUCCESS) tts.setLanguage(new Locale("fa", "IR"));
        });

        findViewById(R.id.btnVoice).setOnClickListener(v -> startVoice());
        findViewById(R.id.btnSend).setOnClickListener(v -> handleCommand(input.getText().toString().trim()));
        findViewById(R.id.btnPermission).setOnClickListener(v -> requestCorePermissions());
        findViewById(R.id.btnSync).setOnClickListener(v -> {
            status.setText("🟢 Local Sync آماده است؛ Cloud در نسخه متصل فعال می‌شود.");
            speak("وضعیت همگام سازی بررسی شد");
        });
        findViewById(R.id.btnSettings).setOnClickListener(v ->
            Toast.makeText(this, "تنظیمات پایه Jili آماده است.", Toast.LENGTH_SHORT).show());
    }

    private void startVoice() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa-IR");
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        try { startActivityForResult(i, REQ_VOICE); }
        catch (Exception e) { status.setText("تشخیص گفتار روی این دستگاه در دسترس نیست."); }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_VOICE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> r = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (r != null && !r.isEmpty()) handleCommand(r.get(0));
        }
    }

    private void handleCommand(String cmd) {
        if (cmd.isEmpty()) return;
        conversation.setText("شما: " + cmd + "\n\nJili: فرمان دریافت شد. در نسخه V1، اجرای امن کارها بعد از تأیید شما انجام می‌شود.");
        status.setText("🟢 فرمان دریافت شد");
        speak("فرمان شما دریافت شد");
        input.setText("");
    }

    private void requestCorePermissions() {
        ArrayList<String> p = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) p.add(Manifest.permission.RECORD_AUDIO);
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) p.add(Manifest.permission.READ_CONTACTS);
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) p.add(Manifest.permission.CALL_PHONE);
            if (checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) p.add(Manifest.permission.READ_CALENDAR);
            if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) p.add(Manifest.permission.WRITE_CALENDAR);
        }
        if (p.isEmpty()) Toast.makeText(this, "دسترسی‌های اصلی فعال هستند.", Toast.LENGTH_SHORT).show();
        else requestPermissions(p.toArray(new String[0]), REQ_PERM);
    }

    private void speak(String s) {
        if (tts != null) tts.speak(s, TextToSpeech.QUEUE_FLUSH, null, "jili");
    }

    @Override protected void onDestroy() {
        if (tts != null) { tts.stop(); tts.shutdown(); }
        super.onDestroy();
    }
}
