package com.example.wallp;


import android.app.Activity;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int RESULT_LOAD_IMG1 = 1;
    private static final int RESULT_LOAD_IMG2 = 2;
    private static final int RESULT_LOAD_IMG3 = 3;
    private static final int RESULT_LOAD_IMG4 = 4;

    ImageView imgView1;
    ImageView imgView2;
    ImageView imgView3;
    ImageView imgView4;
    int counter = 0;

    //ScreenReceiver sr = new ScreenReceiver();  

    TextView input;

    Bitmap b[] = new Bitmap[4];
    boolean screen;
    
    WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock =pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");

      
        Button b1 = (Button) findViewById(R.id.buttonSave);

        input = (TextView) findViewById(R.id.TimeView);
        counter = 0;
        b1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CharSequence time = input.getText();
                int number = Integer.parseInt(time.toString());

                if (counter == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Please Select Atleast one Image",
                            Toast.LENGTH_LONG).show();
                } else if (time.length() == 0) {

                    Toast.makeText(getApplicationContext(),
                            "Please Enter Time", Toast.LENGTH_LONG).show();
                } else if (number == 00) {

                    Toast.makeText(getApplicationContext(),
                            "Please Enter Time More than 0 mins", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Wallpaper changed successfully!",
                            Toast.LENGTH_LONG).show();
                    Thread backT = new Thread(new BackService(number));
                    backT.start();
                }

            };

        });
    }

    class BackService implements Runnable {
        int i = 0;
        WallpaperManager myWM = WallpaperManager
                .getInstance(getApplicationContext());
        int timeToRun = 0;
        int height = 0 ; 
        int width = 0;

        public BackService(int number) {
            timeToRun = number;
            DisplayMetrics metrics = new DisplayMetrics(); 
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            // get the height and width of screen 
                height = metrics.heightPixels; 
                width = metrics.widthPixels;
       
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            android.os.Process
                    .setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            while (true) {
                
                if (b[i] != null)
                    try {
                        myWM.setBitmap(b[i]);
                        myWM.suggestDesiredDimensions(width, height);
                        Thread.sleep(timeToRun * 60000);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                i++;
                if (i >= b.length) {
                    i = 0;
                }
            }
        }
    }
    
    class ScreenReceiver extends BroadcastReceiver {
        
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                // DO WHATEVER YOU NEED TO DO HERE
                screen = false;
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                // AND DO WHATEVER YOU NEED TO DO HERE
                screen = true;
            }
            
            
        }
     
    }

    public void OnClick(View view) {

        // Create intent to Open Image applications like Gallery, Google Photos

        switch (view.getId()) {
        case R.id.button1:
            Intent galleryIntent1 = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            galleryIntent1.setType("image/*");
            startActivityForResult(galleryIntent1, RESULT_LOAD_IMG1);
            break;
        case R.id.button2:
            Intent galleryIntent2 = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            galleryIntent2.setType("image/*");
            startActivityForResult(galleryIntent2, RESULT_LOAD_IMG2);
            break;
        case R.id.button3:
            Intent galleryIntent3 = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            galleryIntent3.setType("image/*");
            startActivityForResult(galleryIntent3, RESULT_LOAD_IMG3);
            break;
        case R.id.button4:
            Intent galleryIntent4 = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            galleryIntent4.setType("image/*");
            startActivityForResult(galleryIntent4, RESULT_LOAD_IMG4);
            break;
        }
    }

    protected Bitmap createImageThumbnail(String imagePath, int width,
            int height) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imagePath, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
                / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
                / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }
        bmpFactoryOptions.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(imagePath, bmpFactoryOptions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // When an Image is picked
        try {
            if (requestCode == RESULT_LOAD_IMG1 && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                imgView1 = (ImageView) findViewById(R.id.imageView1);
                // Set the Image in ImageView after decoding the String
                imgView1.setImageBitmap(createImageThumbnail(
                        imgDecodableString, 786, 1024));
                b[0] = ((BitmapDrawable) imgView1.getDrawable()).getBitmap();
                counter++;

            } else if (requestCode == RESULT_LOAD_IMG2
                    && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                imgView2 = (ImageView) findViewById(R.id.imageView2);
                // Set the Image in ImageView after decoding the String
                imgView2.setImageBitmap(createImageThumbnail(
                        imgDecodableString, 786, 1024));
                b[1] = ((BitmapDrawable) imgView2.getDrawable()).getBitmap();
                counter++;

            } else if (requestCode == RESULT_LOAD_IMG3
                    && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                imgView3 = (ImageView) findViewById(R.id.imageView3);
                // Set the Image in ImageView after decoding the String
                imgView3.setImageBitmap(createImageThumbnail(
                        imgDecodableString, 786, 1024));
                b[2] = ((BitmapDrawable) imgView3.getDrawable()).getBitmap();
                counter++;

            } else if (requestCode == RESULT_LOAD_IMG4
                    && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                imgView4 = (ImageView) findViewById(R.id.imageView4);
                // Set the Image in ImageView after decoding the String
                imgView4.setImageBitmap(createImageThumbnail(
                        imgDecodableString, 786, 1024));
                b[3] = ((BitmapDrawable) imgView4.getDrawable()).getBitmap();
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}