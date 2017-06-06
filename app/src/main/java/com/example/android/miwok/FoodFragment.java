package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class FoodFragment extends Fragment {

    /**
     * Handles playback of all the sound files
     */
    private MediaPlayer mMediaPlayer;

    /**
     * Handles audio focus when playing a sound file
     */
    private AudioManager mAudioManager;

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };

    public FoodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tour_list, container, false);

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        //Creating an ArrayList with 10 items
        final ArrayList<Tour> tours = new ArrayList<Tour>();

        tours.add(new Tour(getString(R.string.ranzini), getString(R.string.ranzini_address), getString(R.string.ranzini_description), R.drawable.ranzini, getString(R.string.ranzini_map), getString(R.string.ranzini_web_link)));
        tours.add(new Tour(getString(R.string.platti), getString(R.string.platti_address), getString(R.string.platti_description), R.drawable.platti, getString(R.string.platti_map), getString(R.string.platti_web_link)));
        tours.add(new Tour(getString(R.string.gelateria), getString(R.string.gelateria_address), getString(R.string.gelateria_description), R.drawable.gelateria2, getString(R.string.gelateria_map), getString(R.string.gelateria_web_link)));
        tours.add(new Tour(getString(R.string.soul), getString(R.string.soul_address), getString(R.string.soul_description), R.drawable.soul, getString(R.string.soul_map), getString(R.string.soul_web_link)));
        tours.add(new Tour(getString(R.string.cianci), getString(R.string.cianci_address), getString(R.string.cianci_description), R.drawable.cianci, getString(R.string.cianci_map), getString(R.string.cianci_web_link)));
        tours.add(new Tour(getString(R.string.porto), getString(R.string.porto_address), getString(R.string.porto_description), R.drawable.porto, getString(R.string.porto_map), getString(R.string.porto_web_link)));
        tours.add(new Tour(getString(R.string.eataly), getString(R.string.eataly_address), getString(R.string.eataly_description), R.drawable.eataly, getString(R.string.eataly_map), getString(R.string.eataly_web_link)));
        tours.add(new Tour(getString(R.string.primavera), getString(R.string.primavera_address), getString(R.string.primavera_description), R.drawable.primavera, getString(R.string.primavera_map), getString(R.string.primavera_web_link)));
        tours.add(new Tour(getString(R.string.ala), getString(R.string.ala_address), getString(R.string.ala_description), R.drawable.ala, getString(R.string.ala_map), getString(R.string.ala_web)));
        tours.add(new Tour(getString(R.string.barricata), getString(R.string.barricata_address), getString(R.string.barricata_description), R.drawable.barricata, getString(R.string.barricata_map), getString(R.string.barricata_web)));
        tours.add(new Tour(getString(R.string.pastis), getString(R.string.pastis_address), getString(R.string.pastis_description), R.drawable.pastis, getString(R.string.pastis_map), getString(R.string.pastis_web)));



        // Create an {@link TourAdapter}, whose data source is a list of {@link Tour}s. The
        // adapter knows how to create list items for each item in the list.
        TourAdapter adapter = new TourAdapter(getActivity(), tours, R.color.category_colors);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // tour_list.xml file.
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);

        // Make the {@link ListView} use the {@link TourAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Tour} in the list.
        listView.setAdapter(adapter);

        //setOnItemClickListener on the listVIew object
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long ID) {

                // Release the media player if it currently exists because we are about to
                // play a different sound file
                releaseMediaPlayer();

                //method get to find the position clicked in our list view
                Tour tourPosition = tours.get(position);

            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();

        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;


            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}