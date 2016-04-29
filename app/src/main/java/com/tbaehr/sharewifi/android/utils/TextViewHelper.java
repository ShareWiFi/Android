package com.tbaehr.sharewifi.android.utils;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

public class TextViewHelper {

    /**
     * This attachs an OnLinkClickListener to a textView that contains a html-formatted link.
     *
     * @param textView The TextView where a listener should be attached on the clickable link
     * @param listener The listener that is called when the link is clicked
     */
    public static void attachOnLinkClickListener(final TextView textView, final IOnLinkClickListener listener) {
        final CharSequence text = textView.getText();
        Spanned html = Html.fromHtml(text.toString());
        SpannableStringBuilder builder = new SpannableStringBuilder(html);
        URLSpan[] urlSpans = builder.getSpans(0, text.length(), URLSpan.class);

        for (final URLSpan span : urlSpans) {
            int start = builder.getSpanStart(span);
            int end = builder.getSpanEnd(span);
            int flags = builder.getSpanFlags(span);

            ClickableSpan clickableSpan = new ClickableSpan() {
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(view, span.getURL());
                    }

                    // Unhighlight the link, remove the current listener and readd the listener
                    // This will give the user a permanent touch feedback (instead only once)
                    TextView telekomTextView = ((TextView) view);
                    telekomTextView.clearComposingText();
                    attachOnLinkClickListener(textView, listener);
                }

                /*public void updateDrawState(TextPaint textPaint) {
                    super.updateDrawState(textPaint);
                    textPaint.setUnderlineText(true);
                    textPaint.setColor...
                }*/
            };
            builder.setSpan(clickableSpan, start, end, flags);
            builder.removeSpan(span);
        }

        textView.setText(builder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}