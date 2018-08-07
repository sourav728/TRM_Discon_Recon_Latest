package com.example.tvd.trm_discon_recon.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.adapter.TCCode_Adapter;
import com.example.tvd.trm_discon_recon.ftp.FTPAPI;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.location.ClassGPS;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.Manifest.permission.CAMERA;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.example.tvd.trm_discon_recon.splash_screen.RequestPermissionCode;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FILE_UPLOADED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FILE_UPLOADED_ERROR;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_CODE_FOUND;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_CODE_NOTFOUND;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_CODE_NOTUPDATE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_CODE_UPDATE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_DETAILS_UPDATE;

public class TC_Details2 extends AppCompatActivity {
    private final int CAMERA_IMAGE = 2;
    private static final String IMAGE_DIRECTORY_NAME = "MyCamera";
    private static Uri fileUri; // file url to store image/video
    SendingData sendingData;
    TCCode_Adapter tcCode_adapter;
    GetSetValues getSetValues;
    RecyclerView recyclerview;
    ArrayList<GetSetValues> arraylist;
    static String MRCODE = "", DATE = "", cur_reading = "", TC_CODE = "";
    ProgressDialog progressdialog;
    AlertDialog tc_details_update_dialog;
    static FunctionCall fcall;
    Toolbar toolbar;
    TextView toolbar_text, Date, mrcode, tc_image;
    SearchView searchView;
    ClassGPS classGPS;
    String gps_lat = "", gps_long = "";
    ImageView imageView;
    File destination;
    static String pathname = "";
    static String pathextension = "";
    static String filename = "";
    static File mediaFile;
    String cons_ImgAdd = "", cons_imageextension = "";
    FTPAPI ftpapi;
    //*************************************************************************************************************************************
    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case TC_CODE_FOUND:
                    progressdialog.dismiss();
                    Toast.makeText(TC_Details2.this, "Success", Toast.LENGTH_SHORT).show();
                    break;
                case TC_CODE_NOTFOUND:
                    progressdialog.dismiss();
                    Toast.makeText(TC_Details2.this, "Data not Found!!", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case TC_CODE_UPDATE:
                    progressdialog.dismiss();
                    Toast.makeText(TC_Details2.this, "TC Details Updated..", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    break;
                case TC_CODE_NOTUPDATE:
                    progressdialog.dismiss();
                    Toast.makeText(TC_Details2.this, "Update Failure!!", Toast.LENGTH_SHORT).show();
                    tc_details_update_dialog.dismiss();
                    break;
                case FILE_UPLOADED:
                    SendingData.Update_Tcdetails update_tcdetails = sendingData.new Update_Tcdetails(handler, getSetValues);
                    update_tcdetails.execute(MRCODE, TC_CODE, fcall.Parse_Date9(DATE), cur_reading, gps_lat, gps_long, pathextension);
                    break;
                case FILE_UPLOADED_ERROR:
                    Toast.makeText(TC_Details2.this, "Update Failure!!", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
            return false;
        }
    });

    //***********************************************************************************************************************************************
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tc__details2);

        toolbar = findViewById(R.id.my_toolbar);
        toolbar_text = toolbar.findViewById(R.id.toolbar_title);
        toolbar_text.setText("TC Details");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchView = toolbar.findViewById(R.id.search_view);
        searchView.setQueryHint("Enter Tc Code..");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tcCode_adapter.getFilter().filter(newText);
                return false;
            }
        });
        classGPS = new ClassGPS(this);
        fcall = new FunctionCall();
        ftpapi = new FTPAPI(this);
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        MRCODE = sharedPreferences.getString("TCMRCODE", "");
        DATE = sharedPreferences.getString("TCMRDATE", "");

        Date = findViewById(R.id.txt_date);
        mrcode = findViewById(R.id.txt_subdiv);
        Date.setText(DATE);
        mrcode.setText(MRCODE);

        getSetValues = new GetSetValues();
        sendingData = new SendingData(this);
        recyclerview = findViewById(R.id.tccode_recyclerview);
        arraylist = new ArrayList<>();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        progressdialog = new ProgressDialog(TC_Details2.this, R.style.MyProgressDialogstyle);
        progressdialog.setCancelable(false);
        progressdialog.setTitle("Fetching Tc codes");
        progressdialog.setMessage("Please Wait.......");
        progressdialog.show();
        tcCode_adapter = new TCCode_Adapter(this, arraylist, TC_Details2.this);
        recyclerview.setAdapter(tcCode_adapter);

        SendingData.Search_Tccode search_tccode = sendingData.new Search_Tccode(handler, getSetValues, arraylist, tcCode_adapter);
        search_tccode.execute(MRCODE, DATE);
    }

    //***********************************************************************************************************************************************
    private void GPSlocation() {
        if (classGPS.canGetLocation()) {
            double latitude = classGPS.getLatitude();
            double longitude = classGPS.getLongitude();
            gps_lat = "" + latitude;
            gps_long = "" + longitude;
        }
    }

    //***********************************************************************************************************************************************
    public void show_tc_details_update_dialog2(int id, final int position, ArrayList<GetSetValues> arrayList) {
        final GetSetValues getSetValues = arrayList.get(position);
        switch (id) {
            case TC_DETAILS_UPDATE:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("TC UPDATE");
                dialog.setCancelable(false);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.tc_details_update_layout2, null);
                dialog.setView(view);
                final TextView tc_name = view.findViewById(R.id.txt_tcname);
                final TextView tc_code = view.findViewById(R.id.txt_tccode);
                final TextView mr_code = view.findViewById(R.id.txt_mrcode);
                final TextView date = view.findViewById(R.id.txt_date);
                final TextView tc_ir = view.findViewById(R.id.txt_ir);
                tc_image = view.findViewById(R.id.txt_image);
                imageView = view.findViewById(R.id.image);


                final EditText current_reading = view.findViewById(R.id.edit_current_reading);
                final Button cancel_button = view.findViewById(R.id.dialog_negative_btn);
                final Button update_button = view.findViewById(R.id.dialog_positive_btn);

                tc_details_update_dialog = dialog.create();
                tc_details_update_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        tc_name.setText(getSetValues.getTc_name());
                        TC_CODE = getSetValues.getTc_code();
                        tc_code.setText(TC_CODE);
                        mr_code.setText(MRCODE);
                        date.setText(DATE);
                        tc_ir.setText(getSetValues.getTcir());
                        current_reading.setText(getSetValues.getTcfr());
                        tc_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, getApplicationContext());
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                startActivityForResult(intent, CAMERA_IMAGE);
                            }
                        });
                        update_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GPSlocation();
                                cur_reading = current_reading.getText().toString();
                                if (Double.parseDouble(getSetValues.getTcir()) <= Double.parseDouble(cur_reading)) {
                                    if (!TextUtils.isEmpty(pathname)) {
                                       // ftpapi.new Upload_file(handler).execute(pathname);
                                        FTPAPI.Upload_file upload_file = ftpapi.new Upload_file(handler);
                                        upload_file.execute(pathname);
                                        progressdialog = new ProgressDialog(TC_Details2.this, R.style.MyProgressDialogstyle);
                                        progressdialog.setTitle("Updating Tc details..");
                                        progressdialog.setMessage("Please Wait..");
                                        progressdialog.show();
                                    } else {
                                        SendingData.Update_Tcdetails update_tcdetails = sendingData.new Update_Tcdetails(handler, getSetValues);
                                        update_tcdetails.execute(MRCODE, TC_CODE, fcall.Parse_Date9(DATE), cur_reading, gps_lat, gps_long, pathextension);
                                    }
                                } else
                                    Toast.makeText(TC_Details2.this, "Current reading should be greater than previous reading!!", Toast.LENGTH_SHORT).show();

                            }
                        });

                        cancel_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tc_details_update_dialog.dismiss();
                            }
                        });
                    }
                });
                tc_details_update_dialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_IMAGE) {
            if (resultCode == RESULT_OK) {
                cons_ImgAdd = pathname;
                cons_imageextension = pathextension;
                Bitmap bitmap = null;
                try {
                    bitmap = fcall.getImage(cons_ImgAdd, getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                destination = fcall.filestorepath("TC_PICS", cons_imageextension);
                if (bitmap != null) {
                    saveExternalPrivateStorage(destination, timestampItAndSave(bitmap));
                }
                String destinationfile = fcall.filepath("TC_PICS") + File.separator + cons_imageextension;
                Bitmap bitmap1 = null;
                try {
                    bitmap1 = fcall.getImage(destinationfile, getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(bitmap1);
                tc_image.setText(pathextension);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //Creating file uri to store image/video
    public Uri getOutputMediaFileUri(int type, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", Objects.requireNonNull(getOutputMediaFile(type)));
        else return Uri.fromFile(getOutputMediaFile(type));
    }


    //returning image / video
    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(android.os.Environment.getExternalStorageDirectory(), fcall.Appfoldername() + File.separator + IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + TC_CODE + "_" + timeStamp + ".jpg");
            pathname = mediaStorageDir.getPath() + File.separator + TC_CODE + "_" + timeStamp + ".jpg";
            pathextension = TC_CODE + "_" + timeStamp + ".jpg";
            filename = TC_CODE + "_" + timeStamp;
        } else {
            return null;
        }

        return mediaFile;
    }


    //**********************************Below code is for adding Watermark****************************************************
    public Bitmap timestampItAndSave(Bitmap bitmap) {
        Bitmap watermarkimage = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap dest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cs = new Canvas(dest);
        Paint tPaint = new Paint();
        tPaint.setTextSize(42);
        tPaint.setColor(Color.RED);
        tPaint.setStyle(Paint.Style.FILL);
        cs.drawBitmap(bitmap, 0f, 0f, null);
        float height = tPaint.measureText("yY");
        cs.drawText(filename, 20f, height + 15f, tPaint);
        try {
            dest.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(new File(pathname)));
            File directory = new File(pathname);
            FileInputStream watermarkimagestream = new FileInputStream(directory);
            watermarkimage = BitmapFactory.decodeStream(watermarkimagestream);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return watermarkimage;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void saveExternalPrivateStorage(File folderDir, Bitmap bitmap) {
        if (folderDir.exists()) {
            folderDir.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(folderDir);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
