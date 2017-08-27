
package com.whl.client.utils;


import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Handler;
import android.text.Editable;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.whl.client.R;

import java.util.List;
import java.util.Locale;


public class KeyboardUtil {

	private KeyboardView keyboardView;
	private View mKeyboarLayout;

	private Keyboard k1;// 字母键盘
	private Keyboard k2;// 数字键盘
	private Keyboard k3;// 符号键盘
	public boolean isnun = false;// 是否数据键盘
	public boolean isupper = false;// 是否大写
	public boolean isSymbos = false;// 是否是符号键盘
	private OnKeyboardInputListener inputListener;
	private EditText editText;
	private StringBuffer inputStrBuffer = new StringBuffer();
	private boolean isShowing = false;
	private LinearLayout mLlyTitle;
	private OnKeyboradStateChangeListener stateChangeListener;

	public KeyboardUtil(Activity act, Context ctx, EditText edit) {
		this.editText = edit;
		k1 = new Keyboard(ctx, R.xml.qwerty);
		k2 = new Keyboard(ctx, R.xml.symbols);
		k3 = new Keyboard(ctx, R.xml.symbols_2);
		keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
		mKeyboarLayout = act.findViewById(R.id.keyboard_layout);
		keyboardView.setKeyboard(k1);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(true);
		keyboardView.setOnKeyboardActionListener(listener);

		mLlyTitle = (LinearLayout) act.findViewById(R.id.title);

		mLlyTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				forceHideKeyboard();
			}
		});
	}

	public void setListener(OnKeyboardInputListener listener) {
		this.inputListener = listener;
	}

	public void setStateChangeListener(OnKeyboradStateChangeListener listener) {
		this.stateChangeListener = listener;
	}
	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {

		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
			if (primaryCode != 0) {
				keyboardView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
						HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
								| HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
			}

			if (primaryCode == 924924) {
				keyboardView.setPreviewEnabled(false);
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {
				keyboardView.setPreviewEnabled(false);
			} else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
				keyboardView.setPreviewEnabled(false);
			} else if (primaryCode == Keyboard.KEYCODE_CANCEL) {
				keyboardView.setPreviewEnabled(false);
			} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
				keyboardView.setPreviewEnabled(false);
			} else if (primaryCode == 32) {
				// space
				keyboardView.setPreviewEnabled(false);
			} else {
				keyboardView.setPreviewEnabled(true);
			}
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			// editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
			Editable editable = editText.getText();
			int start = editText.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
				forceHideKeyboard();
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
				if (editable != null && editable.length() > 0) {
					if (inputStrBuffer.length() == 0) {
						inputStrBuffer.append(editText.getText().toString().trim());
					}
					if (start > 0) {
						editable.delete(start - 1, start);
						inputStrBuffer.delete(start - 1, start);
						if (inputListener != null) {
							inputListener.onKeyboardInput(inputStrBuffer.toString());
						}
					}
				}
			} else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
				changeKey();
				keyboardView.setKeyboard(k1);

			} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
				if (isnun) {
					isnun = false;
					keyboardView.setKeyboard(k1);
				} else {
					isnun = true;
					keyboardView.setKeyboard(k2);
				}
			} else if (primaryCode == 924924) {// show symbols_2
				if (isSymbos) {
					isSymbos = false;
					keyboardView.setKeyboard(k2);
				} else {
					isSymbos = true;
					keyboardView.setKeyboard(k3);
				}
			} else if (primaryCode == 57419) { // go left
				if (start > 0) {
					editText.setSelection(start - 1);
				}
			} else if (primaryCode == 57421) { // go right
				if (start < editText.length()) {
					editText.setSelection(start + 1);
				}
			} else {
				if (primaryCode == 9249241) {
				} else {
					// editable.insert(start, Character.toString((char) primaryCode));
					if (editText.getText().toString().trim().length() == 0) {
						inputStrBuffer.delete(0, inputStrBuffer.length());
					}
					inputStrBuffer.append(Character.toString((char) primaryCode));
					if (inputListener != null) {
						inputListener.onKeyboardInput(inputStrBuffer.toString());
					}
					editable.insert(start, Character.toString((char) primaryCode));
				}
			}
		}
	};

	/**
	 * 键盘大小写切换
	 */
	private void changeKey() {
		List<Key> keylist = k1.getKeys();
		if (isupper) {// 大写切换小写
			isupper = false;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toLowerCase(Locale.getDefault());
					key.codes[0] = key.codes[0] + 32;
				}
			}
		} else {// 小写切换大写
			isupper = true;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toUpperCase(Locale.getDefault());
					key.codes[0] = key.codes[0] - 32;
				}
			}
		}
	}

	public void setDefaultValue(String defaultValue) {
		inputStrBuffer.delete(0, inputStrBuffer.length());
		inputStrBuffer.append(defaultValue);
	}

	// public void clearInputCache() {
	// MLog.d(TAG, "clearCache");
	// // inputStrBuffer.delete(0, inputStrBuffer.length());
	// }

	public void showKeyboard() {
		int visibility = mKeyboarLayout.getVisibility();
		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					mKeyboarLayout.setVisibility(View.VISIBLE);
					isShowing = true;
					if (stateChangeListener != null) {
						stateChangeListener.onChange(false);
					}
				}
			}, 100);
		}
	}

	public boolean isShowing() {
		int visibility = mKeyboarLayout.getVisibility();
		if (visibility == View.VISIBLE) {
			return true;
		} else {
			return false;
		}
	}

	public void hideKeyboard() {
		int visibility = mKeyboarLayout.getVisibility();
		if (visibility == View.VISIBLE) {
			mKeyboarLayout.setVisibility(View.GONE);
			isShowing = false;
			if (stateChangeListener != null) {
				stateChangeListener.onChange(true);
			}
		}
	}

	public void forceHideKeyboard() {
		mKeyboarLayout.setVisibility(View.GONE);
		isShowing = false;
		if (stateChangeListener != null) {
			stateChangeListener.onChange(true);
		}
	}

	public void changeInputEdt(EditText newEdt) {
		this.editText = newEdt;
		keyboardView.setOnKeyboardActionListener(listener);
	}

	private boolean isword(String str) {
		String wordstr = "abcdefghijklmnopqrstuvwxyz";
		if (wordstr.indexOf(str.toLowerCase(Locale.getDefault())) > -1) {
			return true;
		}
		return false;
	}

	public String getInputedStr() {
		return inputStrBuffer.toString().trim();
	}
	public interface OnKeyboardInputListener {

		void onKeyboardInput(String inputString);
	}
	public interface OnKeyboradStateChangeListener {

		void onChange(boolean isHiden);
	}
}
