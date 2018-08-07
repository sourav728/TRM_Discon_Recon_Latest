package com.example.tvd.trm_discon_recon.values;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.other.CheckInternetConnection;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionCall {
    public void logStatus(String message) {
        Log.d("debug", message);
    }

    public boolean checkInternetConnection(Context context) {
        CheckInternetConnection cd = new CheckInternetConnection(context.getApplicationContext());
        return cd.isConnectingToInternet();
    }

    /***********CHECKING INTERNET IS ON OR NOT****************/
    public final boolean isInternetOn(Activity activity) {
        ConnectivityManager connect = (ConnectivityManager) activity.getSystemService(activity.getBaseContext().CONNECTIVITY_SERVICE);
        if (connect.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connect.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else if (connect.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                connect.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    @SuppressLint("DefaultLocale")
    public String convertdateview(String date, String format, String separation) {
        String s1, s2, s3, s4, s5 = "";
        if (date.length() == 10) {
            s1 = date.substring(0, 2);
            s2 = date.substring(3, 5);
            s3 = date.substring(6, 10);
            if (format.equals("DD") || format.equals("dd")) {
                s5 = s1 + separation + s2 + separation + s3;
            } else {
                s5 = s3 + separation + s2 + separation + s1;
            }
        } else if (date.length() == 9) {
            s4 = date.substring(1, 2);
            try {
                int i1 = Integer.parseInt(s4);
                s1 = date.substring(0, 2);
                s2 = date.substring(3, 4);
                s3 = date.substring(5, 9);
                if (format.equals("DD") || format.equals("dd")) {
                    s5 = s1 + separation + "0" + s2 + separation + s3;
                } else {
                    s5 = s3 + separation + "0" + s2 + separation + s1;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                s1 = date.substring(0, 1);
                s2 = date.substring(2, 4);
                s3 = date.substring(5, 9);
                if (format.equals("DD") || format.equals("dd")) {
                    s5 = "0" + s1 + separation + s2 + separation + s3;
                } else {
                    s5 = s3 + separation + s2 + separation + "0" + s1;
                }
            }
        } else if (date.length() == 8) {
            s1 = date.substring(0, 1);
            s2 = date.substring(2, 3);
            s3 = date.substring(4, 8);
            if (format.equals("DD") || format.equals("dd")) {
                s5 = "0" + s1 + separation + "0" + s2 + separation + s3;
            } else {
                s5 = s3 + separation + "0" + s2 + separation + "0" + s1;
            }
        }
        return s5;
    }

    public void setEdittext_error(EditText editText, String error_msg) {
        editText.setError(error_msg);
        editText.requestFocus();
        editText.setSelection(editText.getText().length());
    }

    public String Parse_Date2(String time) {
        String input = "yyyy/MM/dd";
        String output = "dd/MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(input, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(output, Locale.getDefault());

        Date date;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String Parse_Date3(String time) {
        String input = "yyyy-MM-d";
        String output = "yyyy/MM/dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(input);
        SimpleDateFormat outputFormat = new SimpleDateFormat(output);

        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String Parse_Date4(String time) {
        String input = "dd-MM-yyyy hh:mm:ss";
        String output = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(input, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(output, Locale.getDefault());

        Date date;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String Parse_Date5(String time) {
        String input = "yyyy/MM/dd";
        String output = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(input, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(output, Locale.getDefault());

        Date date;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String Parse_Date6(String time) {
        String input = "yyyy/MM/dd";
        String output = "yyyyMM";
        SimpleDateFormat inputFormat = new SimpleDateFormat(input, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(output, Locale.getDefault());

        Date date;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String Parse_Date7(String time) {
        String input = "yyyy/MM/dd";
        String output = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(input, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(output, Locale.getDefault());

        Date date;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String Parse_Date8(String time) {
        String input = "yyyy-MM-d";
        String output = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(input);
        SimpleDateFormat outputFormat = new SimpleDateFormat(output);

        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String Parse_Date9(String time) {
        String input = "yyyy-MM-dd";
        String output = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(input, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(output, Locale.getDefault());

        Date date;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public void showtoast(Context context, String Message) {
        Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
    }

    public Date selectiondate(String date) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public File filestorepath(String value, String file) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory(), Appfoldername()
                + File.separator + value);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, File.separator + file);
    }

    public String Appfoldername() {
        return "Discon_Recon";
    }

    public String filepath(String value) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory(), Appfoldername() + File.separator + value);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.toString();
    }


    public String space(String s, int len) {
        int temp;
        StringBuilder spaces = new StringBuilder();
        temp = len - s.length();
        for (int i = 0; i < temp; i++) {
            spaces.append(" ");
        }
        return (s + spaces);
    }

    public String aligncenter(String msg, int len) {
        int count = msg.length();
        int value = len - count;
        int append = (value / 2);
        return space(" ", append) + msg + space(" ", append);
    }

    public String alignright(String msg, int len) {
        for (int i = 0; i < len - msg.length(); i++) {
            msg = " " + msg;
        }
        msg = String.format("%" + len + "s", msg);
        return msg;
    }

    public boolean compare(String v1, String v2) {
        String s1 = normalisedVersion(v1);
        String s2 = normalisedVersion(v2);
        int cmp = s1.compareTo(s2);
        String cmpStr = cmp < 0 ? "<" : cmp > 0 ? ">" : "==";
        return cmpStr.equals("<");
    }

    public String normalisedVersion(String version) {
        return normalisedVersion(version, ".", 4);
    }

    private String normalisedVersion(String version, String sep, int maxWidth) {
        String[] split = Pattern.compile(sep, Pattern.LITERAL).split(version);
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            sb.append(String.format("%" + maxWidth + 's', s));
        }
        return sb.toString();
    }

    public String currentRecpttime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US);
        return sdf.format(new Date());
    }

    public String system_date() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return sdf.format(new Date());
    }

    public void showprogressdialog(String Message, ProgressDialog dialog) {
        dialog.setTitle("Downloading");
        dialog.setMessage(Message);
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        dialog.show();
    }

    public void updateApp(Context context, File Apkfile) {
        Uri path;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            path = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", Apkfile);
        } else path = Uri.fromFile(Apkfile);
        Intent objIntent = new Intent(Intent.ACTION_VIEW);
        objIntent.setDataAndType(path, "application/vnd.android.package-archive");
        objIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        objIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(objIntent);
    }

    public void splitString(String msg, int lineSize, ArrayList<String> arrayList) {
        arrayList.clear();
        Pattern p = Pattern.compile("\\b.{0," + (lineSize - 1) + "}\\b\\W?");
        Matcher m = p.matcher(msg);
        while (m.find()) {
            arrayList.add(m.group().trim());
        }
    }

    public Bitmap getImage(String path, Context con) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;
        int[] newWH = new int[2];
        newWH[0] = srcWidth / 2;
        newWH[1] = (newWH[0] * srcHeight) / srcWidth;

        int inSampleSize = 1;
        while (srcWidth / 2 >= newWH[0]) {
            srcWidth /= 2;
            srcHeight /= 2;
            inSampleSize *= 2;
        }

        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inSampleSize = inSampleSize;
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap sampledSrcBitmap = BitmapFactory.decodeFile(path, options);
        ExifInterface exif = new ExifInterface(path);
        String s = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        System.out.println("Orientation>>>>>>>>>>>>>>>>>>>>" + s);
        Matrix matrix = new Matrix();
        float rotation = rotationForImage(con, Uri.fromFile(new File(path)));
        if (rotation != 0f) {
            matrix.preRotate(rotation);
        }
        return Bitmap.createBitmap(
                sampledSrcBitmap, 0, 0, sampledSrcBitmap.getWidth(), sampledSrcBitmap.getHeight(), matrix, true);
    }

    private float rotationForImage(Context context, Uri uri) {
        if (uri.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            @SuppressLint("Recycle") Cursor c = context.getContentResolver().query(
                    uri, projection, null, null, null);
            if (c != null && c.moveToFirst()) {
                return c.getInt(0);
            }
        } else if (uri.getScheme().equals("file")) {
            try {
                ExifInterface exif = new ExifInterface(uri.getPath());
                return (int) exifOrientationToDegrees(
                        exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return 0f;
    }

    private static float exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public boolean isDeviceSupportCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
}
