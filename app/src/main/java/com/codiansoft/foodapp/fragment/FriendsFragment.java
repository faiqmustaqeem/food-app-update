package com.codiansoft.foodapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codiansoft.foodapp.FriendsActivitiesDetailsActivity;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.RecyclerTouchListener;
import com.codiansoft.foodapp.adapter.FriendActivityAdapter;
import com.codiansoft.foodapp.adapter.FriendsContactListAdapter;
import com.codiansoft.foodapp.dialog.UploadActivityPostDialog;
import com.codiansoft.foodapp.model.FriendsActivitiesModel;
import com.codiansoft.foodapp.model.FriendsContactsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Codiansoft on 9/25/2017.
 */

public class FriendsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    public static final String TAG = "FriendsFragment";
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    View view;
    FriendActivityAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rvFriendsActivities;
    ArrayList<FriendsActivitiesModel> friendsActivitiesModelArrayList;
    FriendsActivitiesModel friendsActivitiesModel;
    FloatingActionButton fabNewPost, fabNewPostCamera, fabNewPostGallery;
    Animation cameraIconSlideDown, cameraIconSlideUp, galleryIconSlideLeft, galleryIconSlideRight;

    ContentValues values;
    public static Uri newPostImageUri;
    public static Bitmap newPostBitmap;

    String[] mProjection;
    Cursor cursor;
    FriendsContactListAdapter friendsContactListAdapter;

    ProgressDialog progressDialog;
    List<FriendsContactsModel> contactVOList;

    int lastContactListItem;

    TextView tvUserName, tvActivityTitle, tvDateAndTime;
    ImageView ivProfilePic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
/*        EditText v = new EditText(getActivity());
        v.setText("Hello Fragment!");*/
        view = inflater.inflate(R.layout.fragment_friends_activities, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.setProgressPercentFormat(null);
        progressDialog.show();

        lastContactListItem = 0;

        initUI();

/*        mProjection = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Photo.PHOTO
        };

        cursor = getActivity().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                mProjection,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
        );

        friendsContactListAdapter = new FriendsContactListAdapter(getContext(), cursor);

        rvFriendsActivities.setAdapter(adapter);*/

        rvFriendsActivities.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvFriendsActivities, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                FriendsContactsModel friendsContactsModel = contactVOList.get(position);
                Toast.makeText(getActivity(), friendsContactsModel.getContactNumber() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), FriendsActivitiesDetailsActivity.class);
                i.putExtra("UserID", friendsContactsModel.getContactNumber());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    private void getAllContacts() {
        contactVOList = new ArrayList();
        FriendsContactsModel contactVO;

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

//        cursor.moveToPosition(lastContactListItem);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                if (contactVOList.size() >= 10) {
                    lastContactListItem = cursor.getPosition();
                    break;
                }

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contactVO = new FriendsContactsModel();
                    contactVO.setContactName(name);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactVO.setContactNumber(phoneNumber);
                    }

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }
                    contactVOList.add(contactVO);
                }
            }

            FriendsContactListAdapter contactAdapter = new FriendsContactListAdapter(contactVOList, getActivity());
            rvFriendsActivities.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvFriendsActivities.setAdapter(contactAdapter);

            progressDialog.dismiss();
        }
    }

    private void initUI() {
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvActivityTitle = (TextView) view.findViewById(R.id.tvActivityTitle);
        tvDateAndTime = (TextView) view.findViewById(R.id.tvDateAndTime);
        ivProfilePic = (ImageView) view.findViewById(R.id.ivProfilePic);
        tvUserName.setOnClickListener(this);
        tvActivityTitle.setOnClickListener(this);
        tvDateAndTime.setOnClickListener(this);
        ivProfilePic.setOnClickListener(this);

        fabNewPost = (FloatingActionButton) view.findViewById(R.id.fabNewPost);
        fabNewPost.bringToFront();
        fabNewPost.setOnClickListener(this);
        fabNewPostCamera = (FloatingActionButton) view.findViewById(R.id.fabNewPostCamera);
        fabNewPostCamera.setOnClickListener(this);
        fabNewPostGallery = (FloatingActionButton) view.findViewById(R.id.fabNewPostGallery);
        fabNewPostGallery.setOnClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        rvFriendsActivities = (RecyclerView) view.findViewById(R.id.rvFriendsActivities);

        cameraIconSlideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.camera_icon_slide_up);
        cameraIconSlideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.camera_icon_slide_down);

        galleryIconSlideLeft = AnimationUtils.loadAnimation(getActivity(), R.anim.gallery_icon_slide_left);
        galleryIconSlideRight = AnimationUtils.loadAnimation(getActivity(), R.anim.gallery_icon_slide_right);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        rvFriendsActivities.setLayoutManager(layoutManager);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        getAllContacts();

                                        /*new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                // background code

                                            }
                                        }).start();*/


                                        /*friendsActivitiesModelArrayList = new ArrayList<>();

                                        friendsActivitiesModel = new FriendsActivitiesModel("1", "M.A Jinnah", "http://images4.fanpop.com/image/photos/20000000/q-a-quaid-e-azam-20073224-388-550.jpg", "Having lunch at McDonalds", "8-12-22", "2:32 pm");
                                        friendsActivitiesModelArrayList.add(friendsActivitiesModel);
                                        friendsActivitiesModel = new FriendsActivitiesModel("2", "L.A Khan", "http://prn.fm/wp-content/uploads/2015/04/Liaquat-Ali-Khan.jpg", "Having dinner at McDonalds", "8-12-22", "9:32 pm");
                                        friendsActivitiesModelArrayList.add(friendsActivitiesModel);
                                        friendsActivitiesModel = new FriendsActivitiesModel("3", "M Iqbal", "http://media.worldbulletin.net/news/2014/04/21/muhammed-ikbal.jpg", "Having lunch at McDonalds", "8-12-22", "1:32 pm");
                                        friendsActivitiesModelArrayList.add(friendsActivitiesModel);
                                        friendsActivitiesModel = new FriendsActivitiesModel("4", "S.A Khan", "http://www.sirsyedtoday.org/aboutsirsyed/briefintro/images/sir-syed-album09_1-1.jpg", "Having breakfast at McDonalds", "8-12-22", "9:32 am");
                                        friendsActivitiesModelArrayList.add(friendsActivitiesModel);
                                        friendsActivitiesModel = new FriendsActivitiesModel("5", "K Nazimuddin", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e2/Khawaja_Nazimuddin_of_Pakistan.JPG/220px-Khawaja_Nazimuddin_of_Pakistan.JPG", "Having dinner at McDonalds", "8-12-22", "5:32 pm");
                                        friendsActivitiesModelArrayList.add(friendsActivitiesModel);

                                        adapter = new FriendActivityAdapter(getActivity(), friendsActivitiesModelArrayList);
                                        rvFriendsActivities.setAdapter(adapter);*/

                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
        );
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

        rvFriendsActivities.invalidate();

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabNewPost:
                slideUpOrDown();
                break;
            case R.id.fabNewPostCamera:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                    } else {
                        values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                        newPostImageUri = getActivity().getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, newPostImageUri);
                        getParentFragment().startActivityForResult(intent, 2);
                    }
                } else {
                    // create intent with ACTION_IMAGE_CAPTURE action
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    // start camera activity
                    startActivityForResult(intent, 2);
                }

                break;
            case R.id.fabNewPostGallery:

                break;
            case R.id.tvUserName:
            case R.id.tvActivityTitle:
            case R.id.tvDateAndTime:
            case R.id.ivProfilePic:
                SharedPreferences settings = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                String userID = settings.getString("userID", "defaultValue");

                Intent i = new Intent(getActivity(), FriendsActivitiesDetailsActivity.class);
                i.putExtra("UserID", userID);
                startActivity(i);
                break;
        }
    }

    private void slideUpOrDown() {
        if (fabNewPostCamera.getVisibility() == View.GONE) {
            fabNewPostCamera.setVisibility(View.VISIBLE);
            fabNewPostCamera.startAnimation(cameraIconSlideUp);
            fabNewPostGallery.setVisibility(View.VISIBLE);
            fabNewPostGallery.startAnimation(galleryIconSlideLeft);

        } else {
            fabNewPostCamera.startAnimation(cameraIconSlideDown);
            fabNewPostCamera.setVisibility(View.GONE);
            fabNewPostGallery.startAnimation(galleryIconSlideRight);
            fabNewPostGallery.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getActivity(), "resul", Toast.LENGTH_SHORT).show();
        if (requestCode == 2 & resultCode != Activity.RESULT_CANCELED) {
            try {
//                newPostBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), newPostImageUri);
                newPostBitmap = (Bitmap) data.getExtras().get("data");
                newPostBitmap = Bitmap.createScaledBitmap(newPostBitmap, newPostBitmap.getWidth() / 3, newPostBitmap.getHeight() / 3, false);

                UploadActivityPostDialog d = new UploadActivityPostDialog(getActivity(), newPostBitmap);
                d.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}