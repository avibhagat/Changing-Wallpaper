package com.example.wallp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Screen extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startscreen);
		Thread timer = new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					sleep(3000);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					Intent startAct = new Intent("com.example.wallp.MAINACTIVITY");
					startActivity(startAct);
					finish();
				}
			}
		};
		timer.start();
	}
}
