package com.codiansoft.foodapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.codiansoft.foodapp.FriendsActivitiesDetailsActivity;
import com.codiansoft.foodapp.R;
import com.codiansoft.foodapp.RecyclerTouchListener;
import com.codiansoft.foodapp.adapter.FriendActivityAdapter;
import com.codiansoft.foodapp.dialog.UploadActivityPostDialog;
import com.codiansoft.foodapp.model.FriendsActivitiesModel;

import java.util.ArrayList;


/**
 * Created by Codiansoft on 9/25/2017.
 */

public class FriendsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    public static final String TAG = "FriendsFragment";
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    View view;
    FriendActivityAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rvFriendsActivities;
    ArrayList<FriendsActivitiesModel> friendsActivitiesModelArrayList;
    FriendsActivitiesModel friendsActivitiesModel;
    FloatingActionButton fabNewPost, fabNewPostCamera, fabNewPostGallery;
    Animation cameraIconSlideDown, cameraIconSlideUp, galleryIconSlideLeft, galleryIconSlideRight;

    ContentValues values;
    Uri imageUri;

    Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
/*        EditText v = new EditText(getActivity());
        v.setText("Hello Fragment!");*/
        view = inflater.inflate(R.layout.fragment_friends_activities, container, false);
        initUI();

        rvFriendsActivities.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvFriendsActivities, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                FriendsActivitiesModel friendsActivitiesModel = friendsActivitiesModelArrayList.get(position);
                Toast.makeText(getActivity(), friendsActivitiesModel.getID() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), FriendsActivitiesDetailsActivity.class);
                i.putExtra("friendUserID", friendsActivitiesModel.getID());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    private void initUI() {
        fabNewPost = (FloatingActionButton) view.findViewById(R.id.fabNewPost);
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

//                                        fetchRestaurantsFromServer();

                                        friendsActivitiesModelArrayList = new ArrayList<>();

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
                                        rvFriendsActivities.setAdapter(adapter);

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
                        imageUri = getActivity().getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, 2);
                    }
                } else {
                    values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    imageUri = getActivity().getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 2);
                }

                break;
            case R.id.fabNewPostGallery:

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
        if (requestCode == 2 & resultCode != Activity.RESULT_CANCELED) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, false);

                UploadActivityPostDialog d = new UploadActivityPostDialog(getActivity(), bitmap);
                d.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}