/**
 * This is sample code provided by Wowza Media Systems, LLC.  All sample code is intended to be a reference for the
 * purpose of educating developers, and is not intended to be used in any production environment.
 * <p>
 * IN NO EVENT SHALL WOWZA MEDIA SYSTEMS, LLC BE LIABLE TO YOU OR ANY PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL,
 * OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF WOWZA MEDIA SYSTEMS, LLC HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <p>
 * WOWZA MEDIA SYSTEMS, LLC SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. ALL CODE PROVIDED HEREUNDER IS PROVIDED "AS IS".
 * WOWZA MEDIA SYSTEMS, LLC HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 * <p>
 * Copyright © 2015 Wowza Media Systems, LLC. All rights reserved.
 */

package com.pcs.hackathonandroid.activities;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.VideoView;

import com.pcs.hackathonandroid.R;
import com.pcs.hackathonandroid.ui.MultiStateButton;
import com.pcs.hackathonandroid.ui.StatusView;
import com.pcs.hackathonandroid.util.GoCoderSDKPrefs;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.configuration.WZMediaConfig;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;
import com.wowza.gocoder.sdk.api.logging.WZLog;
import com.wowza.gocoder.sdk.api.mp4.WZMP4Broadcaster;
import com.wowza.gocoder.sdk.api.mp4.WZMP4Util;
import com.wowza.gocoder.sdk.api.status.WZState;
import com.wowza.gocoder.sdk.api.status.WZStatus;

public class MP4BroadcastActivity extends GoCoderSDKActivityBase {
    final private static String TAG = MP4BroadcastActivity.class.getSimpleName();

    final private static int VIDEO_SELECTED_RESULT_CODE = 1;

    // UI controls
    private MultiStateButton mBtnFileSelect = null;
    private MultiStateButton mBtnLoop = null;

    protected MultiStateButton mBtnBroadcast = null;
    protected MultiStateButton mBtnSettings = null;

    private VideoView mVideoView = null;
    private StatusView mStatusView = null;

    private WZMP4Broadcaster mMP4Broadcaster = null;

    private MediaPlayer mMediaPlayer = null;
    private boolean mLooping = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp4_broadcast);

        mRequiredPermissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        mMediaPlayer = null;
        mLooping = true;

        mBtnBroadcast = findViewById(R.id.ic_broadcast);
        mBtnSettings = findViewById(R.id.ic_settings);

        mBtnFileSelect = findViewById(R.id.ic_videos);
        mBtnLoop = findViewById(R.id.ic_loop);

        mVideoView = findViewById(R.id.vwVideoPlayer);
        mStatusView = findViewById(R.id.statusView);

        if (sGoCoderSDK != null) {
            mMP4Broadcaster = new WZMP4Broadcaster(this);
//            mMP4Broadcaster.setOffset(10000); //set in ms
            mWZBroadcastConfig.setVideoBroadcaster(mMP4Broadcaster);
            mWZBroadcastConfig.setAudioBroadcaster(mMP4Broadcaster);

            mBtnLoop.setState(mLooping);

            mVideoView.setOnPreparedListener(mediaPlayer -> {
                mMediaPlayer = mediaPlayer;
                mediaPlayer.setLooping(mLooping);

                if (!getBroadcastConfig().isAudioEnabled())
                    mediaPlayer.setVolume(0.0f, 0.0f);
                else
                    mediaPlayer.setVolume(0.75f, 0.75f);

                mediaPlayer.seekTo(((int) mMP4Broadcaster.getOffset()));
            });

            mVideoView.setOnCompletionListener(mediaPlayer -> mMediaPlayer = null);
        } else if (mStatusView != null) {
            mStatusView.setErrorMessage(WowzaGoCoder.getLastError().getErrorDescription());
        }
    }

    /**
     * Android Activity class methods
     */

    @Override
    protected void onResume() {
        super.onResume();
        syncUIControlState();
    }

    /**
     * Click handler for the video selector button
     */
    public void onSelectMedia(View v) {
        if (!mPermissionsGranted) {
            mStatusView.setErrorMessage("The application has not been granted permission to read from external storage");
        } else {
            selectVideoFile();
        }
    }

    private void selectVideoFile() {
        mBtnBroadcast.setEnabled(false);
        mBtnSettings.setEnabled(false);
        mBtnFileSelect.setEnabled(false);

        Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
        mediaChooser.addCategory(Intent.CATEGORY_OPENABLE);
        mediaChooser.setType("video/*");
        startActivityForResult(mediaChooser, VIDEO_SELECTED_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnIntent) {
        mBtnFileSelect.setEnabled(true);
        mBtnSettings.setEnabled(false);

        switch (requestCode) {
            case VIDEO_SELECTED_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri fileUri = returnIntent.getData();
                    setVideoFile(fileUri);
                    syncUIControlState();
/*
                    String mimeType = getContentResolver().getType(fileUri);

                    if (mimeType != null && !mimeType.equals("video/mp4")) {
                        mStatusView.setErrorMessage("The video selected is not an MP4 file");
                    } else {
                        setVideoFile(fileUri);
                        syncUIControlState();
                    }
*/
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, returnIntent);
    }

    private void setVideoFile(Uri fileUri) {
        if (fileUri == null) return;

        WZMediaConfig mp4FileConfig = WZMP4Util.getFileConfig(this, fileUri);
        if (mp4FileConfig != null) {
            mWZBroadcastConfig.set(mp4FileConfig);
            mMP4Broadcaster.setFileUri(fileUri);
            mVideoView.setVideoURI(fileUri);

            findViewById(R.id.vwHelp).setVisibility(View.INVISIBLE);
            mVideoView.setVisibility(View.VISIBLE);
        } else {
            mStatusView.setErrorMessage("The format of the selected MP4 file could not be determined.");
            mVideoView.setVisibility(View.INVISIBLE);
            findViewById(R.id.vwHelp).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Click handler for the broadcast button
     */
    public void onToggleBroadcast(View v) {
        if (mMP4Broadcaster != null && mMP4Broadcaster.getFileUri() == null) {
            mStatusView.setErrorMessage("An MP4 file has not been selected");
            return;
        }


        if (mWZBroadcast.getStatus().isIdle()) {
            // Set the broadcast config so that it mirrors the MP4 file's format
            WZMediaConfig mp4Config = mMP4Broadcaster.getVideoSourceConfig();
            mWZBroadcastConfig.setVideoSourceConfig(mp4Config);
            mWZBroadcastConfig.setVideoFrameSize(mp4Config.getVideoFrameSize());
            mWZBroadcastConfig.setVideoFramerate(mp4Config.getVideoFramerate());
            mWZBroadcastConfig.setVideoKeyFrameInterval(mp4Config.getVideoKeyFrameInterval());
            mWZBroadcastConfig.setVideoRotation(mp4Config.getVideoRotation());

            WZStreamingError configValidationError = mWZBroadcastConfig.validateForBroadcast();
            if (configValidationError != null) {
                mStatusView.setErrorMessage(configValidationError.getErrorDescription());
            } else {
                mWZBroadcast.startBroadcast(mWZBroadcastConfig, this);
            }
        } else if (mWZBroadcast.getStatus().isRunning()) {
            if (mVideoView.isPlaying()) {
                mVideoView.pause();
            }
            mWZBroadcast.endBroadcast(this);
        }
    }

    /**
     * Click handler for the settings button
     */
    public void onSettings(View v) {
        // Display the prefs fragment
        GoCoderSDKPrefs.PrefsFragment prefsFragment = new GoCoderSDKPrefs.PrefsFragment();
        prefsFragment.setFixedSource(true);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, prefsFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Click handler for the loop button
     */
    public void onLoop(View v) {
        mLooping = mBtnLoop.toggleState();
        mMP4Broadcaster.setLooping(mLooping);
        if (mMediaPlayer != null)
            mMediaPlayer.setLooping(mLooping);
    }

    /**
     * WZStatusCallback interface methods
     */

    @Override
    public void onWZStatus(final WZStatus goCoderStatus) {
        new Handler(Looper.getMainLooper()).post(() -> {
            switch (goCoderStatus.getState()) {
                case WZState.IDLE:
                    // Clear the "keep screen on" flag
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                    }
                    break;
                case WZState.RUNNING:
                    // Keep the screen on while we are broadcasting
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    int seekVal = (int) mMP4Broadcaster.getOffset();
                    WZLog.debug("[broadcastVideoTrack] seekval : " + seekVal);
                    mVideoView.seekTo(seekVal);
                    mVideoView.start();
                    break;
            }
            mStatusView.setStatus(goCoderStatus);
            syncUIControlState();
        });
    }

    @Override
    public void onWZError(final WZStatus goCoderStatus) {
        new Handler(Looper.getMainLooper()).post(() -> {
            mStatusView.setStatus(goCoderStatus);
            syncUIControlState();
        });
    }

    /**
     * Update the state of the UI controls
     */
    private void syncUIControlState() {
        boolean disableControls = (mWZBroadcast == null ||
                !(mWZBroadcast.getStatus().isIdle() || mWZBroadcast.getStatus().isRunning()));

        Uri mp4FileUri = mMP4Broadcaster != null ? mMP4Broadcaster.getFileUri() : null;

        if (disableControls) {
            mBtnBroadcast.setEnabled(false);
            mBtnSettings.setEnabled(false);
            mBtnLoop.setEnabled(false);
            mBtnFileSelect.setEnabled(false);
        } else {
            boolean isStreaming = mWZBroadcast.getStatus().isRunning();
            mBtnBroadcast.setState(isStreaming);
            mBtnBroadcast.setEnabled(mp4FileUri != null);

            mBtnSettings.setEnabled(!isStreaming);
            mBtnFileSelect.setEnabled(!isStreaming);
        }
    }
}
