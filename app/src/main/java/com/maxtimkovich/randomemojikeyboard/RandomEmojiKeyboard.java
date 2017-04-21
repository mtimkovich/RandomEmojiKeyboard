package com.maxtimkovich.randomemojikeyboard;

import android.graphics.Paint;
import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomEmojiKeyboard extends InputMethodService {
    List<Emoji> emojis;

    @Override
    public View onCreateInputView() {
        View v = getLayoutInflater().inflate(R.layout.keyboard, null);
        emojis = new ArrayList<>(EmojiManager.getAll());

        return v;
    }

    public String getRandomEmoji() {
        Random rng = new Random();
        Paint paint = new Paint();
        String emoji;

        // Get random emojis until get one that is compatible with device
        do {
            int randIndex = rng.nextInt(emojis.size());
            emoji = emojis.get(randIndex).getUnicode();
        } while (!paint.hasGlyph(emoji));

        return emoji;
    }

    public void onKey(View view) {
        InputConnection ic = getCurrentInputConnection();
        ic.commitText("", 1);

        switch (view.getId()) {
            case R.id.backspace:
                // Emojis are rendered as 2 characters, delete twice for emojis
                // and once for normal characters
                CharSequence prev = ic.getTextBeforeCursor(2, 0);

                if (EmojiManager.isEmoji(prev.toString())) {
                    ic.deleteSurroundingText(2, 0);
                } else {
                    ic.deleteSurroundingText(1, 0);
                }
                break;
            case R.id.enter:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                String emoji = getRandomEmoji();
                ic.commitText(emoji, 1);
        }

    }
}
