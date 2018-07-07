package com.hack.innovvapp.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hack.innovvapp.Activities.MainActivity;
import com.hack.innovvapp.Models.AudioFeedback;
import com.hack.innovvapp.Models.TextFeedback;
import com.hack.innovvapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

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
    Chronometer timerTextView;

    @BindView(R.id.tes_image_view)
    ImageView yesImageView;

    @BindView(R.id.no_image_view)
    ImageView noimageView;

    @BindView(R.id.mic_fragment)
    View micView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.button_submit)
    Button submitButton;

    private MediaRecorder mRecorder;
    private long mStartTime = 0;

    private int[] amplitudes = new int[100];
    private int i = 0;

    private boolean recoding_status = false;
    private Unbinder unbinder;

    private int type;


    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;

    private StorageReference storageReference;

    public static String PATH_AUDIO = "audio";

    Random random = new Random();

    public static  String PATH_TEXT = "text";

    private long randomNumber=random.nextInt(99999999 - 10000000);





    private File mOutputFile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feedback,container,false);
        unbinder = ButterKnife.bind(this,rootView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        if (getArguments()!=null){
           this.type = getArguments().getInt(MainActivity.FEEDBACK_TYPE);
        }

        progressBar.setVisibility(View.GONE);

        if(!recoding_status){
            startRecord();
            micRecordTextView.setText(R.string.start_recodring);

        }

        yesImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upladAudioObject();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feedBackEditText.getText().toString()!=null){
                    TextFeedback textFeedback = new TextFeedback();

                    textFeedback.setFeedback(feedBackEditText.getText().toString());
                    textFeedback.setType(type);

                    HashMap hashMap = new HashMap();

                    hashMap.put("text",textFeedback);

                    databaseReference.child(PATH_TEXT).child(String.valueOf(randomNumber)).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(getContext(),"success",Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });



        return rootView;
    }


    private void startRecord(){

        micImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerTextView.start();
                micImageView.setImageResource(R.drawable.ic_mic_black_big);
                startRecording();
                recoding_status = true;
                stopRecord();
                micRecordTextView.setText(R.string.stop_recording);





            }
        });

    }

    private void stopRecord(){
        micImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerTextView.stop();
                micImageView.setImageResource(R.drawable.ic_mic_black_24dp);
                stopRecording(true);

                recoding_status = false;
                if (yesImageView!=null){
                    yesImageView.setVisibility(View.VISIBLE);

                }
                if (noimageView!=null){
                    noimageView.setVisibility(View.VISIBLE);
                }
                micRecordTextView.setText(R.string.start_recodring);

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

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            micView.setVisibility(show ? View.GONE : View.VISIBLE);
            micView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    micView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            micView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void upladAudioObject(){

        timerTextView.setBase(SystemClock.elapsedRealtime());
        timerTextView.stop();

        showProgress(true);

        Random random = new Random();

        final long randomNumber=random.nextInt(99999999 - 10000000);
        if (mOutputFile!=null){

            storageReference.child(PATH_AUDIO).child(String.valueOf(randomNumber))
                    .putFile(FileProvider.getUriForFile(getContext(),getContext().getApplicationContext().getPackageName()+".com.hack.innovvapp.provider",mOutputFile))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            AudioFeedback audioFeedback = new AudioFeedback();

                            audioFeedback.setAudioPath(String.valueOf(randomNumber));
                            audioFeedback.setType(type);


                            HashMap hashMap = new HashMap();
                            hashMap.put("audio",audioFeedback);

                            databaseReference.child(PATH_AUDIO).child(String.valueOf(randomNumber)).setValue(hashMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                    showProgress(false);
                                    Toast.makeText(getContext(),"audio upload success",Toast.LENGTH_SHORT).show();


                                }
                            });



                            micImageView.setImageResource(R.drawable.ic_mic_black_big);
                            if (yesImageView!=null){
                                yesImageView.setVisibility(View.GONE);

                            }
                            if (noimageView!=null){
                                noimageView.setVisibility(View.GONE);
                            }



                        }
                    });




        }else{
            Toast.makeText(getContext(),"filenull",Toast.LENGTH_SHORT).show();
        }

    }



}
