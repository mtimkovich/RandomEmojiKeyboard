package com.maxtimkovich.randomemojikeyboard;

import android.graphics.Paint;
import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import java.util.Random;

public class RandomEmojiKeyboard extends InputMethodService {

    @Override
    public View onCreateInputView() {
        View v = getLayoutInflater().inflate(R.layout.keyboard, null);

        return v;
    }

    public void onKey(View view) {
        InputConnection ic = getCurrentInputConnection();

        switch (view.getId()) {
            case R.id.backspace:
                // Emojis are rendered as 2 characters, delete twice for emojis
                // and once for normal characters
                CharSequence prev = ic.getTextBeforeCursor(2, 0);

                if (prev.length() > 1  &&
                        Character.isSurrogatePair(prev.charAt(0), prev.charAt(1))) {
                    ic.deleteSurroundingText(2, 0);
                } else {
                    ic.deleteSurroundingText(1, 0);
                }
                break;
            case R.id.enter:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                int[][] emojiInts = {
                        {0x1F600, 0x1F64F}, // emoticons
                        {0x1F300, 0x1F5FF}, // symbols and pictographs
                        {0x1F680, 0x1F6FF}, // transport and maps
                        {0x1F1E0, 0x1F1FF}, // flags
                };

                // Loop until we find a valid emoji
                // I'm not sure if there's a more efficient way to do this
                while (true) {
                    Random rand = new Random();

                    int type = rand.nextInt(emojiInts.length);
                    int[] category = emojiInts[type];
                    int which = rand.nextInt(category[1] - category[0] + 1) + category[0];

                    StringBuffer sb = new StringBuffer();
                    sb.append(Character.toChars(which));

                    String emoji = new String(sb);

                    Paint paint = new Paint();

                    if (paint.hasGlyph(emoji)) {
                        ic.commitText(emoji, 1);
                        break;
                    }
                }
        }

    }
}
