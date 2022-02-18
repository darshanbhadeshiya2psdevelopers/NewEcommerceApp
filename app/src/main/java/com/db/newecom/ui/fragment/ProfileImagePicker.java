package com.db.newecom.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.db.newecom.R;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.util.ArrayList;


public class ProfileImagePicker extends BottomSheetDialogFragment {

    private Method method;
    private String imageProfile;
    private ArrayList<Image> galleryImages;
    private int REQUESTGALLERYPICKER = 100;
    private LinearLayout llRemove, llImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_image_picker, container, false);

        method = new Method(getActivity());

        llRemove = view.findViewById(R.id.ll_remove);
        llImage = view.findViewById(R.id.ll_image);

        llRemove.setOnClickListener(v -> {
            Events.ProImage proImage = new Events.ProImage("", "", false, true);
            GlobalBus.getBus().post(proImage);
            dismiss();
        });

        llImage.setOnClickListener(v -> chooseGalleryImage());

        return view;
    }

    private void chooseGalleryImage() {
        try {
//            String color;
//            String color_progressBar;
//            if (method.isDarkMode()) {
//                color = ConstantApi.darkGallery;
//                color_progressBar = ConstantApi.progressBarDarkGallery;
//            } else {
//                color = ConstantApi.lightGallery;
//                color_progressBar = ConstantApi.progressBarLightGallery;
//            }
            ImagePicker.with(this)
                    .setFolderMode(true)
                    .setFolderTitle("Album")
                    .setImageTitle(getResources().getString(R.string.app_name))
                    .setMultipleMode(true)
                    .setMaxSize(1)
                    .setShowCamera(false)
                    .start();
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == Activity.RESULT_OK && requestCode == REQUESTGALLERYPICKER) {
            galleryImages = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            assert galleryImages != null;
            imageProfile = galleryImages.get(0).getPath();
            Events.ProImage proImage = new Events.ProImage("", imageProfile, true, false);
            GlobalBus.getBus().post(proImage);
            dismiss();
        }
    }
}