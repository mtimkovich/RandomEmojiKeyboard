package com.maxtimkovich.randomemojikeyboard;

import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import java.util.Random;

public class RandomEmojiKeyboard extends InputMethodService {
    @Override
    public View onCreateInputView() {
        View v = getLayoutInflater().inflate(R.layout.keyboard, null);
        v.findViewById(R.id.backspace);

        return v;
    }

    public void onKey(View view) {
        InputConnection ic = getCurrentInputConnection();

        switch (view.getId()) {
            case R.id.backspace:
                ic.deleteSurroundingText(2, 0);
                break;
            case R.id.enter:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                int[][] emojiInts = {
                        {0x1F600, 0x1F64F}, // emoticons
                        {0x1F300, 0x1F5FF}, // symbols and pictographs
//                        {0x1F680, 0x1F6FF}, // transport and maps
//                        {0x1F1E0, 0x1F1FF}, // flags
                };

                Random rand = new Random();

                int type = rand.nextInt(emojiInts.length);
                int[] category = emojiInts[type];
                int which = rand.nextInt(category[1] - category[0] + 1) + category[0];

                StringBuffer emoji = new StringBuffer();
                emoji.append(Character.toChars(which));

                ic.commitText(new String(emoji), 1);
        }

    }
}
