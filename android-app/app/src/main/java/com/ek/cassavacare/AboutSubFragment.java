/*
 * Project: CassavaScan
 * File: AboutSubFragment.java
 * Description: Displays application information, model details, and credits
 *
 * Based on the open-source CassavaCare project
 * Original author: Emmanuel Kirui Barkacha (https://github.com/ekbarkacha)
 * License: MIT
 */

package com.ek.cassavacare;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.ek.cassavacare.R;

public class AboutSubFragment extends Fragment {

    // ---- Change these to your own details ----
    private static final String DEVELOPER_NAME = "Sunday Samuel";
    private static final String DEVELOPER_EMAIL = "samuelsundayiyanu@gmail.com";
    private static final String DEVELOPER_GITHUB = "https://github.com/Sunday-Samuel";
    // -------------------------------------------

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about_sub, container, false);

        TextView tvContent = root.findViewById(R.id.tv_about_content);

        tvContent.setText(Html.fromHtml(
                "<b>About CassavaScan</b><br>" +
                        "A mobile app that checks cassava leaves for common diseases, using a model that runs on your phone. " +
                        "It works offline once installed.<br><br>" +

                        "<b>What it does</b><br>" +
                        "• Scan a leaf with the camera, or choose a photo from your gallery<br>" +
                        "• Shows the likely disease and how confident the model is<br>" +
                        "• Gives simple advice for each result<br>" +
                        "• Keeps a history of your scans<br><br>" +

                        "<b>Limits</b><br>" +
                        "• Use photos of cassava leaves only<br>" +
                        "• Results are a guide, not a confirmed diagnosis<br>" +
                        "• Confirm any problem with an extension officer before acting<br><br>" +

                        "<b>Model</b><br>" +
                        "• Image model trained with Google Teachable Machine<br>" +
                        "• Test accuracy on held-out photos: 98.3% (same-source photos; field accuracy is still being measured)<br><br>" +

                        "<b>Developer</b><br>" +
                        DEVELOPER_NAME + "<br>" +
                        "Email: <a href=\"mailto:" + DEVELOPER_EMAIL + "\">" + DEVELOPER_EMAIL + "</a><br>" +
                        "GitHub: <a href=\"" + DEVELOPER_GITHUB + "\">" + DEVELOPER_GITHUB + "</a><br><br>" +

                        "<b>Credits</b><br>" +
                        "Based on the open-source CassavaCare project by Emmanuel Kirui Barkacha, released under the MIT licence.<br>" +
                        "Disease images: Kaggle cassava leaf disease competition data, and the Makerere University cassava image dataset (CC0)."
        ));
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());

        return root;
    }
}
