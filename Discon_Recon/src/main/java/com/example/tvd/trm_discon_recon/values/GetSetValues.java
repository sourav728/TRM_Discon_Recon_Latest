package com.example.tvd.trm_discon_recon.values;

public class GetSetValues {
    private String mrcode = "", mrname = "", subdivcode = "", acc_id = "", arrears = "", dis_date = "", prev_read = "", server_date = "", login_date = "",
            consumer_name = "", add1 = "", lati = "", longi = "", mtr_read = "", user_role = "", mrpassword = "", app_version = "";
    private String discon_id = "", discon_acc_id = "", discon_arrears = "", discon_date = "", discon_prevread = "", discon_consumer_name = "",
            discon_add1 = "", discon_lat = "", discon_lon = "", discon_mtr_read = "", discon_flag = "", remark = "";
    private String recon_id = "", recon_acc_id = "", recon_arrears = "", recon_date = "", recon_prevread = "", recon_consumer_name = "",
            recon_add1 = "", recon_lat = "", recon_lon = "", recon_mtr_read = "", recon_flag = "", recon_remark = "", re_date = "";
    private String mr_device_id = "", mr_subdiv_name = "";
    private String selected_discon_date = "";
    private String fdr_code = "";
    private String fdr_ir = "";
    private String fdr_fr = "";
    private String fdr_mf = "";
    private String fdr_name="";
    private String fdr_srtpv="";

    public String getFdr_name() {
        return fdr_name;
    }

    public void setFdr_name(String fdr_name) {
        this.fdr_name = fdr_name;
    }

    public String getFdr_srtpv() {
        return fdr_srtpv;
    }

    public void setFdr_srtpv(String fdr_srtpv) {
        this.fdr_srtpv = fdr_srtpv;
    }

    public String getFdr_boundary() {
        return fdr_boundary;
    }

    public void setFdr_boundary(String fdr_boundary) {
        this.fdr_boundary = fdr_boundary;
    }

    private String fdr_boundary="";
    private String feeder_code = "";
    private String recon_memo_acc_id = "";
    private String recon_memo_rrno = "";
    private String reoon_memo_tariff = "";
    private String recon_memo_so = "";
    private String recon_memo_prev_read = "";
    private String tc_code = "";
    private String tcir = "";
    private String tcfr = "";
    private String tcmf = "", tc_name = "";

    public String getTcname() {
        return tcname;
    }

    public void setTcname(String tcname) {
        this.tcname = tcname;
    }

    private String tcname = "";
    private String DTCNAME = "", DTCCODE = "", DTC_DATE = "", feeder_name = "";
    private String recon_memo_customer_name = "";
    private String recon_memo_add1 = "";
    private String recon_memo_mtr_read = "";
    private String recon_memo_arrears = "";
    private String recon_memo_dr_fee = "";
    private String recon_memo_reconnection_date = "";
    private String recon_memo_subdiv = "";
    private String final_reading="";

    public String getMRCODE() {
        return MRCODE;
    }

    public String getFinal_reading() {
        return final_reading;
    }

    public void setFinal_reading(String final_reading) {
        this.final_reading = final_reading;
    }

    public void setMRCODE(String MRCODE) {
        this.MRCODE = MRCODE;
    }

    private String MRCODE = "";

    public String getDTCNAME() {
        return DTCNAME;
    }

    public void setDTCNAME(String DTCNAME) {
        this.DTCNAME = DTCNAME;
    }

    public String getDTCCODE() {
        return DTCCODE;
    }

    public void setDTCCODE(String DTCCODE) {
        this.DTCCODE = DTCCODE;
    }

    public String getDTC_DATE() {
        return DTC_DATE;
    }

    public void setDTC_DATE(String DTC_DATE) {
        this.DTC_DATE = DTC_DATE;
    }

    public String getDTC_MRCODE() {
        return DTC_MRCODE;
    }

    public void setDTC_MRCODE(String DTC_MRCODE) {
        this.DTC_MRCODE = DTC_MRCODE;
    }

    private String DTC_MRCODE = "";

    public String getFeeder_name() {
        return feeder_name;
    }

    public void setFeeder_name(String feeder_name) {
        this.feeder_name = feeder_name;
    }


    public String getTc_code() {
        return tc_code;
    }

    public void setTc_code(String tc_code) {
        this.tc_code = tc_code;
    }

    public String getTcir() {
        return tcir;
    }

    public void setTcir(String tcir) {
        this.tcir = tcir;
    }

    public String getTcfr() {
        return tcfr;
    }

    public void setTcfr(String tcfr) {
        this.tcfr = tcfr;
    }

    public String getTcmf() {
        return tcmf;
    }

    public void setTcmf(String tcmf) {
        this.tcmf = tcmf;
    }

    public String getTc_name() {
        return tc_name;
    }

    public void setTc_name(String tc_name) {
        this.tc_name = tc_name;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getMr_device_id() {
        return mr_device_id;
    }

    public String getMrpassword() {
        return mrpassword;
    }

    public void setMrpassword(String mrpassword) {
        this.mrpassword = mrpassword;
    }

    public void setMr_device_id(String mr_device_id) {
        this.mr_device_id = mr_device_id;
    }

    public String getMr_subdiv_name() {
        return mr_subdiv_name;
    }

    public void setMr_subdiv_name(String mr_subdiv_name) {
        this.mr_subdiv_name = mr_subdiv_name;
    }

    public String getRecon_memo_readdate() {
        return recon_memo_readdate;
    }

    public void setRecon_memo_readdate(String recon_memo_readdate) {
        this.recon_memo_readdate = recon_memo_readdate;
    }

    private String recon_memo_readdate = "";


    public String getRecon_memo_mrcode() {
        return recon_memo_mrcode;
    }

    public void setRecon_memo_mrcode(String recon_memo_mrcode) {
        this.recon_memo_mrcode = recon_memo_mrcode;
    }

    private String recon_memo_mrcode = "";

    public String getMrcode() {
        return mrcode;
    }

    public String getDiscon_id() {
        return discon_id;
    }

    public String getFeeder_code() {
        return feeder_code;
    }

    public String getUser_role() {
        return user_role;
    }

    public String getRecon_memo_subdiv() {
        return recon_memo_subdiv;
    }

    public void setRecon_memo_subdiv(String recon_memo_subdiv) {
        this.recon_memo_subdiv = recon_memo_subdiv;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public void setFeeder_code(String feeder_code) {
        this.feeder_code = feeder_code;
    }

    public void setDiscon_id(String discon_id) {
        this.discon_id = discon_id;
    }

    public String getRecon_memo_acc_id() {
        return recon_memo_acc_id;
    }

    public String getRecon_memo_reconnection_date() {
        return recon_memo_reconnection_date;
    }

    public void setRecon_memo_reconnection_date(String recon_memo_reconnection_date) {
        this.recon_memo_reconnection_date = recon_memo_reconnection_date;
    }

    public void setRecon_memo_acc_id(String recon_memo_acc_id) {

        this.recon_memo_acc_id = recon_memo_acc_id;
    }


    public String getRecon_memo_rrno() {
        return recon_memo_rrno;
    }

    public void setRecon_memo_rrno(String recon_memo_rrno) {
        this.recon_memo_rrno = recon_memo_rrno;
    }

    public String getReoon_memo_tariff() {
        return reoon_memo_tariff;

    }

    public void setReoon_memo_tariff(String reoon_memo_tariff) {
        this.reoon_memo_tariff = reoon_memo_tariff;
    }

    public String getRecon_memo_so() {
        return recon_memo_so;
    }

    public void setRecon_memo_so(String recon_memo_so) {
        this.recon_memo_so = recon_memo_so;
    }

    public String getRecon_memo_prev_read() {
        return recon_memo_prev_read;
    }

    public void setRecon_memo_prev_read(String recon_memo_prev_read) {
        this.recon_memo_prev_read = recon_memo_prev_read;
    }

    public String getRecon_memo_customer_name() {
        return recon_memo_customer_name;
    }

    public void setRecon_memo_customer_name(String recon_memo_customer_name) {
        this.recon_memo_customer_name = recon_memo_customer_name;
    }

    public String getRecon_memo_add1() {
        return recon_memo_add1;
    }

    public void setRecon_memo_add1(String recon_memo_add1) {
        this.recon_memo_add1 = recon_memo_add1;
    }

    public String getRecon_memo_mtr_read() {
        return recon_memo_mtr_read;
    }

    public void setRecon_memo_mtr_read(String recon_memo_mtr_read) {
        this.recon_memo_mtr_read = recon_memo_mtr_read;
    }

    public String getRecon_memo_arrears() {
        return recon_memo_arrears;
    }

    public void setRecon_memo_arrears(String recon_memo_arrears) {
        this.recon_memo_arrears = recon_memo_arrears;
    }

    public String getRecon_memo_dr_fee() {
        return recon_memo_dr_fee;
    }

    public void setRecon_memo_dr_fee(String recon_memo_dr_fee) {
        this.recon_memo_dr_fee = recon_memo_dr_fee;
    }

    public String getSelected_discon_date() {
        return selected_discon_date;
    }

    public void setSelected_discon_date(String selected_discon_date) {
        this.selected_discon_date = selected_discon_date;
    }

    public String getRe_date() {
        return re_date;
    }

    public String getFdr_code() {
        return fdr_code;
    }

    public void setFdr_code(String fdr_code) {
        this.fdr_code = fdr_code;
    }

    public String getFdr_ir() {
        return fdr_ir;
    }

    public void setFdr_ir(String fdr_ir) {
        this.fdr_ir = fdr_ir;
    }

    public String getFdr_fr() {
        return fdr_fr;
    }

    public void setFdr_fr(String fdr_fr) {
        this.fdr_fr = fdr_fr;
    }

    public String getFdr_mf() {
        return fdr_mf;
    }

    public void setFdr_mf(String fdr_mf) {
        this.fdr_mf = fdr_mf;
    }

    public void setRe_date(String re_date) {
        this.re_date = re_date;
    }

    public String getRecon_id() {
        return recon_id;
    }

    public String getRecon_prevread() {
        return recon_prevread;
    }

    public void setRecon_prevread(String recon_prevread) {
        this.recon_prevread = recon_prevread;
    }

    public void setRecon_id(String recon_id) {
        this.recon_id = recon_id;
    }

    public String getRecon_acc_id() {
        return recon_acc_id;
    }

    public void setRecon_acc_id(String recon_acc_id) {
        this.recon_acc_id = recon_acc_id;
    }

    public String getRecon_arrears() {
        return recon_arrears;
    }

    public void setRecon_arrears(String recon_arrears) {
        this.recon_arrears = recon_arrears;
    }

    public String getRecon_date() {
        return recon_date;
    }

    public void setRecon_date(String recon_date) {
        this.recon_date = recon_date;
    }

    public String getRecon_consumer_name() {
        return recon_consumer_name;
    }

    public void setRecon_consumer_name(String recon_consumer_name) {
        this.recon_consumer_name = recon_consumer_name;
    }

    public String getRecon_add1() {
        return recon_add1;
    }

    public void setRecon_add1(String recon_add1) {
        this.recon_add1 = recon_add1;
    }

    public String getRecon_lat() {
        return recon_lat;
    }

    public void setRecon_lat(String recon_lat) {
        this.recon_lat = recon_lat;
    }

    public String getRecon_lon() {
        return recon_lon;
    }

    public void setRecon_lon(String recon_lon) {
        this.recon_lon = recon_lon;
    }

    public String getRecon_mtr_read() {
        return recon_mtr_read;
    }

    public void setRecon_mtr_read(String recon_mtr_read) {
        this.recon_mtr_read = recon_mtr_read;
    }

    public String getRecon_flag() {
        return recon_flag;
    }

    public void setRecon_flag(String recon_flag) {
        this.recon_flag = recon_flag;
    }

    public String getRecon_remark() {
        return recon_remark;
    }

    public void setRecon_remark(String recon_remark) {
        this.recon_remark = recon_remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDiscon_acc_id() {
        return discon_acc_id;
    }

    public void setDiscon_acc_id(String discon_acc_id) {
        this.discon_acc_id = discon_acc_id;
    }

    public String getDiscon_arrears() {
        return discon_arrears;
    }

    public void setDiscon_arrears(String discon_arrears) {
        this.discon_arrears = discon_arrears;
    }

    public String getDiscon_date() {
        return discon_date;
    }

    public void setDiscon_date(String discon_date) {
        this.discon_date = discon_date;
    }

    public String getDiscon_prevread() {
        return discon_prevread;
    }

    public void setDiscon_prevread(String discon_prevread) {
        this.discon_prevread = discon_prevread;
    }

    public String getDiscon_consumer_name() {
        return discon_consumer_name;
    }

    public void setDiscon_consumer_name(String discon_consumer_name) {
        this.discon_consumer_name = discon_consumer_name;
    }

    public String getDiscon_add1() {
        return discon_add1;
    }

    public void setDiscon_add1(String discon_add1) {
        this.discon_add1 = discon_add1;
    }

    public String getDiscon_lat() {
        return discon_lat;
    }

    public void setDiscon_lat(String discon_lat) {
        this.discon_lat = discon_lat;
    }

    public String getDiscon_lon() {
        return discon_lon;
    }

    public void setDiscon_lon(String discon_lon) {
        this.discon_lon = discon_lon;
    }

    public String getDiscon_mtr_read() {
        return discon_mtr_read;
    }

    public void setDiscon_mtr_read(String discon_mtr_read) {
        this.discon_mtr_read = discon_mtr_read;
    }

    public String getDiscon_flag() {
        return discon_flag;
    }

    public void setDiscon_flag(String discon_flag) {
        this.discon_flag = discon_flag;
    }

    public void setMrcode(String mrcode) {
        this.mrcode = mrcode;
    }

    public String getMrname() {
        return mrname;
    }

    public String getConsumer_name() {
        return consumer_name;
    }

    public void setConsumer_name(String consumer_name) {
        this.consumer_name = consumer_name;
    }

    public String getAdd1() {
        return add1;
    }

    public void setAdd1(String add1) {
        this.add1 = add1;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getMtr_read() {
        return mtr_read;
    }

    public void setMtr_read(String mtr_read) {
        this.mtr_read = mtr_read;
    }

    public void setMrname(String mrname) {
        this.mrname = mrname;
    }

    public String getLogin_date() {
        return login_date;
    }

    public void setLogin_date(String login_date) {
        this.login_date = login_date;
    }

    public String getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(String acc_id) {
        this.acc_id = acc_id;
    }

    public String getArrears() {
        return arrears;
    }

    public String getServer_date() {
        return server_date;
    }

    public void setServer_date(String server_date) {
        this.server_date = server_date;
    }

    public void setArrears(String arrears) {
        this.arrears = arrears;
    }

    public String getDis_date() {
        return dis_date;
    }

    public void setDis_date(String dis_date) {
        this.dis_date = dis_date;
    }

    public String getPrev_read() {
        return prev_read;
    }

    public void setPrev_read(String prev_read) {
        this.prev_read = prev_read;
    }

    public String getSubdivcode() {
        return subdivcode;
    }

    public void setSubdivcode(String subdivcode) {
        this.subdivcode = subdivcode;
    }
}
