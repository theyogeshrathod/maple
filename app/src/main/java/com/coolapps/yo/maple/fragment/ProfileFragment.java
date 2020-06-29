package com.coolapps.yo.maple.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coolapps.yo.maple.LoginManager;
import com.coolapps.yo.maple.MapleDataModel;
import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.adapter.InterestsAdapter;
import com.coolapps.yo.maple.model.TagInterestsModel;
import com.coolapps.yo.maple.util.Countries;
import com.coolapps.yo.maple.util.EmailValidation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.coolapps.yo.maple.util.Constants.PREFERENCE_NAME;

/**
 * ProfileFragment for user profile details
 */

public class ProfileFragment extends BaseFragment {
    private static final String INTERESTS = "interests";
    private static final String TAG = "ProfileFragment";
    private static final String LOADING_TAG = "loading_tag";
    private static final String USER_ID = "userId";
    private static final String USER_NAME = "name";
    private static final String USER_IMAGE = "profileImage";
    private static final String USER_EMAIL = "email";
    private static final String USER_PHONE = "phone";
    private static final String USER_ADDRESS = "address";
    private static final String USER_STATE = "state";
    private static final String USER_COUNTRY = "country";
    private static final String OCCUPATION = "occupation";
    private static final String ABOUT_BUSINESS = "aboutBusiness";
    private static final String USER_PROFILE_KEY = "UserProfiles";
    private static final String SELECT_COUNTRY = "Select Your Country";
    private FirebaseAuth mAuth;
    private EditText mUserName;
    private EditText mUserEmail;
    private EditText mUserPhone;
    private EditText mUserAddress;
    private EditText mUserState;
    private EditText mAboutBusiness;
    private TextView mTextVerified; // we can put this field if needed or we can remove it
    private ImageView mUserImage;
    private TextView mSubmitProfile;
    private TextView mMyInterest;
    private RadioButton mRadioBusiness; // 1
    private RadioButton mRadioJob; // 2
    private TextView mChooseInterests;
    private int radioResult = 0;
    private String mUserId;
    private String mUserProfile = "";
    private LoadingFragment mLoadingFragment = LoadingFragment.newInstance();
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private List<String> mSelectedInterestNames = new ArrayList<>();
    private List<String> mSelectedInterestIds = new ArrayList<>();
    private String allIds = "";
    private String allInterests = "";
    private Map<String, Object> profileData = new HashMap<>();
    private List<String> countriesList = new ArrayList<>();
    private Spinner mSpinnerCountries;
    private String mSelectedCountry = "";
    private List<TagInterestsModel> interestsList;
    private List<String> tagIdList = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    private static String getCommaSeparatedString(String[] array) {
        String result = "";
        if (array.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : array) {
                sb.append(s).append(",");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;
    }

    private static String getCommaSpaceSeparatedString(String[] array) {
        String result = "";
        if (array.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : array) {
                sb.append(s).append(", ");
            }
            result = sb.delete(sb.length() - 2, sb.length() - 1).toString();
        }
        return result;
    }

    public static Fragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        final String authProvider = LoginManager.getAuthProvider();
        final FirebaseUser user = LoginManager.getLoggedInUser();
        if (user != null) {
            if (authProvider.equals("google.com")) {
                mUserEmail.setText(user.getEmail());
                mUserEmail.setEnabled(false);
            } else if (authProvider.equals("phone")) {
                mUserPhone.setText(user.getPhoneNumber());
                mUserPhone.setEnabled(false);
            }
        }
        mChooseInterests.setOnClickListener(v -> showInterestsSelectionPopup(interestsList));

        mSubmitProfile.setOnClickListener(v -> verifyInputs());
    }

    private void showInterestsSelectionPopup(List<TagInterestsModel> interestsList) {
        mSelectedInterestIds.clear();
        mSelectedInterestNames.clear();

        final View view = LayoutInflater.from(requireActivity()).inflate(R.layout.layout_pop_up_choose_interests, null, false);

        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view);
        builder.setCancelable(true);

        final RecyclerView interestRecycler = view.findViewById(R.id.chooseInterestList);
        final TextView doneText = view.findViewById(R.id.button_done);
        final AlertDialog alertDialog = builder.create();

        interestRecycler.setHasFixedSize(true);
        interestRecycler.setLayoutManager(new LinearLayoutManager(requireActivity()));

        final InterestsAdapter adapter = new InterestsAdapter(tagIdList);
        interestRecycler.setAdapter(adapter);

        adapter.setInterestsList(interestsList);

        adapter.setOnItemClick(new InterestsAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(TagInterestsModel tag) {
                mSelectedInterestIds.add(tag.getId());
                mSelectedInterestNames.add(tag.getTagName());
            }

            @Override
            public void onItemUncheck(TagInterestsModel tag) {
                mSelectedInterestIds.remove(tag.getId());
                mSelectedInterestNames.remove(tag.getTagName());
            }
        });

        doneText.setOnClickListener(v -> {
            if (mSelectedInterestIds.size() > 0) {
                alertDialog.dismiss();
                submitInterests(mSelectedInterestIds, mSelectedInterestNames);
            } else {
                Toast.makeText(requireActivity(), R.string.select_interest, Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }

    private void submitInterests
            (List<String> mSelectedInterestIds, List<String> mSelectedInterestNames) {
        String[] idsArray = mSelectedInterestIds.toArray(new String[0]);
        String[] namesArray = mSelectedInterestNames.toArray(new String[0]);

        allIds = getCommaSeparatedString(idsArray);
        allInterests = getCommaSpaceSeparatedString(namesArray);

        mMyInterest.setText(allInterests);
    }

    private void verifyInputs() {
        final String name = mUserName.getText().toString();
        final String email = mUserEmail.getText().toString();
        final String phone = mUserPhone.getText().toString();
        final String address = mUserAddress.getText().toString();
        final String state = mUserState.getText().toString();
        final String aboutBusiness = mAboutBusiness.getText().toString();

        if (name.isEmpty()) {
            mUserName.setError(getResources().getString(R.string.required));
            mUserName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            mUserEmail.setError(getResources().getString(R.string.required));
            mUserEmail.requestFocus();
            return;
        }

        if (!EmailValidation.checkEmail(email)) {
            Toast.makeText(requireActivity(), "Invalid Email-Address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.isEmpty()) {
            mUserPhone.setError(getResources().getString(R.string.required));
            mUserPhone.requestFocus();
            return;
        }

        if (mRadioBusiness.isChecked()) {
            radioResult = 1;
        } else if (mRadioJob.isChecked()) {
            radioResult = 2;
        }

        if (radioResult == 0) {
            Toast.makeText(requireActivity(), "Please select your occupation", Toast.LENGTH_SHORT).show();
            return;
        }

        if (address.isEmpty()) {
            mUserAddress.setError(getResources().getString(R.string.required));
            mUserAddress.requestFocus();
            return;
        }

        if (address.length() < 10) {
            Toast.makeText(requireActivity(), "Please Enter Detailed Address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (state.isEmpty()) {
            mUserState.setError(getResources().getString(R.string.required));
            mUserState.requestFocus();
            return;
        }

        if (state.length() <= 2) {
            Toast.makeText(requireActivity(), "Please Enter State", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mSelectedCountry.equals(SELECT_COUNTRY)) {
            Toast.makeText(requireActivity(), SELECT_COUNTRY, Toast.LENGTH_SHORT).show();
            return;
        }

        if (aboutBusiness.isEmpty()) {
            mAboutBusiness.setError(getResources().getString(R.string.required));
            mAboutBusiness.requestFocus();
            return;
        }

        if (aboutBusiness.length() < 10) {
            Toast.makeText(requireActivity(), R.string.min_10_chars_accepted, Toast.LENGTH_SHORT).show();
            return;
        }

        if (allIds.isEmpty()) {
            Toast.makeText(requireActivity(), "Please choose your interests.", Toast.LENGTH_SHORT).show();
            return;
        }

        submitData(name, email, phone, radioResult, allIds, aboutBusiness, address, state, mSelectedCountry);
    }

    private void submitData(String name, String email, String phone, int radioResult, String
            myInterestIds, String aboutBusiness, String address, String state, String country) {
        showLoadingFragment();

        final String id = mUserId;

        final Map<String, String> profileData = new HashMap<>();
        profileData.put(USER_ID, id);
        profileData.put(USER_NAME, name);
        profileData.put(USER_EMAIL, email);
        profileData.put(USER_PHONE, phone);
        profileData.put(USER_IMAGE, mUserProfile);
        profileData.put(USER_ADDRESS, address);
        profileData.put(USER_STATE, state);
        profileData.put(USER_COUNTRY, country);
        profileData.put(OCCUPATION, String.valueOf(radioResult));
        profileData.put(INTERESTS, myInterestIds);
        profileData.put(ABOUT_BUSINESS, aboutBusiness);

        mFirestore.collection(USER_PROFILE_KEY).document(id).set(profileData)
                .addOnSuccessListener(aVoid -> {
                    hideLoadingFragment();
                    Toast.makeText(requireActivity(), R.string.profile_update_success, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    hideLoadingFragment();
                    Toast.makeText(requireActivity(), R.string.profile_update_fail + "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void init(View view) {
        sharedPreferences = requireActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        mUserName = view.findViewById(R.id.etUserName);
        mUserEmail = view.findViewById(R.id.etEmail);
        mUserPhone = view.findViewById(R.id.etPhoneNumber);
        mUserAddress = view.findViewById(R.id.etAddress);
        mUserState = view.findViewById(R.id.etState);
        mAboutBusiness = view.findViewById(R.id.etAboutMyBusiness);
        mUserImage = view.findViewById(R.id.userImage);
        mTextVerified = view.findViewById(R.id.textVerified);
        mSubmitProfile = view.findViewById(R.id.submitProfile);
        mRadioBusiness = view.findViewById(R.id.rbBusiness);
        mMyInterest = view.findViewById(R.id.myInterests);
        mRadioJob = view.findViewById(R.id.rbJob);
        mChooseInterests = view.findViewById(R.id.chooseInterests);
        mSpinnerCountries = view.findViewById(R.id.spinnerCountry);

        countriesList.add(SELECT_COUNTRY);
        countriesList.addAll(Countries.getCountries());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, countriesList);
        mSpinnerCountries.setAdapter(adapter);

        mSpinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedCountry = countriesList.get(position);
                if (!selectedCountry.equals(SELECT_COUNTRY)) {
                    mSelectedCountry = selectedCountry;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * This method sets the profile data from firebase
     *
     * @param name          - user name
     * @param email         - user email
     * @param emailVerified - isEmailVerified
     * @param photoUrl      - photo url
     * @param profileData   - profileData from database
     */
    private void setUserProfileData(String name, String email, boolean emailVerified, Uri
            photoUrl, Map<String, Object> profileData) {

        if (profileData != null) {
            mUserName.setText((String) profileData.get(USER_NAME));
            mUserEmail.setText((String) profileData.get(USER_EMAIL));
            mUserPhone.setText((String) profileData.get(USER_PHONE));
            mUserAddress.setText((String) profileData.get(USER_ADDRESS));
            mUserState.setText((String) profileData.get(USER_STATE));
            mAboutBusiness.setText((String) profileData.get(ABOUT_BUSINESS));
            final String occupation = (String) profileData.get(OCCUPATION);
            final String country = (String) profileData.get(USER_COUNTRY);
            final String tagIds = (String) profileData.get(INTERESTS);

            if (tagIds != null) {
                tagIdList = Arrays.asList(tagIds.split(","));
                for (int i = 0; i < tagIdList.size(); i++) {
                    Log.d(TAG, "setUserProfileData: tags " + tagIdList.get(i));
                }
                String interests = "";

                final StringBuilder sb = new StringBuilder();

                for (int i = 0; i < tagIdList.size(); i++) {
                    String id = tagIdList.get(i);
                    for (int j = 0; j < interestsList.size(); j++) {
                        if (id.equals(interestsList.get(j).getId())) {
                            sb.append(interestsList.get(j).getTagName()).append(", ");
                        }
                    }
                }
                interests = sb.delete(sb.length() - 2, sb.length() - 1).toString();
                mMyInterest.setText(interests);

                Log.d(TAG, "setUserProfileData: interests " + interests);
            }

            mSpinnerCountries.setSelection(countriesList.indexOf(country));

            if (profileData.get(USER_IMAGE) != null) {
                Glide.with(requireActivity())
                        .load((String) profileData.get(USER_IMAGE))
                        .placeholder(R.drawable.person_icon)
                        .into(mUserImage);
            }

            if (occupation != null) {
                if (occupation.equals("1")) {
                    mRadioBusiness.setChecked(true);
                } else if (occupation.equals("2")) {
                    mRadioJob.setChecked(true);
                }
            }

        } else {
            mUserName.setText(name);
            mUserEmail.setText(email);

            if (emailVerified) {
                mTextVerified.setText(R.string.verified);
            } else {
                mTextVerified.setText(R.string.unverified);
            }

            Glide.with(requireActivity())
                    .load(photoUrl)
                    .placeholder(R.drawable.person_icon)
                    .into(mUserImage);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        final FirebaseUser currentUser = LoginManager.getLoggedInUser();
        if (currentUser != null) {
            getUserInfo(currentUser);
        }
    }

    /**
     * This method gets firebase user information
     */
    private void getUserInfo(FirebaseUser user) {
        final String name = user.getDisplayName();
        final String email = user.getEmail();
        final Uri photoUrl = user.getPhotoUrl();

        final boolean emailVerified = user.isEmailVerified();
        mUserId = user.getUid();
        if (photoUrl != null) {
            mUserProfile = photoUrl.toString();
        }

        interestsList = new ArrayList<>(MapleDataModel.getInstance().getAvailableTags());

        profileData = MapleDataModel.getInstance().fetchProfileData(mUserId);

        Log.d(TAG, "getUserInfo: profileData " + profileData);

        setUserProfileData(name, email, emailVerified, photoUrl, profileData);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateToolbar();
    }

    private void updateToolbar() {
        setToolbarTitle(R.string.profile);
    }

    private void showLoadingFragment() {
        if (!isStateSaved() && !mLoadingFragment.isAdded()) {
            mLoadingFragment.show(getChildFragmentManager(), LOADING_TAG);
        }
    }

    private void hideLoadingFragment() {
        if (mLoadingFragment.isAdded()) {
            mLoadingFragment.dismissAllowingStateLoss();
        }
    }
}
