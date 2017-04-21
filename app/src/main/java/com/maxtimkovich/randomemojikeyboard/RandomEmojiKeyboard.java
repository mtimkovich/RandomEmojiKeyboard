package com.maxtimkovich.randomemojikeyboard;

import android.inputmethodservice.InputMethodService;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.ImageButton;

import java.util.Random;

public class RandomEmojiKeyboard extends InputMethodService {
    boolean pressed = false;

    class Delete extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            InputConnection ic = getCurrentInputConnection();
            while (pressed) {
                ic.deleteSurroundingText(2, 0);

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    @Override
    public View onCreateInputView() {
        View v = getLayoutInflater().inflate(R.layout.keyboard, null);
        ImageButton backspace = (ImageButton) v.findViewById(R.id.backspace);
        backspace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!pressed) {
                            pressed = true;
                            new Delete().execute();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        pressed = false;
                        break;
                }
                return true;
            }
        });

        return v;
    }

    public void onKey(View view) {
        InputConnection ic = getCurrentInputConnection();

        switch (view.getId()) {
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
