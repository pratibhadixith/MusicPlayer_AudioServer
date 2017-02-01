package com.example.prati.audioserver;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.prati.KeyCommon.MyMP;

import java.io.IOException;

/**
 * Created by prati on 11/28/2016.
 */

public class MpService extends Service{

    final static int[] SongList={R.raw.clip1,R.raw.clip2,R.raw.clip3,R.raw.clip4,R.raw.clip5};
    static private MediaPlayer mplayer=new MediaPlayer();
    private boolean isStarted = false;
    private int Currentclip;


    // Implement the Stub for this Object
    private final MyMP.Stub mBinder = new MyMP.Stub() {

        // Implement the remote method playclip declared in AIDL file
        //Gets the song by the clipnumber and saves it
        @Override
        public void playclip(int clipNumber) {


            try {

              //  mplayer.setDataSource(String.valueOf(SongList[clipNumber-1]));
                AssetFileDescriptor filedes = getApplicationContext().getResources().openRawResourceFd(SongList[clipNumber - 1]);
                mplayer.setDataSource(filedes.getFileDescriptor(), filedes.getStartOffset(), filedes.getLength());
                mplayer.prepare();
                mplayer.start();
                filedes.close();
            } catch (IOException e) {

                Log.i("mplayer says","Exception in playclip");
            }

            isStarted = true;
            Currentclip=clipNumber;

        }

        // Implement the remote method resume_play_clip declared in AIDL file
        //Check if current song playing is the same as requested and if it was already started
        //If yes, resume playing same song
        //else, stop the current song and play the requested track (new)
        @Override
        public void resume_play_clip(int clipNumber) {

            if (isStarted) {
                if (Currentclip == clipNumber) {
                    mplayer.start();

                } else {
                    mplayer.stop();
                    mplayer.reset();
                    playclip(clipNumber);
                }
            }//play newly requested audio
            else
            {
                playclip(clipNumber);
            }
            Currentclip=clipNumber;

        }

        // Implement the remote method stopclip declared in AIDL file
        @Override
        public void stopclip(int clipNumber) {

            mplayer.stop();
            mplayer.reset();
            isStarted = false;
        }






        // Implement the remote method pauseclip declared in AIDL file
        //check if the clip is playing ,if yes, do pause
        @Override
        public void pauseclip(int clipNumber) {

            if (mplayer.isPlaying()) {
                mplayer.pause();

            }
        }
    };

    // Return the Stub defined above
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //App is killed here
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i("mplayer says","onTaskRemoved executing");
        try {
            stopService(new Intent(this, this.getClass()));
            stopSelf();
        } catch (Exception e) {
            Log.i("mplayer says","Exception in onTaskRemoved ");
        }
        super.onTaskRemoved(rootIntent);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
