package com.jogoler.android.bakingapp.fragment;


import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.jogoler.android.bakingapp.DetailActivity;
import com.jogoler.android.bakingapp.R;
import com.jogoler.android.bakingapp.pojo.Recipe;
import com.jogoler.android.bakingapp.pojo.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.jogoler.android.bakingapp.MainActivity.SELECTED_RECIPE;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepDetailFragment extends Fragment {
    public static final String SELECTED_STEP = "selected_step";
    public static final String SELECTED_POSITION = "selected_position";
    public static final String SELECTED_INDEX = "selected_index";
    private static final String TITLE = "title";
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer simpleExoPlayer;
    private BandwidthMeter bandwidthMeter;
    private ArrayList<Step> stepArrayList = new ArrayList<>();
    private ArrayList<Recipe> recipeArrayList;
    private Handler mainHandler;
    private String recipeName;
    private int index;

    private ItemClickListner itemClickListner;
    private long position  = C.TIME_UNSET;
    private Uri videoUri;

    public interface ItemClickListner {
        void onListItemClickListener(List<Step> stepList, int index, String recipeName);
    }

    public RecipeStepDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        TextView stepTextView = (TextView)rootView.findViewById(R.id.recipe_step_detail_text_view);
        ImageView thumbnailImageView = (ImageView)rootView.findViewById(R.id.thumbnail_image_view);
        Button previousButtin  = (Button)rootView.findViewById(R.id.previous_button);
        Button nextButton = (Button)rootView.findViewById(R.id.next_button);
        simpleExoPlayerView = (SimpleExoPlayerView)rootView.findViewById(R.id.player_exo_view);

        bandwidthMeter = new DefaultBandwidthMeter();
        mainHandler = new Handler();
        itemClickListner = ((DetailActivity) getActivity());
        recipeArrayList = new ArrayList<>();

        if (savedInstanceState != null) {
            position = savedInstanceState.getLong(SELECTED_POSITION, C.TIME_UNSET);


            stepArrayList = savedInstanceState.getParcelableArrayList(SELECTED_STEP);
            index = savedInstanceState.getInt(SELECTED_INDEX);
            recipeName = savedInstanceState.getString(TITLE);
        } else {
            stepArrayList = getArguments().getParcelableArrayList(SELECTED_STEP);
            if(stepArrayList!=null){
                stepArrayList = getArguments().getParcelableArrayList(SELECTED_STEP);
                index = getArguments().getInt(SELECTED_INDEX);
                recipeName = getArguments().getString(TITLE);
            }else{
                recipeArrayList = getArguments().getParcelableArrayList(SELECTED_RECIPE);
                stepArrayList= (ArrayList<Step>) recipeArrayList.get(0).getSteps();
                index=0;
            }
        }

        stepTextView.setText(stepArrayList.get(index).getDescription());
        stepTextView.setVisibility(View.VISIBLE);

        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);


        if(rootView.findViewWithTag(getString(R.string.recipe_step_land))!=null){
            recipeName=((DetailActivity) getActivity()).recipeName;
            ((DetailActivity) getActivity()).getSupportActionBar().setTitle(recipeName);
        }

        String imageUrl = stepArrayList.get(index).getThumbnailURL();
        if (imageUrl!=""){
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(getContext()).load(builtUri).into(thumbnailImageView);
        }else{
            Log.d(RecipeDetailFragment.class.getSimpleName(),"imageUrl empty");
        }

        String videoURL = stepArrayList.get(index).getVideoURL();
        Log.d("aaa","videoUrl: "+videoURL);
        if(!videoURL.isEmpty()){
            videoUri = Uri.parse(stepArrayList.get(index).getVideoURL());

            initializePlayer(videoUri);

            if (rootView.findViewWithTag(getString(R.string.recipe_step_land))!=null) {
                Log.d("aaa","recipe 600dp");
                getActivity().findViewById(R.id.container_fragment2).setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
                simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            }
            else if (isLandscape(getContext())){
                stepTextView.setVisibility(View.GONE);
            }
        }else{
            simpleExoPlayer=null;
            simpleExoPlayerView.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.ic_visibility_off));
            simpleExoPlayerView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lastIndex = stepArrayList.size()-1;
                if(stepArrayList.get(index).getId()<stepArrayList.get(lastIndex).getId()){
                    if (simpleExoPlayer != null) {
                        simpleExoPlayer.stop();
                    }
                    itemClickListner.onListItemClickListener(stepArrayList, stepArrayList.get(index).getId() + 1, recipeName);
                }else{
                    Toast.makeText(getContext(), "No Next step !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        previousButtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(stepArrayList.get(index).getId()>0){
                    if (simpleExoPlayer != null) {
                        simpleExoPlayer.stop();
                    }
                    itemClickListner.onListItemClickListener(stepArrayList, stepArrayList.get(index).getId() - 1, recipeName);
                }else{
                    Toast.makeText(getContext(), "No previous step !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SELECTED_POSITION,position);
        outState.putParcelableArrayList(SELECTED_STEP, stepArrayList);
        outState.putInt(SELECTED_INDEX, index);
        outState.putString(TITLE, recipeName);
    }

    public boolean isLandscape(Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else {
            return false;
        }
    }

    private void initializePlayer(Uri mediaUri){
        if(simpleExoPlayer==null){
            TrackSelection.Factory videoTrackSelection = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector defaultTrackSelector = new DefaultTrackSelector(mainHandler,videoTrackSelection);
            LoadControl control = new DefaultLoadControl();

            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),defaultTrackSelector,control);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);

            String userAgents = Util.getUserAgent(getContext(),getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgents), new DefaultExtractorsFactory(), null, null);
            if (position != C.TIME_UNSET) simpleExoPlayer.seekTo(position);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        if(simpleExoPlayer!=null){
//            simpleExoPlayer.stop();
//            simpleExoPlayer.release();
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoUri != null)
            initializePlayer(videoUri);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(simpleExoPlayer!=null){
            position = simpleExoPlayer.getCurrentPosition();
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        }
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        if(simpleExoPlayer!=null){
//            simpleExoPlayer.stop();
//            simpleExoPlayer.release();
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (simpleExoPlayer != null) {
//            simpleExoPlayer.stop();
//            simpleExoPlayer.release();
//            simpleExoPlayer = null;
//        }
//    }
}
