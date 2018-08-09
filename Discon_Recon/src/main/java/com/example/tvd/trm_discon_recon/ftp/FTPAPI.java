package com.example.tvd.trm_discon_recon.ftp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;

import com.example.tvd.trm_discon_recon.values.FunctionCall;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.APK_FILE_DOWNLOADED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.APK_FILE_NOT_FOUND;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DOWNLOAD_FILE_DELETED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DOWNLOAD_FILE_DELETE_CONNECTION_ERROR;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DOWNLOAD_FILE_NOT_DELETED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FILE_UPLOADED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FILE_UPLOADED_ERROR;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FTP_HOST;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FTP_PASS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FTP_PORT;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FTP_USER;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.REAL_TRM_URL;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.SERVICE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.SERVICE2;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TEST_FTP_HOST;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TEST_FTP_PASS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TEST_FTP_USER;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TEST_TRM_URL;

public class FTPAPI {

    public FTPAPI(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        String test_real = sharedPreferences.getString("TEST_REAL_SERVER", "");
        if (StringUtils.equalsIgnoreCase(test_real,"TEST"))
            server_link(0);
            //flag = "test";
        else
            server_link(1);
        //flag = "real";
    }
    private String FINAL_FTP_HOST,FINAL_FTP_USER,FINAL_FTP_PASS;
    private void server_link(int val)
    {
        if (val == 0)
        {
            //flag = "test";
            FINAL_FTP_HOST = TEST_FTP_HOST;
            FINAL_FTP_USER = TEST_FTP_USER;
            FINAL_FTP_PASS = TEST_FTP_PASS;
        }else {
            //flag = "real";
            FINAL_FTP_HOST = FTP_HOST;
            FINAL_FTP_USER = FTP_USER;
            FINAL_FTP_PASS = FTP_PASS;
        }
    }
    FunctionCall fcall = new FunctionCall();

    @SuppressLint("StaticFieldLeak")
    public class Upload_file extends AsyncTask<String, String, String> {
        FileInputStream fis = null;
        boolean result = false;
        Handler handler;
        String mobilepath = "";

        public Upload_file(Handler handler) {
            this.handler = handler;
        }

        @Override
        protected String doInBackground(String... params) {
            mobilepath = params[0];

            fcall.logStatus("Main_Upload_Images Images uploading started");
            FTPClient client = new FTPClient();
            fcall.logStatus("Main_Upload_Images 1");
            try {
                client.connect(FINAL_FTP_HOST, FTP_PORT);
                fcall.logStatus("Main_Upload_Images 2");
                int reply_from_server = client.getReplyCode();
                fcall.logStatus("Main_Upload_Images 3");
                if (FTPReply.isPositiveCompletion(reply_from_server)) {
                    fcall.logStatus("Main_Upload_Images 3_server");
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                fcall.logStatus("Main_Upload_Images 4");
                client.login(FINAL_FTP_USER, FINAL_FTP_PASS);
                fcall.logStatus("Main_Upload_Images 5");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                fcall.logStatus("Main_Upload_Images 6");
                client.setFileType(FTP.BINARY_FILE_TYPE);
                fcall.logStatus("Main_Upload_Images 7");
                client.enterLocalPassiveMode();
                fcall.logStatus("Main_Upload_Images 8");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                fcall.logStatus("Main_Upload_Images 9");
                client.changeWorkingDirectory("/Android/TC Pics/");
                fcall.logStatus("Main_Upload_Images 10");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                fcall.logStatus("Main_Upload_Images 11");
                File file = new File(mobilepath);
                fcall.logStatus("Main_Upload_Images 12");
                String testName = file.getName();
                fcall.logStatus("Main_Upload_Images 13");
                fis = new FileInputStream(file);
                fcall.logStatus("Main_Upload_Images 14");
                result = client.storeFile(testName, fis);
                fcall.logStatus("Main_Upload_Images 15");
            } catch (Exception e) {
                e.printStackTrace();
            }
            fcall.logStatus("Main_Upload_Images 16");
            try {
                fcall.logStatus("Main_Upload_Images 17");
                client.logout();
                fcall.logStatus("Main_Upload_Images 18");
            } catch (IOException e) {
                e.printStackTrace();
            }
            fcall.logStatus("Main_Upload_Images 19");
            if (result)
                handler.sendEmptyMessage(FILE_UPLOADED);
            else handler.sendEmptyMessage(FILE_UPLOADED_ERROR);
            fcall.logStatus("Main_Upload_Images 20");
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class Download_apk extends AsyncTask<String, String, String> {
        boolean dwnldCmplt = false, downloadapk = false;
        Handler handler;
        FileOutputStream fos = null;
        String mobilepath = fcall.filepath("ApkFolder") + File.separator;
        String update_version = "";

        public Download_apk(Handler handler, String update_version) {
            this.handler = handler;
            this.update_version = update_version;
        }

        @Override
        protected String doInBackground(String... params) {
            fcall.logStatus("Main_Apk 1");
            FTPClient ftp_1 = new FTPClient();
            fcall.logStatus("Main_Apk 2");
            try {
                fcall.logStatus("Main_Apk 3");
                ftp_1.connect(FINAL_FTP_HOST, FTP_PORT);
                fcall.logStatus("Main_Apk 4");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fcall.logStatus("Main_Apk 5");
                ftp_1.login(FINAL_FTP_USER, FINAL_FTP_PASS);
                downloadapk = ftp_1.login(FINAL_FTP_USER, FINAL_FTP_PASS);
                fcall.logStatus("Main_Apk 6");
            } catch (FTPConnectionClosedException e) {
                e.printStackTrace();
                try {
                    downloadapk = false;
                    ftp_1.disconnect();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (downloadapk) {
                fcall.logStatus("Apk download billing_file true");
                try {
                    fcall.logStatus("Main_Apk 7");
                    ftp_1.setFileType(FTP.BINARY_FILE_TYPE);
                    ftp_1.enterLocalPassiveMode();
                    fcall.logStatus("Main_Apk 8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fcall.logStatus("Main_Apk 9");
                    ftp_1.changeWorkingDirectory("/Android/Apk/");
                    fcall.logStatus("Main_Apk 10");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fcall.logStatus("Main_Apk 11");
                    FTPFile[] ftpFiles = ftp_1.listFiles("/Android/Apk/");
                    fcall.logStatus("Main_Apk 12");
                    int length = ftpFiles.length;
                    fcall.logStatus("Main_Apk 13");
                    fcall.logStatus("Main_Apk_length = " + length);
                    for (int i = 0; i < length; i++) {
                        String namefile = ftpFiles[i].getName();
                        fcall.logStatus("Main_Apk_namefile : " + namefile);
                        boolean isFile = ftpFiles[i].isFile();
                        if (isFile) {
                            fcall.logStatus("Main_Apk_File: " + "Discon_Recon_" + update_version + ".apk");
                            if (namefile.equals("Discon_Recon_" + update_version + ".apk")) {
                                fcall.logStatus("Main_Apk File found to download");
                                try {
                                    fos = new FileOutputStream(mobilepath + "Discon_Recon_" + update_version + ".apk");
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    dwnldCmplt = ftp_1.retrieveFile("/Android/Apk/" + "Discon_Recon_" + update_version + ".apk", fos);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            downloadapk = false;
            try {
                ftp_1.logout();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dwnldCmplt)
                handler.sendEmptyMessage(APK_FILE_DOWNLOADED);
            else handler.sendEmptyMessage(APK_FILE_NOT_FOUND);
        }
    }

}
