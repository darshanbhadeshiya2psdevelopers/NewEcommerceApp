package com.db.newecom.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.db.newecom.R;
import com.db.newecom.ui.activity.AboutusActivity;
import com.db.newecom.ui.activity.ContactusActivity;
import com.db.newecom.ui.activity.FAQActivity;
import com.db.newecom.ui.activity.PolicyActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RelativeLayout rl_aboutus, rl_shareapp, rl_rateapp, rl_contactus, rl_faq, rl_payment, rl_termsofuse,
            rl_ppolicy, rl_rpolicy, rl_cpolicy;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        rl_aboutus = view.findViewById(R.id.rl_aboutus);
        rl_shareapp = view.findViewById(R.id.rl_shareapp);
        rl_rateapp = view.findViewById(R.id.rl_rateapp);
        rl_contactus = view.findViewById(R.id.rl_contactus);
        rl_faq = view.findViewById(R.id.rl_faq);
        rl_payment = view.findViewById(R.id.rl_payment);
        rl_termsofuse = view.findViewById(R.id.rl_termsofuse);
        rl_ppolicy = view.findViewById(R.id.rl_ppolicy);
        rl_rpolicy = view.findViewById(R.id.rl_rpolicy);
        rl_cpolicy = view.findViewById(R.id.rl_cpolicy);

        rl_aboutus.setOnClickListener(new ClickListener());
        rl_shareapp.setOnClickListener(new ClickListener());
        rl_rateapp.setOnClickListener(new ClickListener());
        rl_contactus.setOnClickListener(new ClickListener());
        rl_faq.setOnClickListener(new ClickListener());
        rl_payment.setOnClickListener(new ClickListener());
        rl_termsofuse.setOnClickListener(new ClickListener());
        rl_ppolicy.setOnClickListener(new ClickListener());
        rl_rpolicy.setOnClickListener(new ClickListener());
        rl_cpolicy.setOnClickListener(new ClickListener());

        return view;
    }

    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.rl_aboutus:
                    startActivity(new Intent(getActivity(), AboutusActivity.class));
                    break;

                case R.id.rl_shareapp:
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Best Ecommerce app download now. https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share App");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    break;

                case R.id.rl_rateapp:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
                    break;

                case R.id.rl_contactus:
                    startActivity(new Intent(getActivity(), ContactusActivity.class));
                    break;

                case R.id.rl_faq:
                    Intent faq_intent = new Intent(getActivity(),FAQActivity.class);
                    faq_intent.putExtra("type","faq");
                    startActivity(faq_intent);
                    break;

                case R.id.rl_payment:
                    Intent payment_intent = new Intent(getActivity(),FAQActivity.class);
                    payment_intent.putExtra("type","payment");
                    startActivity(payment_intent);
                    break;

                case R.id.rl_termsofuse:
                    Intent terms_intent = new Intent(getActivity(), PolicyActivity.class);
                    terms_intent.putExtra("policy_type", "term_of_use");
                    startActivity(terms_intent);
                    break;

                case R.id.rl_ppolicy:
                    Intent ppolicy_intent = new Intent(getActivity(), PolicyActivity.class);
                    ppolicy_intent.putExtra("policy_type", "privacy_policy");
                    startActivity(ppolicy_intent);
                    break;

                case R.id.rl_rpolicy:
                    Intent rpolicy_intent = new Intent(getActivity(), PolicyActivity.class);
                    rpolicy_intent.putExtra("policy_type", "refund_policy");
                    startActivity(rpolicy_intent);
                    break;

                case R.id.rl_cpolicy:
                    Intent cpolicy_intent = new Intent(getActivity(), PolicyActivity.class);
                    cpolicy_intent.putExtra("policy_type", "cancel_policy");
                    startActivity(cpolicy_intent);
                    break;
            }
        }
    }
}