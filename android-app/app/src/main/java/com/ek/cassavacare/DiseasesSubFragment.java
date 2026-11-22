/*
 * Project: CassavaScan
 * File: DiseasesSubFragment.java
 * Description: Educational information on common cassava diseases
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

public class DiseasesSubFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_diseases_sub, container, false);

        TextView tvContent = root.findViewById(R.id.tv_diseases_content);
        tvContent.setText(Html.fromHtml(
                "<b>Common cassava diseases</b><br><br>" +

                        "<b>1. Cassava Mosaic Disease (CMD)</b><br>" +
                        "&nbsp;&nbsp;• <b>Signs:</b> yellow and green mosaic patterns on leaves, leaves that curl or look smaller than normal, and stunted plants.<br>" +
                        "&nbsp;&nbsp;• <b>Cause:</b> cassava mosaic viruses, spread by whiteflies and infected cuttings.<br>" +
                        "&nbsp;&nbsp;• <b>Why it matters:</b> it can cut yields badly if left alone.<br><br>" +

                        "<b>2. Cassava Bacterial Blight (CBB)</b><br>" +
                        "&nbsp;&nbsp;• <b>Signs:</b> water-soaked, angular dark spots on leaves, wilting, and dieback of shoots.<br>" +
                        "&nbsp;&nbsp;• <b>Cause:</b> a bacterium that spreads through infected cuttings, rain splash, and contaminated tools.<br>" +
                        "&nbsp;&nbsp;• <b>Why it matters:</b> it weakens plants and reduces tuber quality.<br><br>" +

                        "<b>3. Cassava Brown Streak Disease (CBSD)</b><br>" +
                        "&nbsp;&nbsp;• <b>Signs:</b> pale yellow patches along leaf veins, brown streaks on stems, and brown rot inside roots.<br>" +
                        "&nbsp;&nbsp;• <b>Cause:</b> cassava brown streak viruses, spread by whiteflies and infected cuttings.<br>" +
                        "&nbsp;&nbsp;• <b>Why it matters:</b> roots can rot even when the leaves look only slightly sick.<br><br>" +

                        "<b>4. Cassava Green Mottle (CGM)</b><br>" +
                        "&nbsp;&nbsp;• <b>Signs:</b> light green or yellow mottling on leaves, sometimes with distorted growth.<br>" +
                        "&nbsp;&nbsp;• <b>Cause:</b> cassava green mottle virus, mainly spread through infected cuttings.<br>" +
                        "&nbsp;&nbsp;• <b>Why it matters:</b> it can lower leaf development and yields.<br><br>" +

                        "<i>Use the Scan screen to check a leaf. For a confirmed diagnosis, ask an extension officer. " +
                        "For trusted guidance, see IITA or NaCRRI.</i>"
        ));

        tvContent.setMovementMethod(LinkMovementMethod.getInstance());

        return root;
    }
}
