package com.hack.innovvapp.Fragments;

import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.print.PrinterId;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hack.innovvapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FeedbackFrament extends Fragment {

    @BindView(R.id.mic_image_view)
    ImageView micImageView;

    @BindView(R.id.feedback_edit_text)
    EditText feedBackEditText;

    @BindView(R.id.mic_record_text_view)
    TextView micRecordTextView;


    @BindView(R.id.record_timer_text_view)
    TextView timerTextView;

    private MediaRecorder mRecorder;
    private long mStartTime = 0;

    private int[] amplitudes = new int[100];
    private int i = 0;

    private boolean recoding_status = false;
    private Unbinder unbinder;




    private File mOutputFile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feedback,container,false);
        unbinder = ButterKnife.bind(this,rootView);

        if(!recoding_status){
            startRecord();

        }

        return rootView;
    }

    private void startRecord(){

        micImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                micImageView.setImageResource(R.drawable.ic_mic_black_big);
                startRecording();
                recoding_status = true;
                stopRecord();





            }
        });

    }

    private void stopRecord(){
        micImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                micImageView.setImageResource(R.drawable.ic_mic_black_24dp);
                stopRecording(true);

                recoding_status = false;
                startRecord();
            }
        });
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            mRecorder.setAudioEncodingBitRate(48000);
        } else {
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setAudioEncodingBitRate(64000);
        }
        mRecorder.setAudioSamplingRate(16000);
        mOutputFile = getOutputFile();
        mOutputFile.getParentFile().mkdirs();
        mRecorder.setOutputFile(mOutputFile.getAbsolutePath());

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartTime = SystemClock.elapsedRealtime();
            Log.d("Voice Recorder","started recording to "+mOutputFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("Voice Recorder", "prepare() failed "+e.getMessage());
        }
    }

    protected  void stopRecording(boolean saveFile) {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        mStartTime = 0;

    }

    private File getOutputFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.US);
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()
                + "/Voice Recorder/RECORDING_"
                + dateFormat.format(new Date())
                + ".m4a");
    }


}
