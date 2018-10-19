package org.androidtown.MajorBasicProject2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    public String id_target;
    public String id_user;
    public boolean isEditMode;

    ProfileHeaderFragment headerFragment;
    ProfilePrivateFragment privateFragment;
    ProfileTailFragment tailFragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_m5_profile, container, false);

        id_user = this.getArguments().getString("id_user");
        id_target = this.getArguments().getString("id_target");
        isEditMode = this.getArguments().getBoolean("isEditMode");

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        headerFragment =  new ProfileHeaderFragment();
        privateFragment = new ProfilePrivateFragment();
        tailFragment = new ProfileTailFragment();

        android.support.v4.app.FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (isEditMode) {
            transaction.add(R.id.profileHeaderContainer, headerFragment);
            transaction.add(R.id.profileTailContainer, tailFragment);
        }
        transaction.add(R.id.profilePrivateContainer, privateFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        isEditMode = false;
    }

}