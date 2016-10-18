package com.example.rajukoushik.fitnessforks;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

public class IntroActivity extends AppCompatActivity {

    private VideoView videoView;
    String extStorageDirectory;
    protected static final int PLAY = 0x101;
    protected static final int STOP = 0x102;
    protected static final int PAUSE = 0x103;
    int State;
    private String current;

    private String path ;
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //video start

        path="android.resource://" + getPackageName() + "/" + R.raw.ff;
        mVideoView = (VideoView) findViewById(R.id.video);



        if (path == null || path.length() == 0) {
            Toast.makeText(IntroActivity.this, "File URL/path is empty",
                    Toast.LENGTH_LONG).show();

        } else {
            // If the path has not changed, just start the media player
            if (path.equals(current) && mVideoView != null) {
                mVideoView.start();
                mVideoView.requestFocus();
                return;
            }
            current = path;
            mVideoView.setVideoPath(path);
            mVideoView.start();
            mVideoView.requestFocus();
        }
        /*
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer arg0) {

                mVideoView.start();
            }
        });
        */

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    public void btnbfu(View view) {
        Intent intent = new Intent(this, MetricsActivity.class);
        startActivity(intent);

    }
    //video end


    }

