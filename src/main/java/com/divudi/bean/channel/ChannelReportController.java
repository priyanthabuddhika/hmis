/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.bean.channel;

import com.divudi.bean.common.CommonController;
import com.divudi.bean.common.SessionController;
import com.divudi.bean.common.UtilityController;
import com.divudi.bean.common.WebUserController;
import com.divudi.bean.hr.StaffController;
import com.divudi.data.BillType;
import com.divudi.data.FeeType;
import com.divudi.data.HistoryType;
import com.divudi.data.PaymentMethod;
import com.divudi.data.channel.DateEnum;
import com.divudi.data.channel.PaymentEnum;
import com.divudi.data.dataStructure.BillsTotals;
import com.divudi.data.dataStructure.ChannelDoctor;
import com.divudi.data.dataStructure.WebUserBillsTotal;
import com.divudi.data.hr.ReportKeyWord;
import com.divudi.data.table.String1Value1;
import com.divudi.data.table.String1Value3;
import com.divudi.ejb.ChannelBean;
import com.divudi.ejb.CommonFunctions;
import com.divudi.entity.AgentHistory;
import com.divudi.entity.Bill;
import com.divudi.entity.BillFee;
import com.divudi.entity.BillItem;
import com.divudi.entity.BillSession;
import com.divudi.entity.BilledBill;
import com.divudi.entity.CancelledBill;
import com.divudi.entity.Department;
import com.divudi.entity.Institution;
import com.divudi.entity.RefundBill;
import com.divudi.entity.ServiceSession;
import com.divudi.entity.Staff;
import com.divudi.entity.WebUser;
import com.divudi.facade.AgentHistoryFacade;
import com.divudi.facade.BillFacade;
import com.divudi.facade.BillFeeFacade;
import com.divudi.facade.BillItemFacade;
import com.divudi.facade.BillSessionFacade;
import com.divudi.facade.DepartmentFacade;
import com.divudi.facade.ServiceSessionFacade;
import com.divudi.facade.StaffFacade;
import com.divudi.facade.WebUserFacade;
import com.divudi.facade.util.JsfUtil;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TemporalType;

@Named
@SessionScoped
public class ChannelReportController implements Serializable {

    private ServiceSession serviceSession;
    private double billedTotalFee;
    private double repayTotalFee;
    private double taxTotal;
    private double total;
    ///////
    private List<BillSession> billSessions;
    private List<BillSession> billSessionsBilled;
    private List<BillSession> billSessionsReturn;
    private List<BillSession> billSessionsCancelled;
    List<BillSession> selectedBillSessions;
    List<ServiceSession> serviceSessions;
    List<ChannelReportColumnModel> channelReportColumnModels;
    double netTotal;
    double netTotalDoc;
    double cancelTotal;
    double refundTotal;
    double totalBilled;
    double totalCancel;
    double totalRefundDoc;
    double totalBilledDoc;
    double totalCancelDoc;
    double totalRefund;
    double grantTotalBilled;
    double grantTotalCancel;
    double grantTotalRefund;
    double grantNetTotal;
    double doctorFeeTotal;
    double doctorFeeBilledTotal;
    double doctorFeeCancellededTotal;
    double doctorFeeRefundededTotal;
    double hospitalFeeBilledTotal;
    double hospitalFeeCancellededTotal;
    double hospitalFeeRefundededTotal;
    List<String1Value3> valueList;
    ReportKeyWord reportKeyWord;
    Date fromDate;
    Date toDate;
    Date date;
    Institution institution;
    WebUser webUser;
    Staff staff;
    ChannelBillTotals billTotals;
    Department department;
    boolean paid = false;
    boolean summery = false;
    boolean sessoinDate = false;
    boolean withDates = false;
    boolean agncyOnCall = false;
    PaymentMethod paymentMethod;
    ChannelTotal channelTotal;
    /////
    private List<ChannelDoctor> channelDoctors;
    List<AgentHistory> agentHistorys;
    List<AgentHistoryWithDate> agentHistoryWithDate;
    List<BookingCountSummryRow> bookingCountSummryRows;
    List<BookingCountSummryRow> bookingCountSummryRowsScan;
    List<DoctorPaymentSummeryRow> doctorPaymentSummeryRows;
    List<Bill> channelBills;
    List<Bill> channelBillsCancelled;
    List<Bill> channelBillsRefunded;
    /////
    @EJB
    private BillSessionFacade billSessionFacade;
    @EJB
    private BillFeeFacade billFeeFacade;
    @EJB
    private BillItemFacade billItemFacade;
    @EJB
    private BillFacade billFacade;
    @EJB
    AgentHistoryFacade agentHistoryFacade;
    ///////////
    @EJB
    private ChannelBean channelBean;
    @Inject
    SessionController sessionController;
    @Inject
    CommonController commonController;
    @Inject
    StaffController staffController;
    @Inject
    BookingController bookingController;
    @Inject
    WebUserController webUserController;

    @EJB
    DepartmentFacade departmentFacade;
    @EJB
    CommonFunctions commonFunctions;
    @EJB
    StaffFacade staffFacade;
    @EJB
    ServiceSessionFacade serviceSessionFacade;

    public void clearAll() {
        billedBills = new ArrayList<>();
        cancelBills = new ArrayList<>();
        refundBills = new ArrayList<>();
        netTotal = 0.0;
        cancelTotal = 0.0;
        refundTotal = 0.0;
        totalBilled = 0.0;
        totalCancel = 0.0;
        totalRefund = 0.0;
        staff = null;
        sessoinDate = false;
        institution = null;
        date = null;
        bookingCountSummryRows = new ArrayList<>();
        bookingCountSummryRowsScan = new ArrayList<>();
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public ChannelBillTotals getBillTotals() {
        return billTotals;
    }

    public void setBillTotals(ChannelBillTotals billTotals) {
        this.billTotals = billTotals;
    }

    public double getGrantTotalBilled() {
        return grantTotalBilled;
    }

    public void setGrantTotalBilled(double grantTotalBilled) {
        this.grantTotalBilled = grantTotalBilled;
    }

    public double getGrantTotalCancel() {
        return grantTotalCancel;
    }

    public void setGrantTotalCancel(double grantTotalCancel) {
        this.grantTotalCancel = grantTotalCancel;
    }

    public double getGrantTotalRefund() {
        return grantTotalRefund;
    }

    public void setGrantTotalRefund(double grantTotalRefund) {
        this.grantTotalRefund = grantTotalRefund;
    }

    public double getGrantNetTotal() {
        return grantNetTotal;
    }

    public void setGrantNetTotal(double grantNetTotal) {
        this.grantNetTotal = grantNetTotal;
    }

    public double getDoctorFeeTotal() {
        return doctorFeeTotal;
    }

    public void setDoctorFeeTotal(double doctorFeeTotal) {
        this.doctorFeeTotal = doctorFeeTotal;
    }

    public WebUser getWebUser() {
        return webUser;
    }

    public void setWebUser(WebUser webUser) {
        this.webUser = webUser;
    }

    public List<DoctorPaymentSummeryRow> getDoctorPaymentSummeryRows() {
        return doctorPaymentSummeryRows;
    }

    public void setDoctorPaymentSummeryRows(List<DoctorPaymentSummeryRow> doctorPaymentSummeryRows) {
        this.doctorPaymentSummeryRows = doctorPaymentSummeryRows;
    }

    public double getDoctorFeeBilledTotal() {
        return doctorFeeBilledTotal;
    }

    public void setDoctorFeeBilledTotal(double doctorFeeBilledTotal) {
        this.doctorFeeBilledTotal = doctorFeeBilledTotal;
    }

    public double getDoctorFeeCancellededTotal() {
        return doctorFeeCancellededTotal;
    }

    public void setDoctorFeeCancellededTotal(double doctorFeeCancellededTotal) {
        this.doctorFeeCancellededTotal = doctorFeeCancellededTotal;
    }

    public double getDoctorFeeRefundededTotal() {
        return doctorFeeRefundededTotal;
    }

    public void setDoctorFeeRefundededTotal(double doctorFeeRefundededTotal) {
        this.doctorFeeRefundededTotal = doctorFeeRefundededTotal;
    }

    public double getHospitalFeeBilledTotal() {
        return hospitalFeeBilledTotal;
    }

    public void setHospitalFeeBilledTotal(double hospitalFeeBilledTotal) {
        this.hospitalFeeBilledTotal = hospitalFeeBilledTotal;
    }

    public double getHospitalFeeCancellededTotal() {
        return hospitalFeeCancellededTotal;
    }

    public void setHospitalFeeCancellededTotal(double hospitalFeeCancellededTotal) {
        this.hospitalFeeCancellededTotal = hospitalFeeCancellededTotal;
    }

    public double getHospitalFeeRefundededTotal() {
        return hospitalFeeRefundededTotal;
    }

    public void setHospitalFeeRefundededTotal(double hospitalFeeRefundededTotal) {
        this.hospitalFeeRefundededTotal = hospitalFeeRefundededTotal;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    ChannelReportColumnModelBundle rowBundle;
    List<ChannelReportColumnModel> rows;
    List<ChannelReportColumnModel> filteredRows;

    public ChannelReportColumnModelBundle getRowBundle() {
        return rowBundle;
    }

    public void setRowBundle(ChannelReportColumnModelBundle rowBundle) {
        this.rowBundle = rowBundle;
    }

    public List<ChannelReportColumnModel> getFilteredRows() {
        return filteredRows;
    }

    public void setFilteredRows(List<ChannelReportColumnModel> filteredRows) {
        this.filteredRows = filteredRows;
    }

    public List<ChannelReportColumnModel> getRows() {
        return rows;
    }

    public void setRows(List<ChannelReportColumnModel> rows) {
        this.rows = rows;
    }

    public List<BillSession> getSelectedBillSessions() {
        return selectedBillSessions;
    }

    public void setSelectedBillSessions(List<BillSession> selectedBillSessions) {
        this.selectedBillSessions = selectedBillSessions;
    }

    public List<ServiceSession> getServiceSessions() {
        return serviceSessions;
    }

    public void setServiceSessions(List<ServiceSession> serviceSessions) {
        this.serviceSessions = serviceSessions;
    }

    public void fillIncomeWithAgentBookings() {
        Date startTime = new Date();

        String j;
        Map m = new HashMap();

//        Bill b = new Bill();
//        b.setNetTotal(netTotal);
//        
        rows = new ArrayList<>();

        ChannelReportColumnModel br;
        ChannelReportColumnModel cr;
        ChannelReportColumnModel rr;
        ChannelReportColumnModel nr;

        j = "select sum(b.netTotal)"
                + " from Bill b "
                + " where b.retired=false "
                + " and b.createdAt between :fd and :td "
                + " and b.billType in :bts "
                + " and type(b) = :bt "
                + " and b.paymentMethod =:pm ";

        if (institution != null) {
            m.put("ins", institution);
            j += " and b.institution=:ins ";
        }

        List<BillType> bts = new ArrayList<>();
        bts.add(BillType.ChannelAgent);
        bts.add(BillType.ChannelCash);
        bts.add(BillType.ChannelPaid);

        m.put("fd", fromDate);
        m.put("td", toDate);
        m.put("bts", bts);

        //System.out.println("j = " + j);
        //Bookings
        br = new ChannelReportColumnModel();
        m.put("bt", BilledBill.class);
        br.setName("Bookings");

        m.put("pm", PaymentMethod.Cash);
        br.setCashTotal(billFacade.findDoubleByJpql(j, m, TemporalType.TIMESTAMP));

        m.put("pm", PaymentMethod.Card);
        br.setCardTotal(billFacade.findDoubleByJpql(j, m, TemporalType.TIMESTAMP));

        m.put("pm", PaymentMethod.Cheque);
        br.setChequeTotal(billFacade.findDoubleByJpql(j, m, TemporalType.TIMESTAMP));

        m.put("pm", PaymentMethod.Slip);
        br.setSlipTotal(billFacade.findDoubleByJpql(j, m, TemporalType.TIMESTAMP));

        br.setCollectionTotal(br.cashTotal + br.cardTotal + br.chequeTotal);

        m.put("pm", PaymentMethod.Agent);
        br.setAgentTotal(billFacade.findDoubleByJpql(j, m, TemporalType.TIMESTAMP));

        br.setValue(br.getAgentTotal() + br.getCollectionTotal());

        //Cancellations
        cr = new ChannelReportColumnModel();
        m.put("bt", CancelledBill.class);
        cr.setName("Cancellations");

        m.put("pm", PaymentMethod.Cash);
        cr.setCashTotal(billFacade.findDoubleByJpql(j, m, TemporalType.TIMESTAMP));

        m.put("pm", PaymentMethod.Card);
        cr.setCardTotal(billFacade.findDoubleByJpql(j, m, TemporalType.TIMESTAMP));

        m.put("pm", PaymentMethod.Cheque);
        cr.setChequeTotal(billFacade.findDoubleByJpql(j, m, TemporalType.TIMESTAMP));

        m.put("pm", PaymentMethod.Slip);
        cr.setSlipTotal(billFacade.findDoubleByJpql(j, m, TemporalType.TIMESTAMP));

        cr.setCollectionTotal(cr.cashTotal + cr.cardTotal + cr.chequeTotal);

        m.put("pm", PaymentMethod.Agent);
        cr.setAgentTotal(billFacade.findDoubleByJpql(j, m, TemporalType.TIMESTAMP));

        cr.setValue(cr.getAgentTotal() + cr.getCollectionTotal());

        //Refunds
        rr = new ChannelReportColumnModel();
        m.put("bt", RefundBill.class);
        rr.setName("Refunds");

        m.put("pm", PaymentMethod.Cash);
        rr.setCashTotal(billFacade.findDoubleByJpql(j, m, TemporalType.TIMESTAMP));

        m.put("pm", PaymentMethod.Card);
        rr.setCardTotal(billFacade.findDoubleByJpql(j, m, TemporalType.TIMESTAMP));

        m.put("pm", PaymentMethod.Cheque);
        rr.setChequeTotal(billFacade.findDoubleByJpql(j, m, TemporalType.TIMESTAMP));

        m.put("pm", PaymentMethod.Slip);
        rr.setSlipTotal(billFacade.findDoubleByJpql(j, m, TemporalType.TIMESTAMP));

        rr.setCollectionTotal(rr.cashTotal + rr.cardTotal + rr.chequeTotal);

        m.put("pm", PaymentMethod.Agent);
        rr.setAgentTotal(billFacade.findDoubleByJpql(j, m, TemporalType.TIMESTAMP));

        rr.setValue(rr.getAgentTotal() + rr.getCollectionTotal());

        nr = new ChannelReportColumnModel();
        nr.name = "Total";
        nr.cashTotal = br.cashTotal + rr.cashTotal + cr.cardTotal;
        nr.cardTotal = br.cardTotal + rr.cardTotal + cr.cardTotal;
        nr.chequeTotal = br.chequeTotal + rr.chequeTotal + cr.chequeTotal;
        nr.slipTotal = br.slipTotal + rr.slipTotal + cr.slipTotal;
        nr.collectionTotal = br.collectionTotal + rr.collectionTotal + cr.collectionTotal;
        nr.agentTotal = br.agentTotal + rr.agentTotal + cr.agentTotal;
        nr.value = br.value + rr.value + cr.value;

        rows.add(br);
        rows.add(cr);
        rows.add(rr);

        rowBundle = new ChannelReportColumnModelBundle();
        rowBundle.setBundle(nr);
        rowBundle.setRows(rows);

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Payment/Reports/Income report/Income with agent report(/faces/channel/income_with_agent_bookings.xhtml)");

    }

    public List<BillSession> createBillSessionQuery(Bill bill, PaymentEnum paymentEnum, DateEnum dateEnum, ReportKeyWord reportKeyWord) {
        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelOnCall, BillType.ChannelStaff};
        List<BillType> bts = Arrays.asList(billTypes);
        HashMap hm = new HashMap();

        String sql = "SELECT b FROM BillSession b "
                + "  where type(b.bill)=:class "
                + " and b.bill.billType in :bt "
                + " and b.bill.retired=false ";

        if (reportKeyWord.getSpeciality() != null) {
            sql += " and b.staff.speciality=:sp";
            hm.put("sp", reportKeyWord.getSpeciality());
        }

        if (reportKeyWord.getStaff() != null) {
            sql += "and b.staff=:stf";
            hm.put("stf", reportKeyWord.getStaff());
        }

        if (reportKeyWord.getPatient() != null) {
            sql += "and b.bill.patient=:pt";
            hm.put("pt", reportKeyWord.getPatient());
        }

        if (reportKeyWord.getInstitution() != null) {
            sql += "and b.bill.fromInstitution=:ins";
            hm.put("ins", reportKeyWord.getInstitution());
        }

        if (reportKeyWord.getPaymentMethod() != null) {
            sql += "and b.bill.paymentMethod=:pm";
            hm.put("pm", reportKeyWord.getPaymentMethod());
        }

        if (reportKeyWord.getItem() != null) {
            sql += "and b.serviceSession=:sv";
            hm.put("sv", reportKeyWord.getItem());
        }

        switch (paymentEnum) {
            case Paid:
                sql += " and b.bill.paidAmount!=0";
                break;
            case NotPaid:
                sql += " and b.bill.paidAmount=0";
                break;
            case All:
                break;
        }

        switch (dateEnum) {
            case AppointmentDate:
                sql += " and b.bill.appointmentAt between :frm and  :to";
                break;
            case PaidDate:
                sql += " and b.bill.paidAt between :frm and  :to";
                break;
            case CreatedDate:
                sql += " and b.bill.createdAt between :frm and  :to";
                break;

        }
        hm.put("bt", bts);
        hm.put("frm", getFromDate());
        hm.put("to", getToDate());
        hm.put("class", bill.getClass());

        return billSessionFacade.findBySQL(sql, hm, TemporalType.TIMESTAMP);
    }

    public List<Object[]> createBillSessionQueryAgregation(Bill bill, PaymentEnum paymentEnum, DateEnum dateEnum, ReportKeyWord reportKeyWord) {
        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelOnCall, BillType.ChannelStaff};
        List<BillType> bts = Arrays.asList(billTypes);
        HashMap hm = new HashMap();

        String sql = "SELECT sum(b.billItem.qty),b.bill.billType "
                + " FROM BillSession b "
                + "  where type(b.bill)=:class "
                + " and b.bill.billType in :bt "
                + " and b.bill.retired=false ";

        if (reportKeyWord.getSpeciality() != null) {
            sql += " and b.staff.speciality=:sp ";
            hm.put("sp", reportKeyWord.getSpeciality());
        }

        if (reportKeyWord.getStaff() != null) {
            sql += " and b.staff=:stf ";
            hm.put("stf", reportKeyWord.getStaff());
        }

        if (reportKeyWord.getPatient() != null) {
            sql += " and b.bill.patient=:pt ";
            hm.put("pt", reportKeyWord.getPatient());
        }

        if (reportKeyWord.getInstitution() != null) {
            sql += " and b.bill.fromInstitution=:ins ";
            hm.put("ins", reportKeyWord.getInstitution());
        }

        if (reportKeyWord.getPaymentMethod() != null) {
            sql += " and b.bill.paymentMethod=:pm ";
            hm.put("pm", reportKeyWord.getPaymentMethod());
        }

        if (reportKeyWord.getItem() != null) {
            sql += " and b.serviceSession=:sv ";
            hm.put("sv", reportKeyWord.getItem());
        }

        switch (paymentEnum) {
            case Paid:
                sql += " and b.bill.paidAmount!=0";
                break;
            case NotPaid:
                sql += " and b.bill.paidAmount=0";
                break;
            case All:
                break;
        }

        switch (dateEnum) {
            case AppointmentDate:
                sql += " and b.bill.appointmentAt between :frm and  :to";
                break;
            case PaidDate:
                sql += " and b.bill.paidAt between :frm and  :to";
                break;
            case CreatedDate:
                sql += " and b.bill.createdAt between :frm and  :to";
                break;

        }

        sql += " group by b.bill.billType "
                + " order by b.bill.billType ";

        hm.put("bt", bts);
        hm.put("frm", getFromDate());
        hm.put("to", getToDate());
        hm.put("class", bill.getClass());

        return billSessionFacade.findAggregates(sql, hm, TemporalType.TIMESTAMP);
    }

    public void createChannelCashierSummeryTable() {
        channelReportColumnModels = new ArrayList<>();

        FeeType ft[] = {FeeType.OtherInstitution, FeeType.OwnInstitution, FeeType.Staff};
        List<FeeType> fts = Arrays.asList(ft);
        BillType bty[] = {BillType.ChannelCash, BillType.ChannelStaff};
        List<BillType> btys = Arrays.asList(bty);
        PaymentMethod pm[] = {PaymentMethod.Cash, PaymentMethod.Staff};
        List<PaymentMethod> pms = Arrays.asList(pm);

//        if (webUser != null) {
//            doctorFeeTotal = calDoctorFeeNetTotal(pms, btys, FeeType.Staff, webUser);
//        }
        doctorFeeTotal = calDoctorFeeNetTotal(pms, btys, FeeType.Staff);

        for (PaymentMethod p : pm) {
            ChannelReportColumnModel cm = new ChannelReportColumnModel();
            switch (p) {
                case Cash:
                    getChannelCashierSumTotals(PaymentMethod.Cash, BillType.ChannelCash, cm, channelReportColumnModels);
                    break;
                case Staff:
                    getChannelCashierSumTotals(PaymentMethod.Staff, BillType.ChannelStaff, cm, channelReportColumnModels);
                    break;
            }
        }

        grantTotalBilled = 0;
        grantTotalCancel = 0;
        grantTotalRefund = 0;
        grantNetTotal = 0;

        for (ChannelReportColumnModel chm : channelReportColumnModels) {
            grantTotalBilled += chm.getBilledTotal();
            grantTotalCancel += chm.getCancellTotal();
            grantTotalRefund += chm.getRefundTotal();
            grantNetTotal += chm.getTotal();
        }

    }

    public void getChannelCashierSumTotals(PaymentMethod pay, BillType bty, ChannelReportColumnModel chm, List<ChannelReportColumnModel> chmlst) {
//        if (webUser != null) {
//            totalBilled = calCashierNetTotal(new BilledBill(), pay, bty, webUser);
//            totalCancel = calCashierNetTotal(new CancelledBill(), pay, bty, webUser);
//            totalRefund = calCashierNetTotal(new RefundBill(), pay, bty, webUser);
//        }

        totalBilled = calCashierNetTotal(new BilledBill(), pay, bty);
        totalCancel = calCashierNetTotal(new CancelledBill(), pay, bty);
        totalRefund = calCashierNetTotal(new RefundBill(), pay, bty);

        //System.out.println("Billed,Cancell,Refund" + totalBilled + "," + totalCancel + "," + totalRefund);
        if (pay == PaymentMethod.Cash) {
            //System.out.println("payment method=" + pay);
            //System.out.println("Billed,Cancell,Refund" + totalBilled + "," + totalCancel + "," + totalRefund);
            totalBilled += calCashierNetTotal(new BilledBill(), pay, BillType.ChannelPaid);
            totalCancel += calCashierNetTotal(new CancelledBill(), pay, BillType.ChannelPaid);
            totalRefund += calCashierNetTotal(new RefundBill(), pay, BillType.ChannelPaid);
            //System.out.println("netTotal" + netTotal);
        }
        netTotal = totalBilled + totalCancel + totalRefund;
        //System.out.println("netTotal = " + netTotal);

        chm.setPaymentMethod(pay);
        chm.setBilledTotal(totalBilled);
        chm.setCancellTotal(totalCancel);
        chm.setRefundTotal(totalRefund);

        chm.setTotal(netTotal);

        //System.out.println("chmlst = " + chmlst);
        chmlst.add(chm);
    }

    public double calCashierNetTotal(Bill bill, PaymentMethod paymentMethod, BillType billType) {
        HashMap hm = new HashMap();

        String sql = " SELECT sum(b.netTotal) from Bill b "
                + " where type(b)=:class "
                + " and b.retired=false "
                + " and b.billType =:bt "
                + " and b.paymentMethod=:pm "
                + " and b.createdAt between :frm and :to ";

        if (webUser != null) {
            sql += " and b.creater=:wb ";
            hm.put("wb", webUser);
        }
        hm.put("class", bill.getClass());
        hm.put("bt", billType);
        hm.put("pm", paymentMethod);
        hm.put("frm", getFromDate());
        hm.put("to", getToDate());

        return billFacade.findDoubleByJpql(sql, hm, TemporalType.TIMESTAMP);

    }

//    public double calCashierNetTotal(Bill bill, PaymentMethod paymentMethod, BillType billType, WebUser webUser) {
//        HashMap hm = new HashMap();
//
//        String sql = " SELECT sum(b.netTotal) from Bill b "
//                + " where type(b)=:class "
//                + " and b.retired=false "
//                + " and b.billType =:bt "
//                + " and b.paymentMethod=:pm "
//                + " and b.creater=:wu "
//                + " and b.createdAt between :frm and :to ";
//
//        hm.put("class", bill.getClass());
//        hm.put("bt", billType);
//        hm.put("pm", paymentMethod);
//        hm.put("wu", webUser);
//        hm.put("frm", getFromDate());
//        hm.put("to", getToDate());
//
//        return billFacade.findDoubleByJpql(sql, hm, TemporalType.TIMESTAMP);
//
//    }
    public double calDoctorFeeNetTotal(List<PaymentMethod> paymentMethod, List<BillType> billType, FeeType ftp) {
        HashMap hm = new HashMap();

        String sql = " SELECT sum(b.feeValue) from BillFee b "
                //+ " where type(b)=:class "
                + " where b.bill.retired=false "
                + " and b.bill.cancelled=false "
                + " and b.bill.refunded=false "
                + " and b.bill.billType in :bt "
                + " and b.bill.paymentMethod in :pm "
                + " and b.fee.feeType=:ft"
                + " and b.bill.createdAt between :frm and :to ";

        //hm.put("class", bill.getClass());
        if (webUser != null) {
            sql += " and b.bill.creater=:wb";
            hm.put("wb", webUser);
        }

        hm.put("bt", billType);
        hm.put("pm", paymentMethod);
        hm.put("ft", ftp);
        hm.put("frm", getFromDate());
        hm.put("to", getToDate());

        return billFacade.findDoubleByJpql(sql, hm, TemporalType.TIMESTAMP);

    }

//    public double calDoctorFeeNetTotal(List<PaymentMethod> paymentMethod, List<BillType> billType, FeeType ftp, WebUser webUser) {
//        HashMap hm = new HashMap();
//
//        String sql = " SELECT sum(b.feeValue) from BillFee b "
//                //+ " where type(b)=:class "
//                + " where b.bill.retired=false "
//                + " and b.bill.billType in :bt "
//                + " and b.bill.paymentMethod in :pm "
//                + " and b.fee.feeType=:ft "
//                + " and b.bill.creater=:wu "
//                + " and b.bill.createdAt between :frm and :to ";
//
//        //hm.put("class", bill.getClass());
//        hm.put("bt", billType);
//        hm.put("pm", paymentMethod);
//        hm.put("ft", ftp);
//        hm.put("wu", webUser);
//        hm.put("frm", getFromDate());
//        hm.put("to", getToDate());
//
//        return billFacade.findDoubleByJpql(sql, hm, TemporalType.TIMESTAMP);
//
//    }
    public double createBillSessionQueryTotal(Bill bill, PaymentEnum paymentEnum, DateEnum dateEnum, ReportKeyWord reportKeyWord) {
        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelOnCall, BillType.ChannelStaff};
        List<BillType> bts = Arrays.asList(billTypes);
        HashMap hm = new HashMap();

        String sql = "SELECT sum(b.bill.netTotal) FROM BillSession b "
                + "  where type(b.bill)=:class "
                + " and b.bill.billType in :bt "
                + " and b.bill.retired=false ";

        if (reportKeyWord.getSpeciality() != null) {
            sql += " and b.staff.speciality=:sp";
            hm.put("sp", reportKeyWord.getSpeciality());
        }

        if (reportKeyWord.getStaff() != null) {
            sql += "and b.staff=:stf";
            hm.put("stf", reportKeyWord.getStaff());
        }

        if (reportKeyWord.getPatient() != null) {
            sql += "and b.bill.patient=:pt";
            hm.put("pt", reportKeyWord.getPatient());
        }

        if (reportKeyWord.getInstitution() != null) {
            sql += "and b.bill.fromInstitution=:ins";
            hm.put("ins", reportKeyWord.getInstitution());
        }

        if (reportKeyWord.getPaymentMethod() != null) {
            sql += "and b.bill.paymentMethod=:pm";
            hm.put("pm", reportKeyWord.getPaymentMethod());
        }

        if (reportKeyWord.getItem() != null) {
            sql += "and b.serviceSession=:sv";
            hm.put("sv", reportKeyWord.getItem());
        }

        switch (paymentEnum) {
            case Paid:
                sql += " and b.bill.paidAmount!=0";
                break;
            case NotPaid:
                sql += " and b.bill.paidAmount=0";
                break;
            case All:
                break;
        }

        switch (dateEnum) {
            case AppointmentDate:
                sql += " and b.bill.appointmentAt between :frm and  :to";
                break;
            case PaidDate:
                sql += " and b.bill.paidAt between :frm and  :to";
                break;
            case CreatedDate:
                sql += " and b.bill.createdAt between :frm and  :to";
                break;

        }
        hm.put("bt", bts);
        hm.put("frm", getFromDate());
        hm.put("to", getToDate());
        hm.put("class", bill.getClass());

        return billSessionFacade.findDoubleByJpql(sql, hm, TemporalType.TIMESTAMP);
    }

    public void createBillSession_report_1() {
        Date startTime = new Date();

        billSessionsBilled = createBillSessionQuery(new BilledBill(), PaymentEnum.Paid, DateEnum.AppointmentDate, getReportKeyWord());
        billSessionsCancelled = createBillSessionQuery(new CancelledBill(), PaymentEnum.Paid, DateEnum.CreatedDate, getReportKeyWord());
        billSessionsReturn = createBillSessionQuery(new RefundBill(), PaymentEnum.Paid, DateEnum.CreatedDate, getReportKeyWord());

        netTotal = createBillSessionQueryTotal(new BilledBill(), PaymentEnum.Paid, DateEnum.AppointmentDate, getReportKeyWord());
        cancelTotal = createBillSessionQueryTotal(new CancelledBill(), PaymentEnum.Paid, DateEnum.CreatedDate, getReportKeyWord());
        refundTotal = createBillSessionQueryTotal(new RefundBill(), PaymentEnum.Paid, DateEnum.CreatedDate, getReportKeyWord());

        valueList = new ArrayList<>();

        List<Object[]> billedAgg = createBillSessionQueryAgregation(new BilledBill(), PaymentEnum.Paid, DateEnum.AppointmentDate, getReportKeyWord());
        List<Object[]> cancelledAgg = createBillSessionQueryAgregation(new CancelledBill(), PaymentEnum.Paid, DateEnum.CreatedDate, getReportKeyWord());
        List<Object[]> returnedAgg = createBillSessionQueryAgregation(new RefundBill(), PaymentEnum.Paid, DateEnum.CreatedDate, getReportKeyWord());

        String1Value3 channelAgent = new String1Value3();
        String1Value3 channelCall = new String1Value3();
        String1Value3 channelCash = new String1Value3();
        String1Value3 channelStaff = new String1Value3();

        //SETTING BILLED COUNT
        for (Object[] obj : billedAgg) {
            Double dbl = (Double) obj[0];
            BillType btp = (BillType) obj[1];

            switch (btp) {
                case ChannelAgent:
                    channelAgent.setString(btp.getLabel());
                    channelAgent.setValue1(dbl);
                    break;
                case ChannelCash:
                    channelCash.setString(btp.getLabel());
                    channelCash.setValue1(dbl);
                    break;
                case ChannelOnCall:
                    channelCall.setString(btp.getLabel());
                    channelCall.setValue1(dbl);
                    break;
                case ChannelStaff:
                    channelStaff.setString(btp.getLabel());
                    channelStaff.setValue1(dbl);
                    break;
            }
        }

        //SETTING CANCELLED COUNT
        for (Object[] obj : cancelledAgg) {
            Double dbl = (Double) obj[0];
            BillType btp = (BillType) obj[1];

            switch (btp) {
                case ChannelAgent:
                    channelAgent.setString(btp.getLabel());
                    channelAgent.setValue2(dbl);
                    break;
                case ChannelCash:
                    channelCash.setString(btp.getLabel());
                    channelCash.setValue2(dbl);
                    break;
                case ChannelOnCall:
                    channelCall.setString(btp.getLabel());
                    channelCall.setValue2(dbl);
                    break;
                case ChannelStaff:
                    channelStaff.setString(btp.getLabel());
                    channelStaff.setValue2(dbl);
                    break;
            }
        }

        //SETTING REFUND COUNT
        for (Object[] obj : returnedAgg) {
            Double dbl = (Double) obj[0];
            BillType btp = (BillType) obj[1];

            switch (btp) {
                case ChannelAgent:
                    channelAgent.setString(btp.getLabel());
                    channelAgent.setValue3(dbl);
                    break;
                case ChannelCash:
                    channelCash.setString(btp.getLabel());
                    channelCash.setValue3(dbl);
                    break;
                case ChannelOnCall:
                    channelCall.setString(btp.getLabel());
                    channelCall.setValue3(dbl);
                    break;
                case ChannelStaff:
                    channelStaff.setString(btp.getLabel());
                    channelStaff.setValue3(dbl);
                    break;

            }
        }

        getValueList().add(channelAgent);
        getValueList().add(channelCall);
        getValueList().add(channelCash);
        getValueList().add(channelStaff);

        totalBilled = 0.0;
        totalCancel = 0.0;
        totalRefund = 0.0;

        for (String1Value3 ls : getValueList()) {
            totalBilled += ls.getValue1();
            totalCancel += ls.getValue2();
            totalRefund += ls.getValue3();
        }

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Bill report(/faces/channel/channel_report_bill_session_1.xhtml)");

    }

    public void updateChannel() {
        updateChannelCancelBillFee(new CancelledBill());
        updateChannelCancelBillFee(new RefundBill());
    }

    public void updateChannelCancelBillFee(Bill b) {
        String sql;
        Map m = new HashMap();
        BillType[] bts = {BillType.ChannelCash, BillType.ChannelPaid, BillType.ChannelStaff,};
        List<BillType> bt = Arrays.asList(bts);
        sql = " select bs from BillSession bs where "
                + " bs.bill.billType in :bt "
                + " and type(bs.bill)=:cla "
                + " and bs.bill.singleBillSession is null ";

        m.put("cla", b.getClass());
        m.put("bt", bt);
        //System.out.println("getBillSessionFacade().findBySQL(sql, m) = " + getBillSessionFacade().findBySQL(sql, m));
        List<BillSession> billSessions = getBillSessionFacade().findBySQL(sql, m);
        //System.out.println("billSessions = " + billSessions.size());
        for (BillSession bs : billSessions) {
            //System.out.println("In");
            bs.getBill().setSingleBillSession(bs);
            //System.out.println("bs.getSingleBillSession() = " + bs.getBill().getSingleBillSession());
            getBillFacade().edit(bs.getBill());
            //System.out.println("Out");
        }
    }

    List<Bill> billedBills;
    List<Bill> cancelBills;
    List<Bill> refundBills;

    public void channelBillClassList() {
        Date startTime = new Date();

        billedBills = new ArrayList<>();
        cancelBills = new ArrayList<>();
        refundBills = new ArrayList<>();

        billedBills = channelListByBillClass(new BilledBill(), webUser, sessoinDate, institution, agncyOnCall);
        cancelBills = channelListByBillClass(new CancelledBill(), webUser, sessoinDate, institution, agncyOnCall);
        refundBills = channelListByBillClass(new RefundBill(), webUser, sessoinDate, institution, agncyOnCall);

        totalBilled = calTotal(billedBills);
        totalCancel = calTotal(cancelBills);
        totalRefund = calTotal(refundBills);
        totalBilledDoc = calTotalDoc(billedBills);
        totalCancelDoc = calTotalDoc(cancelBills);
        totalRefundDoc = calTotalDoc(refundBills);
        netTotal = totalBilled + totalCancel + totalRefund;
        netTotalDoc = totalBilledDoc + totalCancelDoc + totalRefundDoc;

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Bill report/Bill detail summery(/faces/channel/channel_report_by_bill_class.xhtml)");

    }

    public void channelBillClassListByPaymentMethord() {
        Date startTime = new Date();

        if (webUser == null && !webUserController.hasPrivilege("Developers")) {
            JsfUtil.addErrorMessage("Select User......");
            return;
        }
        if (paymentMethod == null) {
            JsfUtil.addErrorMessage("Select Payment Methord.....");
            return;
        }
        billedBills = new ArrayList<>();
        cancelBills = new ArrayList<>();
        refundBills = new ArrayList<>();
        BillType bt = null;
        switch (paymentMethod) {
            case Cash:
                bt = BillType.ChannelCash;
                break;
            case Cheque:
                bt = BillType.ChannelCash;
                break;
            case Slip:
                bt = BillType.ChannelCash;
                break;
            case Card:
                bt = BillType.ChannelCash;
                break;
            case Agent:
                bt = BillType.ChannelAgent;
                break;
            case OnCall:
                bt = BillType.ChannelPaid;
                break;
            case Staff:
                bt = BillType.ChannelPaid;
                break;
        }
        System.out.println("bt = " + bt);
        billedBills = fetchBills(new BilledBill(), bt, paymentMethod, webUser, fromDate, toDate);
        cancelBills = fetchBills(new CancelledBill(), bt, paymentMethod, webUser, fromDate, toDate);
        refundBills = fetchBills(new RefundBill(), bt, paymentMethod, webUser, fromDate, toDate);

        totalBilled = calTotal(billedBills);
        totalCancel = calTotal(cancelBills);
        totalRefund = calTotal(refundBills);
        totalBilledDoc = calTotalDoc(billedBills);
        totalCancelDoc = calTotalDoc(cancelBills);
        totalRefundDoc = calTotalDoc(refundBills);
        netTotal = totalBilled + totalCancel + totalRefund;
        netTotalDoc = totalBilledDoc + totalCancelDoc + totalRefundDoc;

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/New Channel report/All bill detail by paymnet method(/faces/channel/channel_report_by_bill_class_by_user_by_payment_methord.xhtml)");

    }

    public List<Bill> channelListByBillClass(Bill bill, WebUser webUser, boolean sd, Institution agent, boolean crditAgent) {
        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelOnCall, BillType.ChannelStaff};
        List<BillType> bts = Arrays.asList(billTypes);
        HashMap hm = new HashMap();

        String sql = " select b from Bill b "
                + " where b.retired=false "
                + " and type(b)=:class ";

        if (webUser != null) {
            sql += " and b.creater=:web ";
            hm.put("web", webUser);
        }
        if (sd) {
            sql += " and b.singleBillSession.sessionDate between :fd and :td ";
        } else {
            sql += " and b.createdAt between :fd and :td ";
        }
        if (crditAgent) {
            if (agent != null) {
                sql += " and b.creditCompany=:a ";
                hm.put("a", agent);
            } else {
                sql += " and b.creditCompany is not null ";
            }
            sql += " and b.billType=:bt ";
            hm.put("bt", BillType.ChannelOnCall);
        } else {
            sql += " and b.billType in :bts ";
            hm.put("bts", bts);
        }
//        sql += " order by b.singleBillSession.sessionDate ";

        hm.put("class", bill.getClass());
        hm.put("fd", getFromDate());
        hm.put("td", getToDate());
        System.out.println("sql = " + sql);
        System.out.println("hm = " + hm);
        return billFacade.findBySQL(sql, hm, TemporalType.TIMESTAMP);

    }

    private List<Bill> fetchBills(Bill billClass, BillType bt, PaymentMethod paymentMethod, WebUser wUser, Date fd, Date td) {
        String sql;
        Map temMap = new HashMap();

        sql = "SELECT b FROM Bill b WHERE b.retired=false "
                + " and b.billType=:btp "
                + " and type(b)=:bill "
                + " and b.institution=:ins ";

        if (billClass.getClass().equals(BilledBill.class)) {
            sql += " and b.singleBillSession.sessionDate between :fromDate and :toDate ";
        }
        if (billClass.getClass().equals(CancelledBill.class)) {
            sql += " and b.createdAt between :fromDate and :toDate ";
        }
        if (billClass.getClass().equals(RefundBill.class)) {
            sql += " and b.createdAt between :fromDate and :toDate ";
        }

        if (wUser != null) {
            sql += " and b.creater=:w ";
            temMap.put("w", wUser);
        }

        if (paymentMethod == PaymentMethod.OnCall || paymentMethod == PaymentMethod.Staff || paymentMethod == PaymentMethod.Agent) {
            if (billClass instanceof BilledBill) {
                if (paymentMethod == PaymentMethod.Agent) {
                    sql += " and b.paymentMethod=:pm ";
                    temMap.put("pm", paymentMethod);
                } else {
                    sql += " and b.referenceBill.paymentMethod=:pm ";
                    temMap.put("pm", paymentMethod);
                }
            } else {
                if (paymentMethod == PaymentMethod.Agent) {
                    sql += " and b.billedBill.paymentMethod=:pm ";
                    temMap.put("pm", paymentMethod);
                } else {
                    sql += " and b.billedBill.referenceBill.paymentMethod=:pm ";
                    temMap.put("pm", paymentMethod);
                }
            }

        } else {
            sql += " and b.paymentMethod=:pm ";
            temMap.put("pm", paymentMethod);
        }

        temMap.put("fromDate", fd);
        temMap.put("toDate", td);
        temMap.put("btp", bt);
        temMap.put("ins", getSessionController().getInstitution());
        temMap.put("bill", billClass.getClass());

        sql += " order by b.insId ";
        System.out.println("temMap = " + temMap);
        System.out.println("sql = " + sql);
        return getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

    }

    List<Department> deps;
    List<DepartmentBill> depBills;

    double departmentBilledBillTotal;
    double departmentCanceledBillTotal;
    double departmentRefundBillTotal;

    public void createDepartmentBills() {
        Date startTime = new Date();

        deps = getDepartments();
        depBills = new ArrayList<>();
        for (Department d : deps) {

            DepartmentBill db = new DepartmentBill();
            db.setBillDepartment(d);
            db.setBills(getDepartmentBills(d));
            if (db.getBills() != null && !db.getBills().isEmpty()) {
                db.setDepartmentBillTotal(calTotal(db.getBills()));
                depBills.add(db);

            }
        }

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Bill report/All department bill summery(/faces/channel/channel_report_department_wise_bills.xhtml)");

    }

    public double calTotal(List<Bill> bills) {

        double departmentTotal = 0.0;
        for (Bill bill : bills) {
            departmentTotal += bill.getNetTotal();
        }
        return departmentTotal;
    }

    public double calTotalDoc(List<Bill> bills) {

        double departmentTotal = 0.0;
        for (Bill bill : bills) {
            departmentTotal += bill.getStaffFee();
        }
        return departmentTotal;
    }

//     public void createDepartmentBills() {
//        deps = getDepartments();
//        depBills = new ArrayList<>();
//        for (Department d : deps) {
//
//            List<Object[]> depList = getDepartmentBills(d);
//            if (depList == null) {
//                continue;
//            }
//
//            DepartmentBill db = new DepartmentBill();
//            db.setBillDepartment(d);
//            for (Object[] obj : depList) {
//                List<Bill> bills = new ArrayList<>();
//                if (obj[0] != null) {
//                    bills = (List<Bill>) obj[0];
//                    db.setBills(bills);
//                }
//                if (obj[1] != null) {
//                    db.setDepartmentBillTotal((double) obj[1]);
//                }
//
//            }
//            if (db.getBills() != null && !db.getBills().isEmpty()) {
//                depBills.add(db);
//
//            }
//        }
//
//    }
    public double getDepartmentBilledBillTotal() {
        return departmentBilledBillTotal;
    }

    public void setDepartmentBilledBillTotal(double departmentBilledBillTotal) {
        this.departmentBilledBillTotal = departmentBilledBillTotal;
    }

    public double getDepartmentCanceledBillTotal() {
        return departmentCanceledBillTotal;
    }

    public void setDepartmentCanceledBillTotal(double departmentCanceledBillTotal) {
        this.departmentCanceledBillTotal = departmentCanceledBillTotal;
    }

    public double getDepartmentRefundBillTotal() {
        return departmentRefundBillTotal;
    }

    public void setDepartmentRefundBillTotal(double departmentRefundBillTotal) {
        this.departmentRefundBillTotal = departmentRefundBillTotal;
    }

    public List<Bill> getChannelBillsCancelled() {
        return channelBillsCancelled;
    }

    public void setChannelBillsCancelled(List<Bill> channelBillsCancelled) {
        this.channelBillsCancelled = channelBillsCancelled;
    }

    public List<Bill> getChannelBillsRefunded() {
        return channelBillsRefunded;
    }

    public void setChannelBillsRefunded(List<Bill> channelBillsRefunded) {
        this.channelBillsRefunded = channelBillsRefunded;
    }

    public List<Department> getDepartments() {
        String sql;
        HashMap hm = new HashMap();
        sql = " select d from Department d "
                + " where d.retired=false "
                + " order by d.name";
        return getDepartmentFacade().findBySQL(sql, hm);
    }

    public List<Bill> getDepartmentBills(Department dep) {

        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelOnCall, BillType.ChannelStaff};
        List<BillType> bts = Arrays.asList(billTypes);

        HashMap hm = new HashMap();
        String sql = " select b from Bill b "
                + " where b.billType in :bt "
                + " and b.retired=false "
                + " and b.department=:dep "
                + " and b.createdAt between :fDate and :tDate "
                + " order by b.singleBillSession.sessionDate ";

        hm.put("bt", bts);
        hm.put("dep", dep);
        hm.put("fDate", getFromDate());
        hm.put("tDate", getToDate());
        return billFacade.findBySQL(sql, hm, TemporalType.TIMESTAMP);
    }

    BillsTotals billedBillList;
    BillsTotals canceledBillList;
    BillsTotals refundBillList;

    public void createChannelCashierBillList() {

        getBilledBillList().setBills(createUserBills(new BilledBill(), getWebUser(), getDepartment()));
        getCanceledBillList().setBills(createUserBills(new CancelledBill(), getWebUser(), getDepartment()));
        getRefundBillList().setBills(createUserBills(new RefundBill(), getWebUser(), getDepartment()));

        getBilledBillList().setAgent(calTotal(new BilledBill(), getWebUser(), getDepartment(), PaymentMethod.Agent));
        getCanceledBillList().setAgent(calTotal(new CancelledBill(), getWebUser(), getDepartment(), PaymentMethod.Agent));
        getRefundBillList().setAgent(calTotal(new RefundBill(), getWebUser(), getDepartment(), PaymentMethod.Agent));

        getBilledBillList().setCash(calTotal(new BilledBill(), getWebUser(), getDepartment(), PaymentMethod.Cash));
        getCanceledBillList().setCash(calTotal(new CancelledBill(), getWebUser(), getDepartment(), PaymentMethod.Cash));
        getRefundBillList().setCash(calTotal(new RefundBill(), getWebUser(), getDepartment(), PaymentMethod.Cash));

        getBilledBillList().setCard(calTotal(new BilledBill(), getWebUser(), getDepartment(), PaymentMethod.Card));
        getCanceledBillList().setCard(calTotal(new CancelledBill(), getWebUser(), getDepartment(), PaymentMethod.Card));
        getRefundBillList().setCard(calTotal(new RefundBill(), getWebUser(), getDepartment(), PaymentMethod.Card));

        getBilledBillList().setSlip(calTotal(new BilledBill(), getWebUser(), getDepartment(), PaymentMethod.Slip));
        getCanceledBillList().setSlip(calTotal(new CancelledBill(), getWebUser(), getDepartment(), PaymentMethod.Slip));
        getRefundBillList().setSlip(calTotal(new RefundBill(), getWebUser(), getDepartment(), PaymentMethod.Slip));

        getBilledBillList().setCheque(calTotal(new BilledBill(), getWebUser(), getDepartment(), PaymentMethod.Cheque));
        getCanceledBillList().setCheque(calTotal(new CancelledBill(), getWebUser(), getDepartment(), PaymentMethod.Cheque));
        getRefundBillList().setCheque(calTotal(new RefundBill(), getWebUser(), getDepartment(), PaymentMethod.Cheque));

        createSummary();
    }

    private List<String1Value1> channelSummary;

    public void createSummary() {
        List<BillsTotals> list2 = new ArrayList<>();
        list2.add(billedBillList);
        list2.add(canceledBillList);
        list2.add(refundBillList);

        double agent = 0.0;
        double slip = 0;
        double creditCard = 0.0;
        double cheque = 0.0;
        double cash = 0.0;
        for (BillsTotals bt : list2) {
            if (bt != null) {
                agent += bt.getAgent();
                slip += bt.getSlip();
                creditCard += bt.getCard();
                cheque += bt.getCheque();
                cash += bt.getCash();
            }
        }

        channelSummary = new ArrayList<>();
        String1Value1 tmp1 = new String1Value1();
        tmp1.setString("Final Agent Total");
        tmp1.setValue(agent);
        channelSummary.add(tmp1);

        String1Value1 tmp2 = new String1Value1();
        tmp2.setString("Final Slip Total");
        tmp2.setValue(slip);
        channelSummary.add(tmp2);

        String1Value1 tmp3 = new String1Value1();
        tmp3.setString("Final Cash Total");
        tmp3.setValue(cash);
        channelSummary.add(tmp3);

        String1Value1 tmp4 = new String1Value1();
        tmp4.setString("Final Card Total");
        tmp4.setValue(creditCard);
        channelSummary.add(tmp4);

        String1Value1 tmp5 = new String1Value1();
        tmp5.setString("Final Cheque Total");
        tmp5.setValue(cheque);
        channelSummary.add(tmp5);

        String1Value1 tmp6 = new String1Value1();
        tmp6.setString("Final Summary Total");
        tmp6.setValue(slip + cheque + agent + cash + creditCard);
        channelSummary.add(tmp6);

    }

    public List<String1Value1> getChannelSummary() {
        return channelSummary;
    }

    public void setChannelSummary(List<String1Value1> channelSummary) {
        this.channelSummary = channelSummary;
    }

    public List<Bill> createUserBills(Bill billClass, WebUser webUser, Department department) {

        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelPaid};
        List<BillType> bts = Arrays.asList(billTypes);

        String sql = "SELECT b FROM Bill b WHERE type(b)=:bill "
                + " and b.retired=false "
                + " and b.billType in :bt "
                + " and b.institution=:ins "
                + " and b.createdAt between :fromDate and :toDate";
        Map temMap = new HashMap();

        if (department != null) {
            sql += " and b.department=:dep ";
            temMap.put("dep", department);
        }

        if (webUser != null) {
            sql += " and b.creater=:web ";
            temMap.put("web", webUser);
        }

        temMap.put("fromDate", getFromDate());
        temMap.put("toDate", getToDate());
        temMap.put("bill", billClass.getClass());
        temMap.put("bt", bts);
        temMap.put("ins", getSessionController().getInstitution());

        sql += " order by b.insId ";

        return getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

    }

    public double calTotal(Bill billClass, WebUser wUser, Department department, PaymentMethod paymentMethod) {

        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelPaid};
        List<BillType> bts = Arrays.asList(billTypes);

        String sql = "SELECT sum(b.netTotal) FROM Bill b WHERE"
                + " type(b)=:bill "
                + " and b.retired=false  "
                + " and b.billType in :bt "
                + " and b.paymentMethod=:pm"
                + " and b.institution=:ins"
                + " and b.createdAt between :fromDate and :toDate";
        Map temMap = new HashMap();

        if (department != null) {
            sql += " and b.department=:dep ";
            temMap.put("dep", department);
        }

        if (webUser != null) {
            sql += " and b.creater=:w";
            temMap.put("w", wUser);
        }

        temMap.put("fromDate", getFromDate());
        temMap.put("toDate", getToDate());
        temMap.put("bt", bts);
        temMap.put("pm", paymentMethod);

        temMap.put("ins", getSessionController().getInstitution());
        temMap.put("bill", billClass.getClass());

        sql += " order by b.insId ";

        return getBillFacade().findDoubleByJpql(sql, temMap, TemporalType.TIMESTAMP);

    }

    double finalCashTot;
    double finalAgentTot;
    double finalCardTot;
    double finalChequeTot;
    double finalSlipTot;
    List<WebUserBillsTotal> webUserBillsTotals;

    public void calCashierData() {

        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelOnCall, BillType.ChannelStaff};
        List<BillType> bts = Arrays.asList(billTypes);

        finalCashTot = finalChequeTot = finalCardTot = finalAgentTot = finalSlipTot = 0;
        webUserBillsTotals = new ArrayList<>();
        for (WebUser webUser : getCashiers()) {
            WebUserBillsTotal tmp = new WebUserBillsTotal();
            tmp.setWebUser(webUser);
            List<BillsTotals> billls = new ArrayList<>();

            double uCard = 0;
            double uCash = 0;
            double uCheque = 0;
            double uAgent = 0;
            double uSlip = 0;
            for (BillType btp : bts) {
                BillsTotals newB = createRow(btp, "Billed", new BilledBill(), webUser);

                if (newB.getCard() != 0 || newB.getCash() != 0 || newB.getCheque() != 0 || newB.getAgent() != 0 || newB.getSlip() != 0) {
                    billls.add(newB);
                }

                BillsTotals newC = createRow(btp, "Cancelled", new CancelledBill(), webUser);

                if (newC.getCard() != 0 || newC.getCash() != 0 || newC.getCheque() != 0 || newC.getAgent() != 0 || newC.getSlip() != 0) {
                    billls.add(newC);
                }

                BillsTotals newR = createRow(btp, "Refunded", new RefundBill(), webUser);

                if (newR.getCard() != 0 || newR.getCash() != 0 || newR.getCheque() != 0 || newR.getAgent() != 0 || newR.getSlip() != 0) {
                    billls.add(newR);
                }

                uCard += (newB.getCard() + newC.getCard() + newR.getCard());
                uCash += (newB.getCash() + newC.getCash() + newR.getCash());
                uCheque += (newB.getCheque() + newC.getCheque() + newR.getCheque());
                uAgent += (newB.getAgent() + newC.getAgent() + newR.getAgent());
                uSlip += (newB.getSlip() + newC.getSlip() + newR.getSlip());

            }

            BillsTotals newSum = new BillsTotals();
            newSum.setName("Total ");
            newSum.setBold(true);
            newSum.setCard(uCard);
            newSum.setCash(uCash);
            newSum.setCheque(uCheque);
            newSum.setAgent(uAgent);
            newSum.setSlip(uSlip);

            if (newSum.getCard() != 0 || newSum.getCash() != 0 || newSum.getCheque() != 0 || newSum.getAgent() != 0 || newSum.getSlip() != 0) {
                billls.add(newSum);
            }

            tmp.setBillsTotals(billls);
            webUserBillsTotals.add(tmp);

        }

    }

    private BillsTotals createRow(BillType billType, String suffix, Bill bill, WebUser webUser) {
        BillsTotals newB = new BillsTotals();
        newB.setName(billType.getLabel() + " " + suffix);
        newB.setCard(calTotalValueOwn(webUser, bill, PaymentMethod.Card, billType));
        finalCardTot += newB.getCard();
        newB.setCash(calTotalValueOwn(webUser, bill, PaymentMethod.Cash, billType));
        finalCashTot += newB.getCash();
        newB.setCheque(calTotalValueOwn(webUser, bill, PaymentMethod.Cheque, billType));
        finalChequeTot += newB.getCheque();
        newB.setAgent(calTotalValueOwn(webUser, bill, PaymentMethod.Agent, billType));
        finalAgentTot += newB.getAgent();
        newB.setSlip(calTotalValueOwn(webUser, bill, PaymentMethod.Slip, billType));
        finalSlipTot += newB.getSlip();

        return newB;

    }

    List<WebUser> cashiers;
    @EJB
    WebUserFacade webUserFacade;

    public List<WebUser> getCashiers() {

        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelOnCall, BillType.ChannelStaff};
        List<BillType> bts = Arrays.asList(billTypes);

        String sql;
        Map temMap = new HashMap();
        sql = "select us from "
                + " Bill b "
                + " join b.creater us "
                + " where b.retired=false "
                + " and b.institution=:ins "
                + " and b.billType in :btp "
                + " and b.createdAt between :fromDate and :toDate "
                + " group by us "
                + " having sum(b.netTotal)!=0 ";
        temMap.put("toDate", getToDate());
        temMap.put("fromDate", getFromDate());
        temMap.put("btp", bts);
        temMap.put("ins", sessionController.getInstitution());
        cashiers = getWebUserFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
        if (cashiers == null) {
            cashiers = new ArrayList<>();
        }

        return cashiers;
    }

    private double calTotalValueOwn(WebUser w, Bill billClass, PaymentMethod pM, BillType billType) {

        String sql;
        Map temMap = new HashMap();

        sql = "select sum(b.netTotal) from Bill b where type(b)=:bill and b.creater=:cret and "
                + " b.paymentMethod= :payMethod  and b.institution=:ins"
                + " and b.billType= :billTp and b.createdAt between :fromDate and :toDate ";

        temMap.put("toDate", getToDate());
        temMap.put("fromDate", getFromDate());
        temMap.put("billTp", billType);
        temMap.put("payMethod", pM);
        temMap.put("bill", billClass.getClass());
        temMap.put("cret", w);
        temMap.put("ins", getSessionController().getInstitution());

        return getBillFacade().findDoubleByJpql(sql, temMap, TemporalType.TIMESTAMP);

    }

    public void calCashierDataTotalOnly() {

        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelOnCall, BillType.ChannelStaff};
        List<BillType> bts = Arrays.asList(billTypes);

        finalCashTot = finalChequeTot = finalCardTot = finalAgentTot = finalSlipTot = 0;
        webUserBillsTotals = new ArrayList<>();
        for (WebUser webUser : getCashiers()) {
            WebUserBillsTotal tmp = new WebUserBillsTotal();
            tmp.setWebUser(webUser);
            List<BillsTotals> billls = new ArrayList<>();

            double uCard = 0;
            double uCash = 0;
            double uCheque = 0;
            double uAgent = 0;
            double uSlip = 0;
            for (BillType btp : bts) {
                BillsTotals newB = createRow(btp, "Billed", new BilledBill(), webUser);
                BillsTotals newC = createRow(btp, "Cancelled", new CancelledBill(), webUser);
                BillsTotals newR = createRow(btp, "Refunded", new RefundBill(), webUser);

                uCard += (newB.getCard() + newC.getCard() + newR.getCard());
                uCash += (newB.getCash() + newC.getCash() + newR.getCash());
                uCheque += (newB.getCheque() + newC.getCheque() + newR.getCheque());
                uAgent += (newB.getAgent() + newC.getAgent() + newR.getAgent());
                uSlip += (newB.getSlip() + newC.getSlip() + newR.getSlip());

            }

            BillsTotals newSum = new BillsTotals();
            newSum.setName("Total ");
            newSum.setBold(true);
            newSum.setCard(uCard);
            newSum.setCash(uCash);
            newSum.setCheque(uCheque);
            newSum.setAgent(uAgent);
            newSum.setSlip(uSlip);

            if (newSum.getCard() != 0 || newSum.getCash() != 0 || newSum.getCheque() != 0 || newSum.getAgent() != 0 || newSum.getSlip() != 0) {
                billls.add(newSum);
            }

            tmp.setBillsTotals(billls);
            webUserBillsTotals.add(tmp);

        }

    }

    List<BillFee> billedBillFeeList;
    List<BillFee> cancelledBillFeeList;
    List<BillFee> refundBillFeeList;
    double billedBillTotal;
    double canceledBillTotl;
    double refundBillTotal;

    public void createFeeTable() {

        billedBillFeeList = new ArrayList<>();
        cancelledBillFeeList = new ArrayList<>();
        refundBillFeeList = new ArrayList<>();
        billedBillTotal = 0.0;
        canceledBillTotl = 0.0;
        refundBillTotal = 0.0;

        billedBillFeeList = getBillFee(new BilledBill());
        cancelledBillFeeList = getBillFee(new CancelledBill());
        refundBillFeeList = getBillFee(new RefundBill());

        billedBillTotal = calFeeTotal(new BilledBill());
        canceledBillTotl = calFeeTotal(new CancelledBill());
        refundBillTotal = calFeeTotal(new RefundBill());

    }

    List<BillFee> billFeeList;

    public List<BillFee> getBillFee(Bill bill) {

        String sql;
        Map m = new HashMap();
        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelOnCall, BillType.ChannelStaff};
        List<BillType> bts = Arrays.asList(billTypes);

        FeeType[] feeTypes = {FeeType.Staff, FeeType.OwnInstitution, FeeType.OtherInstitution, FeeType.Service};
        List<FeeType> fts = Arrays.asList(feeTypes);

        sql = " select bf from BillFee  bf where "
                + " bf.bill.retired=false "
                + " and bf.bill.singleBillSession.sessionDate between :fd and :td "
                + " and bf.bill.billType in :bt "
                + " and type(bf.bill)=:class "
                + " and bf.fee.feeType in :ft ";

        if (getWebUser() != null) {
            sql += " and bf.bill.creater=:wu ";
            m.put("wu", getWebUser());
        }

        sql += " order by bf.bill.insId ";

        m.put("fd", getFromDate());
        m.put("td", getToDate());
        m.put("class", bill.getClass());
        m.put("ft", fts);
        m.put("bt", bts);

        billFeeList = getBillFeeFacade().findBySQL(sql, m, TemporalType.TIMESTAMP);

        return billFeeList;
    }

    List<FeetypeFee> feetypeFees;

    public void createFeeTypeBillFeeList() {
        Date startTime = new Date();

        feetypeFees = new ArrayList<>();

        FeeType[] feeTypes = {FeeType.Staff, FeeType.OwnInstitution, FeeType.OtherInstitution, FeeType.Service};
        List<FeeType> fts = Arrays.asList(feeTypes);
        for (FeeType feeType : fts) {
            FeetypeFee ftf = new FeetypeFee();
            ftf.setFeeType(feeType);
            ftf.setBilledBillFees(getBillFeeWithFeeTypes(new BilledBill(), feeType));
            ftf.setCanceledBillFees(getBillFeeWithFeeTypes(new CancelledBill(), feeType));
            ftf.setRefunBillFees(getBillFeeWithFeeTypes(new RefundBill(), feeType));

            ftf.setBilledBillFeeTypeTotal(calFeeTypeTotal(ftf.getBilledBillFees()));
            ftf.setCanceledBillFeeTypeTotal(calFeeTypeTotal(ftf.getCanceledBillFees()));
            ftf.setRefundBillFeeTypeTotal(calFeeTypeTotal(ftf.getRefunBillFees()));

            if (ftf.getBilledBillFees() != null && !ftf.getBilledBillFees().isEmpty() || ftf.getCanceledBillFees() != null && !ftf.getCanceledBillFees().isEmpty() || ftf.getRefunBillFees() != null && !ftf.getRefunBillFees().isEmpty()) {
                feetypeFees.add(ftf);
            }
        }

        billedBillTotal = 0.0;
        canceledBillTotl = 0.0;
        refundBillTotal = 0.0;

        billedBillTotal = calFeeTotal(new BilledBill());
        canceledBillTotl = calFeeTotal(new CancelledBill());
        refundBillTotal = calFeeTotal(new RefundBill());

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Bill report/Bill Fees(/faces/channel/channel_report_by_fee.xhtml)");
    }

    public double calFeeTypeTotal(List<BillFee> billFees) {

        double feeTypeTotal = 0.0;
        for (BillFee bf : billFees) {
            feeTypeTotal += bf.getFee().getFee();
        }
        return feeTypeTotal;
    }

    public List<BillFee> getBillFeeWithFeeTypes(Bill bill, FeeType feeType) {

        String sql;
        Map m = new HashMap();
        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelOnCall, BillType.ChannelStaff};
        List<BillType> bts = Arrays.asList(billTypes);

        sql = " select bf from BillFee  bf where "
                + " bf.bill.retired=false "
                + " and bf.bill.singleBillSession.sessionDate between :fd and :td "
                + " and bf.bill.billType in :bt "
                + " and type(bf.bill)=:class "
                + " and bf.fee.feeType =:ft ";

        if (getWebUser() != null) {
            sql += " and bf.bill.creater=:wu ";
            m.put("wu", getWebUser());
        }

        sql += " order by bf.bill.insId ";

        m.put("fd", getFromDate());
        m.put("td", getToDate());
        m.put("class", bill.getClass());
        m.put("ft", feeType);
        m.put("bt", bts);

        billFeeList = getBillFeeFacade().findBySQL(sql, m, TemporalType.TIMESTAMP);

        return billFeeList;
    }

    //get scan count and other channel count seperatly
    public double countBillByBillTypeAndFeeType(Bill bill, FeeType ft, BillType bt, boolean sessoinDate, boolean paid) {

        String sql;
        Map m = new HashMap();

        sql = " select count(distinct(bf.bill)) from BillFee  bf where "
                + " bf.bill.retired=false "
                + " and bf.bill.billType=:bt "
                + " and type(bf.bill)=:class "
                + " and bf.fee.feeType =:ft "
                + " and bf.feeValue>0 ";

        if (bill.getClass().equals(CancelledBill.class)) {
            sql += " and bf.bill.cancelled=true";
            System.err.println("cancel");
        }
        if (bill.getClass().equals(RefundBill.class)) {
            sql += " and bf.bill.refunded=true";
            System.err.println("Refund");
        }

        if (ft == FeeType.OwnInstitution) {
            sql += " and bf.fee.name =:fn ";
            m.put("fn", "Hospital Fee");
        }

        if (paid) {
            sql += " and bf.bill.paidBill is not null "
                    + " and bf.bill.paidAmount!=0 ";
        }
        if (sessoinDate) {
            if (bill.getClass().equals(BilledBill.class)) {
                sql += " and bf.bill.singleBillSession.sessionDate between :fd and :td ";
            }
            if (bill.getClass().equals(CancelledBill.class)) {
                sql += " and bf.bill.cancelledBill.createdAt between :fd and :td ";
            }
            if (bill.getClass().equals(RefundBill.class)) {
                sql += " and bf.bill.refundedBill.createdAt between :fd and :td ";
            }
        } else {
            if (bill.getClass().equals(BilledBill.class)) {
                sql += " and bf.bill.createdAt between :fd and :td ";
            }
            if (bill.getClass().equals(CancelledBill.class)) {
                sql += " and bf.bill.cancelledBill.createdAt between :fd and :td ";
            }
            if (bill.getClass().equals(RefundBill.class)) {
                sql += " and bf.bill.refundedBill.createdAt between :fd and :td ";
            }

        }

        m.put("fd", getFromDate());
        m.put("td", getToDate());
        m.put("class", BilledBill.class);
        m.put("ft", ft);
        m.put("bt", bt);
//        m.put("fn", "Scan Fee");

        double d = getBillFeeFacade().findAggregateLong(sql, m, TemporalType.TIMESTAMP);

        System.out.println("sql = " + sql);
        System.out.println("m = " + m);
        System.out.println("getBillFeeFacade().findAggregateLong(sql, m, TemporalType.TIMESTAMP) = " + d);
        return d;
    }

    public double hospitalTotalBillByBillTypeAndFeeType(Bill bill, FeeType fts, BillType bt, boolean sessoinDate, boolean paid) {

        String sql;
        Map m = new HashMap();

        sql = " select distinct(bf.bill) from BillFee  bf where "
                + " bf.bill.retired=false "
                + " and bf.bill.billType=:bt "
                + " and type(bf.bill)=:class "
                + " and bf.fee.feeType =:ft "
                + " and bf.feeValue>0 ";

        if (bill.getClass().equals(CancelledBill.class)) {
            sql += " and bf.bill.cancelled=true";
            System.err.println("cancel");
        }
//        if (bill.getClass().equals(RefundBill.class)) {
//            sql += " and bf.bill.refunded=true";
//            System.err.println("Refund");
//        }

        if (fts == FeeType.OwnInstitution) {
            sql += " and bf.fee.name =:fn ";
            m.put("fn", "Hospital Fee");
        }

        if (paid) {
            sql += " and bf.bill.paidBill is not null "
                    + " and bf.bill.paidAmount!=0 ";
        }
        if (sessoinDate) {
            if (bill.getClass().equals(BilledBill.class)) {
                sql += " and bf.bill.singleBillSession.sessionDate between :fd and :td ";
            }
            if (bill.getClass().equals(CancelledBill.class)) {
                sql += " and bf.bill.cancelledBill.createdAt between :fd and :td ";
            }
            if (bill.getClass().equals(RefundBill.class)) {
                sql += " and bf.bill.refundedBill.createdAt between :fd and :td ";
            }
        } else {
            if (bill.getClass().equals(BilledBill.class)) {
                sql += " and bf.bill.createdAt between :fd and :td ";
            }
            if (bill.getClass().equals(CancelledBill.class)) {
                sql += " and bf.bill.cancelledBill.createdAt between :fd and :td ";
            }
            if (bill.getClass().equals(RefundBill.class)) {
                sql += " and bf.bill.refundedBill.createdAt between :fd and :td ";
            }

        }

        m.put("fd", getFromDate());
        m.put("td", getToDate());
        m.put("class", BilledBill.class);
        m.put("ft", fts);
        m.put("bt", bt);
//        m.put("fn", "Scan Fee");

        List<Bill> b = getBillFacade().findBySQL(sql, m, TemporalType.TIMESTAMP);
        double d = 0;

        for (Bill b1 : b) {
            for (BillFee bf : b1.getBillFees()) {
                if (bf.getFee().getFeeType() != FeeType.Staff) {
                    d += (bf.getFeeValue() + bf.getFeeVat());
                }
            }
        }
        System.out.println("b.size() = " + b.size());
        System.out.println("sql = " + sql);
        System.out.println("m = " + m);
        System.out.println("getBillFeeFacade().findAggregateLong(sql, m, TemporalType.TIMESTAMP) = " + d);
        return d;
    }

    public double[] hospitalTotalBillByBillTypeAndFeeTypeWithdocfee(Bill bill, FeeType fts, BillType bt, boolean sessoinDate, boolean paid) {

        String sql;
        Map m = new HashMap();

        sql = " select distinct(bf.bill) from BillFee  bf where "
                + " bf.bill.retired=false "
                + " and bf.bill.billType=:bt "
                + " and type(bf.bill)=:class "
                + " and bf.fee.feeType =:ft "
                + " and bf.feeValue>0 ";

        if (bill.getClass().equals(CancelledBill.class)) {
            sql += " and bf.bill.cancelled=true";
            System.err.println("cancel");
        }
        if (bill.getClass().equals(RefundBill.class)) {
            sql += " and bf.bill.refunded=true";
            System.err.println("Refund");
        }

        if (fts == FeeType.OwnInstitution) {
            sql += " and bf.fee.name =:fn ";
            m.put("fn", "Hospital Fee");
        }

        if (paid) {
            sql += " and bf.bill.paidBill is not null "
                    + " and bf.bill.paidAmount!=0 ";
        }
        if (sessoinDate) {
            if (bill.getClass().equals(BilledBill.class)) {
                sql += " and bf.bill.singleBillSession.sessionDate between :fd and :td ";
            }
            if (bill.getClass().equals(CancelledBill.class)) {
                sql += " and bf.bill.cancelledBill.createdAt between :fd and :td ";
            }
            if (bill.getClass().equals(RefundBill.class)) {
                sql += " and bf.bill.refundedBill.createdAt between :fd and :td ";
            }
        } else {
            if (bill.getClass().equals(BilledBill.class)) {
                sql += " and bf.bill.createdAt between :fd and :td ";
            }
            if (bill.getClass().equals(CancelledBill.class)) {
                sql += " and bf.bill.cancelledBill.createdAt between :fd and :td ";
            }
            if (bill.getClass().equals(RefundBill.class)) {
                sql += " and bf.bill.refundedBill.createdAt between :fd and :td ";
            }

        }

        m.put("fd", getFromDate());
        m.put("td", getToDate());
        m.put("class", BilledBill.class);
        m.put("ft", fts);
        m.put("bt", bt);
//        m.put("fn", "Scan Fee");

        List<Bill> b = getBillFacade().findBySQL(sql, m, TemporalType.TIMESTAMP);
        double[] d = new double[4];
        d[0] = 0.0;
        d[1] = 0.0;
        d[2] = 0.0;
        d[3] = 0.0;
        for (Bill b1 : b) {
            for (BillFee bf : b1.getBillFees()) {
                if (bf.getFee().getFeeType() != FeeType.Staff) {
                    if (!bill.getClass().equals(RefundBill.class)) {
                        d[0] += bf.getFeeValue();
                        d[1] += bf.getFeeVat();
                    }
                } else {
                    d[2] += bf.getFeeValue();
                    d[3] += bf.getFeeVat();
                }
            }
        }
        System.out.println("b.size() = " + b.size());
        System.out.println("sql = " + sql);
        System.out.println("m = " + m);
        System.out.println("getBillFeeFacade().findAggregateLong(sql, m, TemporalType.TIMESTAMP) = " + d);
        return d;
    }

//    public double hospitalTotalBillByBillTypeAndFeeType(Bill bill, List<String> ftps, BillType bt, boolean sessoinDate, boolean paid) {
//
//        String sql;
//        Map m = new HashMap();
//
//        sql = " select sum(bf.bill.hospitalFee) from BillFee  bf "
//                + "where bf.bill.retired=false "
//                + " and bf.bill.billType=:bt "
//                + " and type(bf.bill)=:class "
//                + " and bf.fee.name not in :ftp "
//                + " and bf.feeValue>0 ";
//
//        if (bill.getClass().equals(CancelledBill.class)) {
//            sql += " and bf.bill.cancelled=true";
//            System.err.println("cancel");
//        }
//        if (bill.getClass().equals(RefundBill.class)) {
//            sql += " and bf.bill.refunded=true";
//            System.err.println("Refund");
//        }
//
//        if (paid) {
//            sql += " and bf.bill.paidBill is not null "
//                    + " and bf.bill.paidAmount!=0 ";
//        }
//        if (sessoinDate) {
//            sql += " and bf.bill.singleBillSession.sessionDate between :fd and :td ";
//        } else {
//            sql += " and bf.bill.createdAt between :fd and :td ";
//        }
//
//        m.put("fd", getFromDate());
//        m.put("td", getToDate());
//        m.put("class", BilledBill.class);
//        m.put("ftp", ftps);
//        m.put("bt", bt);
////        m.put("fn", "Scan Fee");
//
//        double d = getBillFeeFacade().findDoubleByJpql(sql, m, TemporalType.TIMESTAMP);
//
//        System.out.println("sql = " + sql);
//        System.out.println("m = " + m);
//        System.out.println("getBillFeeFacade().findAggregateLong(sql, m, TemporalType.TIMESTAMP) = " + d);
//        return d;
//    }
    public double countBillByBillType(Bill bill, BillType bt, boolean sessoinDate, Staff st) {

        String sql;
        Map m = new HashMap();

        sql = " select count(distinct(b)) from Bill  b  "
                + " where b.retired=false ";

        if (sessoinDate) {
            if (bill.getClass().equals(BilledBill.class)) {
                sql += " and b.singleBillSession.sessionDate between :fd and :td "
                        + " and b.paidBill is not null "
                        + " and b.paidAmount!=0 ";
            }
            if (bill.getClass().equals(CancelledBill.class)) {
                sql += " and b.createdAt between :fd and :td ";
            }
            if (bill.getClass().equals(RefundBill.class)) {
                sql += " and b.createdAt between :fd and :td ";
            }
        } else {
            if (bill.getClass().equals(BilledBill.class)) {
                sql += " and b.createdAt between :fd and :td "
                        + " and b.paidBill is not null "
                        + " and b.paidAmount!=0 ";
            }
            if (bill.getClass().equals(CancelledBill.class)) {
                sql += " and b.createdAt between :fd and :td ";
            }
            if (bill.getClass().equals(RefundBill.class)) {
                sql += " and b.createdAt between :fd and :td ";
            }

        }

        if (bt != null) {
            if (bill.getClass().equals(BilledBill.class)) {
                sql += " and b.billType=:bt ";
                m.put("bt", bt);
            }
            if (bill.getClass().equals(CancelledBill.class) || bill.getClass().equals(RefundBill.class)) {
                if (bt == BillType.ChannelOnCall || bt == BillType.ChannelStaff) {
                    sql += " and b.billedBill.referenceBill.billType=:bt ";
                    m.put("bt", bt);
                } else {
                    sql += " and b.billType=:bt ";
                    m.put("bt", bt);
                }

            }
        }

        if (bill != null) {
            sql += " and type(b)=:class ";
            m.put("class", bill.getClass());
        }

        if (st != null) {
            sql += " and b.singleBillSession.staff =:stf ";
            m.put("stf", st);
        }

        m.put("fd", getFromDate());
        m.put("td", getToDate());

        double d = getBillFeeFacade().findAggregateLong(sql, m, TemporalType.TIMESTAMP);

        System.out.println("sql = " + sql);
        System.out.println("m = " + m);
        System.out.println("getBillFeeFacade().findDoubleByJpql(sql, m, TemporalType.TIMESTAMP) = " + d);
        return d;
    }

//    public double countScan(BillType bt, Bill bill, boolean sessoinDate) {
//        Map m = new HashMap();
//        String sql = " select count(bf) from BillFee  bf where "
//                + " bf.bill.retired=false "
//                + " and bf.bill.billType=:bt "
//                + " and bf.feeValue>0 "
//                + " and type(bf.bill)=:class "
//                + " and bf.fee.feeType =:ft"
//                + " and bf.bill.createdAt between :fd and :td ";
//        
//        m.put("fd", getFromDate());
//        m.put("td", getToDate());
//        m.put("ft", FeeType.Service);
//        m.put("class", bill.getClass());
//        m.put("bt", bt);
//        System.out.println("getBillFeeFacade().findDoubleByJpql(sql, m, TemporalType.TIMESTAMP) = " + getBillFeeFacade().findDoubleByJpql(sql, m, TemporalType.TIMESTAMP));
//        return getBillFeeFacade().findAggregateLong(sql, m, TemporalType.TIMESTAMP);
//    }
    FeeType feeType;
    List<BillFee> listBilledBillFees;
    List<BillFee> listCanceledBillFees;
    List<BillFee> listRefundBillFees;

    public void getUsersWithFeeType() {
        Date startTime = new Date();

        if (getFeeType() == null) {
            JsfUtil.addErrorMessage("Please Insert Fee Type");
        } else {
            listBilledBillFees = getBillFeeWithFeeTypes(new BilledBill(), getFeeType());
            listCanceledBillFees = getBillFeeWithFeeTypes(new CancelledBill(), getFeeType());
            listRefundBillFees = getBillFeeWithFeeTypes(new RefundBill(), getFeeType());
        }

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Bill report/Bill fees with fee type(/faces/channel/report_cashier_summery_with_fee-type.xhtml)");

    }

    public void createChannelDoctorPaymentTable() {
        Date startTime = new Date();
        createChannelDoctorPayment(false);

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Consultant report/Doctor payment report(/faces/channel/channel_payment_report.xhtml)");
    }

    public void createChannelDoctorPaymentTableBySession() {
        Date startTime = new Date();
        createChannelDoctorPayment(true);

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Consultant report/Doctor payment report by session(/faces/channel/channel_payment_report_by_session.xhtml)");
    }

    public void createChannelDoctorPayment(boolean bySession) {
        System.out.println("create doctor payment");
        doctorPaymentSummeryRows = new ArrayList<>();

        BillType[] billTypes = {BillType.ChannelCash, BillType.ChannelAgent, BillType.ChannelPaid};
        List<BillType> bts = Arrays.asList(billTypes);

        System.out.println("getChannelPaymentStaffbyClassType(bts, BillType.PaymentBill, fromDate, toDate) = " + getChannelPaymentStaffbyClassType(bts, BillType.PaymentBill, fromDate, toDate));
        List<Staff> staffs = new ArrayList<>();

        if (staff != null) {
            staffs.add(staff);
        } else {
            staffs.addAll(getChannelPaymentStaffbyClassType(bts, BillType.PaymentBill, fromDate, toDate));
        }

        for (Staff stf : staffs) {
            DoctorPaymentSummeryRow doctorPaymentSummeryRow = new DoctorPaymentSummeryRow();
            System.out.println("stf = " + stf);

            doctorPaymentSummeryRow.setConsultant(stf);
            System.out.println("doctorPaymentSummeryRow.getConsultant() = " + doctorPaymentSummeryRow.getConsultant().getPerson().getName());

            if (bySession) {
                doctorPaymentSummeryRow.setDoctorPaymentSummeryRowSubs(getDoctorPaymentSummeryRowSubsBySession(bts, BillType.PaymentBill, stf, fromDate, toDate));
            } else {
                doctorPaymentSummeryRow.setDoctorPaymentSummeryRowSubs(getDoctorPaymentSummeryRowSubs(bts, BillType.PaymentBill, stf, fromDate, toDate));
            }
            if (!doctorPaymentSummeryRow.getDoctorPaymentSummeryRowSubs().isEmpty()) {
                doctorPaymentSummeryRows.add(doctorPaymentSummeryRow);
            }

        }

    }

    public void createDoctorPaymentBySession() {
        Date startTime = new Date();

        System.out.println("create doctor payment");
        doctorPaymentSummeryRows = new ArrayList<>();

        BillType[] billTypes = {BillType.ChannelCash, BillType.ChannelAgent, BillType.ChannelPaid};
        List<BillType> bts = Arrays.asList(billTypes);

        System.out.println("getChannelPaymentStaffbyClassType(bts, BillType.PaymentBill, fromDate, toDate) = " + getChannelPaymentStaffbyClassType(bts, BillType.PaymentBill, fromDate, toDate));
        List<Staff> staffs = new ArrayList<>();

        if (staff != null) {
            staffs.add(staff);
        } else {
            staffs.addAll(getChannelPaymentStaffbyClassType(bts, BillType.PaymentBill, fromDate, toDate));
        }

        for (Staff stf : staffs) {
            DoctorPaymentSummeryRow doctorPaymentSummeryRow = new DoctorPaymentSummeryRow();
            System.out.println("stf = " + stf);

            doctorPaymentSummeryRow.setConsultant(stf);
            System.out.println("doctorPaymentSummeryRow.getConsultant() = " + doctorPaymentSummeryRow.getConsultant().getPerson().getName());

            doctorPaymentSummeryRow.setDoctorPaymentSummeryRowSubs(getSessionTotal(bts, BillType.PaymentBill, stf));

            if (!doctorPaymentSummeryRow.getDoctorPaymentSummeryRowSubs().isEmpty()) {
                doctorPaymentSummeryRows.add(doctorPaymentSummeryRow);
            }

        }

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Consultant report/Doctor payment report by session date(/faces/channel/channel_payment_report_by_date.xhtml)");
    }

    public List<DoctorPaymentSummeryRowSub> getSessionTotal(List<BillType> bts, BillType bt, Staff staff) {
        System.out.println("in getDoctorPaymentSummeryRowSubs");
        List<DoctorPaymentSummeryRowSub> doctorPaymentSummeryRowSubs;
        doctorPaymentSummeryRowSubs = new ArrayList<>();

        List<ServiceSession> sessions = new ArrayList<>();
        double ptCount = 0;

        for (ServiceSession ss : getServiceSessions(fromDate, toDate, staff)) {
            DoctorPaymentSummeryRowSub dpsrs = new DoctorPaymentSummeryRowSub();
            dpsrs.setServiceSession(ss);
            dpsrs.setBills(getChannelPaymentBillListbyClassTypes(bts, bt, null, fromDate, toDate, staff, ss));

            dpsrs.setStaffFeeTotal(getStaffFeeTotal(dpsrs.getBills()));

            double cashCount = 0;
            double onCallCount = 0;
            double agentCount = 0;
            double staffCount = 0;

            for (Bill b : dpsrs.getBills()) {
                if (b.getReferenceBill() == null) {
                    if (b.getPaymentMethod() == PaymentMethod.Cash) {
                        cashCount++;
                        ptCount++;
                    }

                    if (b.getPaymentMethod() == PaymentMethod.Agent) {
                        agentCount++;
                        ptCount++;
                    }
                }

                if (b.getReferenceBill() != null) {
                    if (b.getReferenceBill().getPaymentMethod() == PaymentMethod.OnCall) {
                        onCallCount++;
                        ptCount++;
                    }

                    if (b.getReferenceBill().getPaymentMethod() == PaymentMethod.Staff) {
                        staffCount++;
                        ptCount++;
                    }
                }

                System.out.println("cashCount = " + cashCount);
                System.out.println("agentCount = " + agentCount);
                System.out.println("onCallCount = " + onCallCount);
                System.out.println("staffCount = " + staffCount);

                dpsrs.setCashCount(cashCount);
                dpsrs.setAgentCount(agentCount);
                dpsrs.setOnCallCount(onCallCount);
                dpsrs.setStaffCount(staffCount);

                //ptCount+=(cashCount + agentCount + onCallCount + staffCount);
                System.out.println("ptCount1 = " + ptCount);

            }
            System.out.println("dpsrs.getCashCount() = " + dpsrs.getCashCount());
            System.out.println("dpsrs.getOnCallCount() = " + dpsrs.getOnCallCount());
            System.out.println("dpsrs.getAgentCount() = " + dpsrs.getAgentCount());
            System.out.println("dpsrs.getStaffCount() = " + dpsrs.getStaffCount());

            System.out.println("ptCount2 = " + ptCount);
            dpsrs.setTotalCount(ptCount);

            if (!dpsrs.getBills().isEmpty()) {
                doctorPaymentSummeryRowSubs.add(dpsrs);
            }

        }

        return doctorPaymentSummeryRowSubs;
    }

    public List<DoctorPaymentSummeryRowSub> getDoctorPaymentSummeryRowSubs(List<BillType> bts, BillType bt, Staff staff, Date fd, Date td) {
        System.out.println("in getDoctorPaymentSummeryRowSubs");
        List<DoctorPaymentSummeryRowSub> doctorPaymentSummeryRowSubs;
        doctorPaymentSummeryRowSubs = new ArrayList<>();

        Date nowDate = fd;
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);

        while (nowDate.before(td)) {
            DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(nowDate);
            System.out.println("formattedDate = " + formattedDate);
            System.out.println("nowDate = " + nowDate);

            DoctorPaymentSummeryRowSub doctorPaymentSummeryRowSub = new DoctorPaymentSummeryRowSub();

            doctorPaymentSummeryRowSub.setDate(nowDate);
            System.out.println("doctorPaymentSummeryRowSub.getDate() = " + doctorPaymentSummeryRowSub.getDate());

            doctorPaymentSummeryRowSub.setBills(getChannelPaymentBillListbyClassTypes(bts, bt, nowDate, null, null, staff, null));

            doctorPaymentSummeryRowSub.setHospitalFeeTotal(getHospitalFeeTotal(doctorPaymentSummeryRowSub.getBills()));
            doctorPaymentSummeryRowSub.setStaffFeeTotal(getStaffFeeTotal(doctorPaymentSummeryRowSub.getBills()));

            double cashCount = 0;
            double onCallCount = 0;
            double agentCount = 0;
            double staffCount = 0;

            for (Bill b : doctorPaymentSummeryRowSub.getBills()) {
                if (b.getReferenceBill() == null) {
                    System.out.println("b.getPaymentMethod() = " + b.getPaymentMethod());
                    System.out.println("b.getInsId() = " + b.getInsId());
                    if (b.getPaymentMethod() == PaymentMethod.Cash) {
                        cashCount++;
                        System.out.println("cashCount1 = " + cashCount);
                    }

                    if (b.getPaymentMethod() == PaymentMethod.Agent) {
                        agentCount++;
                        System.out.println("agentCount1 = " + agentCount);
                    }
                }

                if (b.getReferenceBill() != null) {
                    System.out.println("b.getReferenceBill().getPaymentMethod() = " + b.getReferenceBill().getPaymentMethod());
                    System.out.println("b.getReferenceBill().getInsId() = " + b.getInsId());
                    if (b.getReferenceBill().getPaymentMethod() == PaymentMethod.OnCall) {
                        onCallCount++;
                        System.out.println("onCallCount1 = " + onCallCount);
                    }

                    if (b.getReferenceBill().getPaymentMethod() == PaymentMethod.Staff) {
                        staffCount++;
                        System.out.println("staffCount = " + staffCount);
                    }
                }

                System.out.println("cashCount = " + cashCount);
                System.out.println("agentCount = " + agentCount);
                System.out.println("onCallCount = " + onCallCount);
                System.out.println("staffCount = " + staffCount);

                doctorPaymentSummeryRowSub.setCashCount(cashCount);
                doctorPaymentSummeryRowSub.setAgentCount(agentCount);
                doctorPaymentSummeryRowSub.setOnCallCount(onCallCount);
                doctorPaymentSummeryRowSub.setStaffCount(staffCount);

            }

            System.out.println("doctorPaymentSummeryRowSub.getCashCount() = " + doctorPaymentSummeryRowSub.getCashCount());
            System.out.println("doctorPaymentSummeryRowSub.getOnCallCount() = " + doctorPaymentSummeryRowSub.getOnCallCount());
            System.out.println("doctorPaymentSummeryRowSub.getAgentCount() = " + doctorPaymentSummeryRowSub.getAgentCount());
            System.out.println("doctorPaymentSummeryRowSub.getStaffCount() = " + doctorPaymentSummeryRowSub.getStaffCount());

            Calendar nc = Calendar.getInstance();
            nc.setTime(nowDate);
            nc.add(Calendar.DATE, 1);
            nowDate = nc.getTime();

            if (!doctorPaymentSummeryRowSub.getBills().isEmpty()) {
                doctorPaymentSummeryRowSubs.add(doctorPaymentSummeryRowSub);
            }

        }

        return doctorPaymentSummeryRowSubs;
    }

    public void createUnpaidDoctorVoucher() {
        Date startTime = new Date();

        System.out.println("create doctor un-paid");
        doctorPaymentSummeryRows = new ArrayList<>();

        BillType[] billTypes = {BillType.ChannelCash, BillType.ChannelAgent, BillType.ChannelPaid};
        List<BillType> bts = Arrays.asList(billTypes);

        Date nowDate = fromDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);

        while (nowDate.before(toDate)) {
            DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(nowDate);
            System.out.println("formattedDate = " + formattedDate);
            System.out.println("nowDate = " + nowDate);

            DoctorPaymentSummeryRow doctorPaymentSummeryRow = new DoctorPaymentSummeryRow();

            doctorPaymentSummeryRow.setDate(nowDate);
            System.out.println("doctorPaymentSummeryRowSub.getDate() = " + doctorPaymentSummeryRow.getDate());

            doctorPaymentSummeryRow.setDoctorPaymentSummeryRowSubs(getDoctorUnPaidSummeryRowSubs(doctorPaymentSummeryRow.getDate(), bts));

            Calendar nc = Calendar.getInstance();
            nc.setTime(nowDate);
            nc.add(Calendar.DATE, 1);
            nowDate = nc.getTime();

            if (!doctorPaymentSummeryRow.getDoctorPaymentSummeryRowSubs().isEmpty()) {
                doctorPaymentSummeryRows.add(doctorPaymentSummeryRow);
            }
        }

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Consultant report/Doctor un-paid report(/faces/channel/channel_unpaid_report.xhtml)");
    }

    public List<DoctorPaymentSummeryRowSub> getDoctorUnPaidSummeryRowSubs(Date d, List<BillType> bts) {
        System.out.println("in getDoctorUnPaidSummeryRowSubs");
        List<DoctorPaymentSummeryRowSub> doctorPaymentSummeryRowSubs;
        doctorPaymentSummeryRowSubs = new ArrayList<>();

        System.out.println("getChannelPaymentStaffbyClassType(bts, fromDate, toDate) = " + getChannelUnPaidStaffbyClassType(bts, fromDate, toDate));
        List<Staff> staffs = new ArrayList<>();

        if (staff != null) {
            staffs.add(staff);
        } else {
            staffs.addAll(getChannelUnPaidStaffbyClassType(bts, fromDate, toDate));
        }

        for (Staff staff : staffs) {
            DoctorPaymentSummeryRowSub doctorPaymentSummeryRowSub = new DoctorPaymentSummeryRowSub();
            System.out.println("staff = " + staff);
            doctorPaymentSummeryRowSub.setConsultant(staff);
            doctorPaymentSummeryRowSub.setBills(getChannelUnPaidBillListbyClassTypes(bts, d, staff));
            doctorPaymentSummeryRowSub.setHospitalFeeTotal(getHospitalFeeTotal(doctorPaymentSummeryRowSub.getBills()));
            doctorPaymentSummeryRowSub.setStaffFeeTotal(getStaffFeeTotal(doctorPaymentSummeryRowSub.getBills()));

            if (!doctorPaymentSummeryRowSub.getBills().isEmpty()) {
                System.out.println("Adding");
                doctorPaymentSummeryRowSubs.add(doctorPaymentSummeryRowSub);
            }

        }

        return doctorPaymentSummeryRowSubs;
    }

    public List<Bill> getChannelUnPaidBillListbyClassTypes(List<BillType> bts, Date d, Staff stf) {
        System.out.println("Inside getChannelUnPaidBillListbyClassTypes");
        HashMap hm = new HashMap();

        Date fd = commonFunctions.getStartOfDay(d);
        Date td = commonFunctions.getEndOfDay(d);

        System.out.println("td = " + td);
        System.out.println("fd = " + fd);
        System.out.println("stf = " + stf);

        String sql = "SELECT distinct(bf.bill) FROM BillFee bf "
                + " WHERE bf.retired = false "
                + " and bf.bill.cancelled=false "
                + " and bf.bill.refunded=false "
                + " and bf.staff=:st "
                + " and bf.bill.billType in :bts "
                + " and bf.paidValue=0 "
                + " and bf.createdAt between :fd and :td ";

        hm.put("fd", fd);
        hm.put("td", td);
        hm.put("bts", bts);
        hm.put("st", stf);

        System.out.println("Bill List = " + billFacade.findBySQL(sql, hm, TemporalType.TIMESTAMP));

        return billFacade.findBySQL(sql, hm, TemporalType.TIMESTAMP);
    }

    public List<DoctorPaymentSummeryRowSub> getDoctorPaymentSummeryRowSubsBySession(List<BillType> bts, BillType bt, Staff staff, Date fd, Date td) {
        System.out.println("in getDoctorPaymentSummeryRowSubs");
        List<DoctorPaymentSummeryRowSub> doctorPaymentSummeryRowSubs;
        doctorPaymentSummeryRowSubs = new ArrayList<>();

        List<ServiceSession> sessions = new ArrayList<>();

        Date nowDate = fd;
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);

        while (nowDate.before(td)) {
            DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(nowDate);
            System.out.println("formattedDate = " + formattedDate);
            System.out.println("nowDate = " + nowDate);

            if (serviceSession != null) {
                sessions.add(serviceSession);
            } else {
                sessions.addAll(getServiceSessions(nowDate, null, staff));
            }

            System.out.println("getServiceSessions(nowDate, staff) = " + getServiceSessions(nowDate, null, staff));

            for (ServiceSession ss : sessions) {

                DoctorPaymentSummeryRowSub doctorPaymentSummeryRowSub = new DoctorPaymentSummeryRowSub();
                doctorPaymentSummeryRowSub.setServiceSession(ss);
                doctorPaymentSummeryRowSub.setDate(nowDate);
                System.out.println("doctorPaymentSummeryRowSub.getServiceSession() = " + doctorPaymentSummeryRowSub.getServiceSession());
                System.out.println("doctorPaymentSummeryRowSub.getDate() = " + doctorPaymentSummeryRowSub.getDate());
                doctorPaymentSummeryRowSub.setBills(getChannelPaymentBillListbyClassTypes(bts, bt, nowDate, null, null, staff, ss));

                doctorPaymentSummeryRowSub.setHospitalFeeTotal(getHospitalFeeTotal(doctorPaymentSummeryRowSub.getBills()));
                doctorPaymentSummeryRowSub.setStaffFeeTotal(getStaffFeeTotal(doctorPaymentSummeryRowSub.getBills()));

                double cashCount = 0;
                double onCallCount = 0;
                double agentCount = 0;
                double staffCount = 0;

                for (Bill b : doctorPaymentSummeryRowSub.getBills()) {
                    if (b.getReferenceBill() == null) {
                        if (b.getPaymentMethod() == PaymentMethod.Cash) {
                            cashCount++;
                        }

                        if (b.getPaymentMethod() == PaymentMethod.Agent) {
                            agentCount++;
                        }
                    }

                    if (b.getReferenceBill() != null) {
                        if (b.getReferenceBill().getPaymentMethod() == PaymentMethod.OnCall) {
                            onCallCount++;
                        }

                        if (b.getReferenceBill().getPaymentMethod() == PaymentMethod.Staff) {
                            agentCount++;
                        }
                    }

                    System.out.println("cashCount = " + cashCount);
                    System.out.println("agentCount = " + agentCount);
                    System.out.println("onCallCount = " + onCallCount);
                    System.out.println("staffCount = " + staffCount);

                    doctorPaymentSummeryRowSub.setCashCount(cashCount);
                    doctorPaymentSummeryRowSub.setAgentCount(agentCount);
                    doctorPaymentSummeryRowSub.setOnCallCount(onCallCount);
                    doctorPaymentSummeryRowSub.setStaffCount(staffCount);

                }

                System.out.println("doctorPaymentSummeryRowSub.getCashCount() = " + doctorPaymentSummeryRowSub.getCashCount());
                System.out.println("doctorPaymentSummeryRowSub.getOnCallCount() = " + doctorPaymentSummeryRowSub.getOnCallCount());
                System.out.println("doctorPaymentSummeryRowSub.getAgentCount() = " + doctorPaymentSummeryRowSub.getAgentCount());
                System.out.println("doctorPaymentSummeryRowSub.getStaffCount() = " + doctorPaymentSummeryRowSub.getStaffCount());

                Calendar nc = Calendar.getInstance();
                nc.setTime(nowDate);
                nc.add(Calendar.DATE, 1);
                nowDate = nc.getTime();

                if (!doctorPaymentSummeryRowSub.getBills().isEmpty()) {
                    doctorPaymentSummeryRowSubs.add(doctorPaymentSummeryRowSub);
                }

            }

        }

        return doctorPaymentSummeryRowSubs;
    }

    public List<ServiceSession> getServiceSessions(Date fd, Date td, Staff s) {
        HashMap hm = new HashMap();
        String sql = "";

        Date frd = new Date();
        Date tod = new Date();

        if (fd != null && td == null) {
            frd = commonFunctions.getStartOfDay(fd);
            tod = commonFunctions.getEndOfDay(fd);
        }

        sql = "Select distinct(s) From ServiceSession s "
                + " where s.retired=false "
                + " and s.staff=:stf "
                + " and s.sessionDate between :fd and :td ";

        hm.put("stf", s);

        if (fd != null && td == null) {
            hm.put("fd", frd);
            hm.put("td", tod);
        } else {
            hm.put("fd", fd);
            hm.put("td", td);
        }

        return serviceSessionFacade.findBySQL(sql, hm, TemporalType.TIMESTAMP);

    }

    public void fillSessions() {
        System.out.println("Inside");
        String sql;
        Map m = new HashMap();
        sql = "Select s From ServiceSession s "
                + " where s.retired=false "
                + " and type(s)=:class "
                + " and s.staff=:doc "
                + " and s.originatingSession is null "
                + " order by s.sessionWeekday,s.startingTime";
        m.put("doc", staff);
        m.put("class", ServiceSession.class);
        System.out.println("currentStaff = " + staff);
        serviceSessions = serviceSessionFacade.findBySQL(sql, m);
    }

    public List<Bill> getChannelPaymentBillListbyClassTypes(List<BillType> bts, BillType bt, Date d, Date sessionFDate, Date sessionTDate, Staff stf, ServiceSession ss) {
        System.out.println("Inside getStaffbyClassType");
        HashMap hm = new HashMap();

        Date fd = new Date();
        Date td = new Date();

        System.out.println("td = " + td);
        System.out.println("fd = " + fd);
        System.out.println("stf = " + stf);

//        String sql = "select bf from BillFee bf "
//                + " where bf.bill.retired=false "
//                + " and bf.bill.billType=:bt "
//                + " and bf.staff =:st "
//                + " and bf.billItem.paidForBillFee.bill.billType in :bts"
//                + " and bf.createdAt between :fd and :td ";
        String sql = "SELECT bi.paidForBillFee.bill FROM BillItem bi "
                + " WHERE bi.retired = false "
                + " and bi.bill.cancelled=false "
                + " and bi.bill.refunded=false "
                + " and bi.bill.billType=:bt "
                + " and bi.paidForBillFee.staff=:st "
                + " and bi.paidForBillFee.bill.billType in :bts ";

        if (ss != null) {
            System.out.println("ss = " + ss);
            System.out.println("ss date = " + ss.getSessionDate());
            sql += " and bi.paidForBillFee.bill.singleBillSession.serviceSession=:itm ";
            hm.put("itm", ss);
        }

        if (sessionFDate != null && sessionTDate != null) {
            sql += " and bi.paidForBillFee.bill.singleBillSession.serviceSession.sessionDate between :fd and :td ";
            hm.put("fd", sessionFDate);
            hm.put("td", sessionTDate);
        }

        if (d != null) {
            fd = commonFunctions.getStartOfDay(d);
            td = commonFunctions.getEndOfDay(d);
            sql += " and bi.createdAt between :fd and :td ";
            hm.put("fd", fd);
            hm.put("td", td);
        }

        hm.put("bt", bt);
        hm.put("bts", bts);
        hm.put("st", stf);

        System.out.println("Bill List = " + billFacade.findBySQL(sql, hm, TemporalType.TIMESTAMP));

        return billFacade.findBySQL(sql, hm, TemporalType.TIMESTAMP);
    }

    public void createAbsentPatientTable() {
        Date startTime = new Date();
        channelBills = new ArrayList<>();
        channelBills.addAll(getChannelBillsAbsentPatient(staff));

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Absent report/patient Absent report(/faces/channel/channel_report_absent_patients.xhtml)");
    }

    public List<Bill> getChannelBillsAbsentPatient(Staff stf) {
        HashMap hm = new HashMap();
        String sql = " select b from Bill b "
                + " where b.retired=false "
                + " and b.singleBillSession.absent=true "
                + " and b.createdAt between :fd and :td";

        if (stf != null) {
            sql += " and b.staff=:st";
            hm.put("st", stf);
        }

        sql += " order by b.insId ";

        hm.put("fd", fromDate);
        hm.put("td", toDate);

        List<Bill> b = getBillFacade().findBySQL(sql, hm, TemporalType.TIMESTAMP);

        doctorFeeTotal = getStaffFeeTotal(b);

        return b;
    }

    public double getChannelPaymentBillCountbyClassTypes(Bill b, List<BillType> bts, BillType bt, Date d, Staff stf, PaymentMethod pm) {
        System.out.println("Inside getStaffbyClassType");
        HashMap hm = new HashMap();

        Date fd = commonFunctions.getStartOfDay(d);
        Date td = commonFunctions.getEndOfDay(d);

        System.out.println("td = " + td);
        System.out.println("fd = " + fd);
        System.out.println("stf = " + stf);

        String sql = "SELECT count(bi.paidForBillFee.bill) FROM BillItem bi "
                + " WHERE bi.retired = false "
                + " and bi.bill.cancelled=false "
                + " and bi.bill.refunded=false "
                + " and bi.bill.billType=:bt "
                + " and bi.paidForBillFee.staff=:st "
                + " and bi.paidForBillFee.bill.billType in :bts "
                + " and bi.createdAt between :fd and :td ";

        if (b.getReferenceBill() == null) {
            System.out.println("b.getPaymentMethod()1 = " + b.getPaymentMethod());
            System.out.println("b.getInsId()1 = " + b.getInsId());
            sql += " and bi.paidForBillFee.bill.paymentMethod=:pay";
        }

        if (b.getReferenceBill() != null) {
            System.out.println("b.getReferenceBill().getPaymentMethod()2 = " + b.getReferenceBill().getPaymentMethod());
            System.out.println("b.getReferenceBill().getInsId()2 = " + b.getReferenceBill().getInsId());
            sql += " and bi.paidForBillFee.bill.referenceBill.paymentMethod=:pay";
        }

        hm.put("bt", bt);
        hm.put("fd", fd);
        hm.put("td", td);
        hm.put("bts", bts);
        hm.put("pay", pm);
        hm.put("st", stf);

        System.out.println("Bill List = " + billFacade.findBySQL(sql, hm, TemporalType.TIMESTAMP));

        return billFacade.findAggregateLong(sql, hm, TemporalType.TIMESTAMP);
    }

    public List<Staff> getChannelPaymentStaffbyClassType(List<BillType> bts, BillType bt, Date fd, Date td) {
        System.out.println("Inside getStaffbyClassType");
        HashMap hm = new HashMap();

//        String sql = "select distinct(bf.staff) from BillFee bf "
//                + " where bf.bill.retired=false "
//                + " and bf.bill.billType=:bt "
//                + " and bf.staff is not null "
//                + " and bf.billItem.paidForBillFee.bill.billType in :bts"
//                + " and bf.createdAt between :fd and :td ";
        String sql = "SELECT distinct(bi.paidForBillFee.staff) FROM BillItem bi "
                + " WHERE bi.retired = false "
                + " and bi.bill.billType=:bt "
                + " and bi.bill.cancelled=false "
                + " and bi.bill.refunded=false "
                + " and bi.paidForBillFee.staff is not null "
                //+ " and type(bi.bill)=:class "
                + " and bi.paidForBillFee.bill.billType in :bts "
                + " and bi.createdAt between :fromDate and :toDate ";

        hm.put("bt", bt);
        hm.put("fromDate", fd);
        hm.put("toDate", td);
        hm.put("bts", bts);

        return staffFacade.findBySQL(sql, hm, TemporalType.TIMESTAMP);
    }

    public List<Staff> getChannelUnPaidStaffbyClassType(List<BillType> bts, Date fd, Date td) {
        System.out.println("Inside getChannelUnPaidStaffbyClassType");
        HashMap hm = new HashMap();

        String sql = "SELECT distinct(bf.staff) FROM BillFee bf "
                + " WHERE bf.retired = false "
                + " and bf.bill.cancelled=false "
                + " and bf.bill.refunded=false "
                + " and bf.staff is not null "
                //+ " and type(bi.bill)=:class "
                + " and bf.bill.billType in :bts "
                + " and bf.paidValue=0 "
                + " and bf.createdAt between :fromDate and :toDate ";

        //hm.put("bt", bt);
        hm.put("fromDate", fd);
        hm.put("toDate", td);
        hm.put("bts", bts);

        return staffFacade.findBySQL(sql, hm, TemporalType.TIMESTAMP);
    }

    public void createAllChannelBillReportByCreatedDate() {
        Date startTime = new Date();
        createAllChannelBillReport(true);

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Bill report/All channel bills(Process Created Date)(/faces/channel/channel_report_all_bills.xhtml)");
    }

    public void createAllChannelBillReportBySessionDate() {
        Date startTime = new Date();
        createAllChannelBillReport(false);

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Bill report/All channel bills(Process Session Date)(/faces/channel/channel_report_all_bills.xhtml)");
    }

    public void createAllChannelBillReportForVat() {
        Date startTime = new Date();
        channelBills = new ArrayList<>();
        institution=null;

        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelPaid};
        List<BillType> bts = Arrays.asList(billTypes);
        if (sessoinDate) {
            channelBills.addAll(channelBillListByBillType(false, new BilledBill(), bts, fromDate, toDate));
            channelBills.addAll(channelBillListByBillType(false, new CancelledBill(), bts, fromDate, toDate));
            channelBills.addAll(channelBillListByBillType(false, new RefundBill(), bts, fromDate, toDate));
            channelTotal = new ChannelTotal();
            channelTotal.setStaffFee(channelBillTotalByBillType(false, new BilledBill(), bts, fromDate, toDate, true, false, false, false)
                    + channelBillTotalByBillType(false, new CancelledBill(), bts, fromDate, toDate, true, false, false, false)
                    + channelBillTotalByBillType(false, new RefundBill(), bts, fromDate, toDate, true, false, false, false));
            channelTotal.setHosFee(channelBillTotalByBillType(false, new BilledBill(), bts, fromDate, toDate, false, true, false, false)
                    + channelBillTotalByBillType(false, new CancelledBill(), bts, fromDate, toDate, false, true, false, false)
                    + channelBillTotalByBillType(false, new RefundBill(), bts, fromDate, toDate, false, true, false, false));
            channelTotal.setNetTotal(channelBillTotalByBillType(false, new BilledBill(), bts, fromDate, toDate, false, false, true, false)
                    + channelBillTotalByBillType(false, new CancelledBill(), bts, fromDate, toDate, false, false, true, false)
                    + channelBillTotalByBillType(false, new RefundBill(), bts, fromDate, toDate, false, false, true, false));
            channelTotal.setVat(channelBillTotalByBillType(false, new BilledBill(), bts, fromDate, toDate, false, false, false, true)
                    + channelBillTotalByBillType(false, new CancelledBill(), bts, fromDate, toDate, false, false, false, true)
                    + channelBillTotalByBillType(false, new RefundBill(), bts, fromDate, toDate, false, false, false, true));
        } else {
            channelBills.addAll(channelBillListByBillType(true, new BilledBill(), bts, fromDate, toDate));
            channelBills.addAll(channelBillListByBillType(true, new CancelledBill(), bts, fromDate, toDate));
            channelBills.addAll(channelBillListByBillType(true, new RefundBill(), bts, fromDate, toDate));
            channelTotal = new ChannelTotal();
            channelTotal.setStaffFee(channelBillTotalByBillType(true, new BilledBill(), bts, fromDate, toDate, true, false, false, false)
                    + channelBillTotalByBillType(true, new CancelledBill(), bts, fromDate, toDate, true, false, false, false)
                    + channelBillTotalByBillType(true, new RefundBill(), bts, fromDate, toDate, true, false, false, false));
            channelTotal.setHosFee(channelBillTotalByBillType(true, new BilledBill(), bts, fromDate, toDate, false, true, false, false)
                    + channelBillTotalByBillType(true, new CancelledBill(), bts, fromDate, toDate, false, true, false, false)
                    + channelBillTotalByBillType(true, new RefundBill(), bts, fromDate, toDate, false, true, false, false));
            channelTotal.setNetTotal(channelBillTotalByBillType(true, new BilledBill(), bts, fromDate, toDate, false, false, true, false)
                    + channelBillTotalByBillType(true, new CancelledBill(), bts, fromDate, toDate, false, false, true, false)
                    + channelBillTotalByBillType(true, new RefundBill(), bts, fromDate, toDate, false, false, true, false));
            channelTotal.setVat(channelBillTotalByBillType(true, new BilledBill(), bts, fromDate, toDate, false, false, false, true)
                    + channelBillTotalByBillType(true, new CancelledBill(), bts, fromDate, toDate, false, false, false, true)
                    + channelBillTotalByBillType(true, new RefundBill(), bts, fromDate, toDate, false, false, false, true));
        }

        commonController.printReportDetails(fromDate, toDate, startTime, "OPD/Summery/6) Bill Lists/3)opd Vat(reportCashier/report_opd_bills_for_vat.xhtml)");
    }

    public void createAllOPDBillReportForVat() {
        Date startTime = new Date();
        channelBills = new ArrayList<>();

        BillType[] billTypes = {BillType.OpdBill};
        List<BillType> bts = Arrays.asList(billTypes);

        channelBills.addAll(channelBillListByBillType2(true, null, bts, fromDate, toDate));
        channelTotal = new ChannelTotal();
        channelTotal.setStaffFee(channelBillTotalByBillType(true, null, bts, fromDate, toDate, true, false, false, false));
        channelTotal.setHosFee(channelBillTotalByBillType(true, null, bts, fromDate, toDate, false, true, false, false));
        channelTotal.setNetTotal(channelBillTotalByBillType(true, null, bts, fromDate, toDate, false, false, true, false));
        channelTotal.setVat(channelBillTotalByBillType(true, null, bts, fromDate, toDate, false, false, false, true));

        commonController.printReportDetails(fromDate, toDate, startTime, "OPD/Summery/6) Bill Lists/3)opd Vat(reportCashier/report_opd_bills_for_vat.xhtml)");
    }

    public void createAllChannelBillReport(boolean createdDate) {
        channelBills = new ArrayList<>();
        channelBillsCancelled = new ArrayList<>();
        channelBillsRefunded = new ArrayList<>();

        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelPaid};
        List<BillType> bts = Arrays.asList(billTypes);

        channelBills.addAll(channelBillListByBillType(createdDate, new BilledBill(), bts, fromDate, toDate));
        channelBillsCancelled.addAll(channelBillListByBillType(createdDate, new CancelledBill(), bts, fromDate, toDate));
        channelBillsRefunded.addAll(channelBillListByBillType(createdDate, new RefundBill(), bts, fromDate, toDate));

        doctorFeeBilledTotal = getStaffFeeTotal(channelBills);
        doctorFeeCancellededTotal = getStaffFeeTotal(channelBillsCancelled);
        doctorFeeRefundededTotal = getStaffFeeTotal(channelBillsRefunded);

        hospitalFeeBilledTotal = getHospitalFeeTotal(channelBills);
        hospitalFeeCancellededTotal = getHospitalFeeTotal(channelBillsCancelled);
        hospitalFeeRefundededTotal = getHospitalFeeTotal(channelBillsRefunded);
    }

    public double getHospitalFeeTotal(List<Bill> bills) {
        double tot = 0;

        for (Bill b : bills) {
            tot += (b.getNetTotal() - b.getStaffFee());
        }
        return tot;
    }

    public double getStaffFeeTotal(List<Bill> bills) {
        double tot = 0;

        for (Bill b : bills) {
            tot += b.getStaffFee();
        }
        return tot;
    }

    public List<Bill> channelBillListByBillType(boolean createdDate, Bill bill, List<BillType> billTypes, Date fd, Date td) {

        HashMap hm = new HashMap();

        String sql = " select b from Bill b "
                + " where b.retired=false ";
        if (createdDate) {
            sql += " and b.createdAt between :fDate and :tDate ";
        } else {
            if (bill.getClass().equals(BilledBill.class)) {
                sql += " and b.singleBillSession.sessionDate between :fDate and :tDate ";
            }
            if (bill.getClass().equals(CancelledBill.class)) {
                sql += " and b.createdAt between :fDate and :tDate ";
            }
            if (bill.getClass().equals(RefundBill.class)) {
                sql += " and b.createdAt between :fDate and :tDate ";
            }
        }

        if (!billTypes.isEmpty()) {
            sql += " and b.billType in :bt ";
            hm.put("bt", billTypes);
        }

        if (bill != null) {
            sql += " and type(b)=:class";
            hm.put("class", bill.getClass());
        }

        sql += " order by b.createdAt ";

        hm.put("fDate", fd);
        hm.put("tDate", td);

        return billFacade.findBySQL(sql, hm, TemporalType.TIMESTAMP);

    }

    public List<Bill> channelBillListByBillType2(boolean createdDate, Bill bill, List<BillType> billTypes, Date fd, Date td) {

        HashMap hm = new HashMap();

        String sql = " select b from Bill b "
                + " where b.retired=false ";
        if (createdDate) {
            sql += " and b.createdAt between :fDate and :tDate ";
        } else {
            sql += " and b.singleBillSession.sessionDate between :fDate and :tDate ";
        }

        if (!billTypes.isEmpty()) {
            sql += " and b.billType in :bt ";
            hm.put("bt", billTypes);
        }

        if (bill != null) {
            sql += " and type(b)=:class";
            hm.put("class", bill.getClass());
        }
        
        if (institution!=null) {
            sql+=" and  b.institution=:ins ";
            hm.put("ins", institution);
        }

        sql += " order by b.toDepartment.name ,b.createdAt ";

        hm.put("fDate", fd);
        hm.put("tDate", td);

        return billFacade.findBySQL(sql, hm, TemporalType.TIMESTAMP);

    }

    public double channelBillTotalByBillType(boolean createdDate, Bill bill, List<BillType> billTypes, Date fd, Date td, boolean staff, boolean hos, boolean net, boolean vat) {

        HashMap hm = new HashMap();
        String sql = "";

        if (vat) {
            sql = " select sum(b.vat) from Bill b "
                    + " where b.retired=false ";
        }
        if (staff) {
            sql = " select sum(b.staffFee) from Bill b "
                    + " where b.retired=false ";
        }
        if (net) {
            sql = " select sum(b.netTotal) from Bill b "
                    + " where b.retired=false ";
        }
        if (hos) {
            sql = " select sum(b.netTotal-b.staffFee) from Bill b "
                    + " where b.retired=false ";
        }

        if (createdDate) {
            sql += " and b.createdAt between :fDate and :tDate ";
        } else {
            if (bill.getClass().equals(BilledBill.class)) {
                sql += " and b.singleBillSession.sessionDate between :fDate and :tDate ";
            }
            if (bill.getClass().equals(CancelledBill.class)) {
                sql += " and b.createdAt between :fDate and :tDate ";
            }
            if (bill.getClass().equals(RefundBill.class)) {
                sql += " and b.createdAt between :fDate and :tDate ";
            }
        }

        if (!billTypes.isEmpty()) {
            sql += " and b.billType in :bt ";
            hm.put("bt", billTypes);
        }

        if (bill != null) {
            sql += " and type(b)=:class";
            hm.put("class", bill.getClass());
        }
        
        if (institution!=null) {
            sql+=" and b.institution=:ins ";
            hm.put("ins", institution);
        }

        hm.put("fDate", fd);
        hm.put("tDate", td);

        return billFacade.findDoubleByJpql(sql, hm, TemporalType.TIMESTAMP);

    }

    public void createConsultantCountTableByCreatedDate() {
        Date startTime = new Date();
        createConsultantCountTable(false);

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Count/Count report by doctor/(/faces/channel/channel_report_by_consultant.xhtml)");
    }

    public void createConsultantCountTableBySessionDate() {
        createConsultantCountTable(true);
    }

    public void createConsultantCountTable(boolean sessionDate) {
        bookingCountSummryRows = new ArrayList<>();
        double billedCount = 0;
        double canceledCount = 0;
        double refundCount = 0;
        channelTotal = new ChannelTotal();
        BillType[] billTypes = {BillType.ChannelCash, BillType.ChannelAgent, BillType.ChannelOnCall, BillType.ChannelStaff};
        List<BillType> bts = Arrays.asList(billTypes);

//        System.out.println("getStaffbyClassType(bts) = " + getStaffbyClassType(bts, fromDate, toDate));
        for (Staff s : getStaffbyClassType(bts, fromDate, toDate)) {

            System.out.println("s = " + s);
            BookingCountSummryRow row = new BookingCountSummryRow();
            double[] arr = new double[4];
            int i = 0;
            System.out.println("i = " + i);

            row.setConsultant(s);

            for (BillType bt : bts) {
                billedCount = countBillByBillType(new BilledBill(), bt, sessionDate, s);
                canceledCount = countBillByBillType(new CancelledBill(), bt, sessionDate, s);
                refundCount = countBillByBillType(new RefundBill(), bt, sessionDate, s);
                arr[i] = billedCount - (canceledCount + refundCount);
                i++;
                System.out.println("i" + i);
                System.out.println("bilType" + bt);
                System.out.println("billedCount" + billedCount);
                System.out.println("canceledCount" + canceledCount);
                System.out.println("refundCount" + refundCount);
            }
            row.setCashCount(arr[0]);
            row.setAgentCount(arr[1]);
            row.setOncallCount(arr[2]);
            row.setStaffCount(arr[3]);
            channelTotal.setVat(channelTotal.getVat() + row.getCashCount());
            channelTotal.setDiscount(channelTotal.getDiscount() + row.getAgentCount());
            channelTotal.setHosFee(channelTotal.getHosFee() + row.getOncallCount());
            channelTotal.setNetTotal(channelTotal.getNetTotal() + row.getStaffCount());

            bookingCountSummryRows.add(row);

        }
    }

    public List<Staff> getStaffbyClassType(List<BillType> bts, Date fd, Date td) {
        System.out.println("Inside getStaffbyClassType");
        HashMap hm = new HashMap();
//        String sql = "select p from Staff p where p.retired=false ";
//        
//        if(st!=null){
//            System.out.println("1");
//            sql+=" and type(p)=:class ";
//            hm.put("class", st.getClass());
//        }

        String sql = "select distinct(bf.staff) from BillFee bf "
                + " where bf.bill.retired=false "
                + " and bf.bill.billType in :bts "
                + " and bf.staff is not null "
                + " and bf.createdAt between :fd and :td ";

        hm.put("bts", bts);
        hm.put("fd", fd);
        hm.put("td", td);

        return staffFacade.findBySQL(sql, hm, TemporalType.TIMESTAMP);
    }

    public void createChannelPatientCountByCreatedDate() {
        Date startTime = new Date();

        createChannelPatientCount(false);

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Channel patient count(By Created Date)(/faces/channel/channel_report_patient_count.xhtml)");
    }

    public void createChannelPatientCountBySessionDate() {
        Date startTime = new Date();
        createChannelPatientCount(true);
        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Channel patient count(By Session Date)(/faces/channel/channel_report_patient_count.xhtml)");
    }

    public void createChannelHospitalIncomeByCreatedDate() {
        Date startTime = new Date();
        createChannelHospitalIncome(false);

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Channel patient count(By Created Date)(/faces/channel/channel_report_patient_count_income.xhtml)");
    }

    public void createChannelHospitalIncomeBySessionDate() {
        Date startTime = new Date();
        createChannelHospitalIncome(true);

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Channel patient count(By Session Date)(/faces/channel/channel_report_patient_count_income.xhtml)");
    }

    public void createChannelHospitalIncomeByCreatedDateWithDoc() {
        Date startTime = new Date();
        createChannelHospitalIncomeWithDoc(false);

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Channel patient count(By Created Date)(/faces/channel/channel_report_patient_count_income.xhtml)");
    }

    public void createChannelHospitalIncomeBySessionDateWithDoc() {
        Date startTime = new Date();
        createChannelHospitalIncomeWithDoc(true);

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Channel patient count(By Session Date)(/faces/channel/channel_report_patient_count_income.xhtml)");
    }

    public void createChannelPatientCount(boolean sessionDate) {
        bookingCountSummryRows = new ArrayList<>();
        BillType[] billTypes = {BillType.ChannelCash,
            BillType.ChannelOnCall,
            BillType.ChannelStaff,
            BillType.ChannelAgent,};
        List<BillType> bts = Arrays.asList(billTypes);
        createSmmeryRows(bts, sessionDate, FeeType.OwnInstitution);
        createSmmeryRows(bts, sessionDate, FeeType.Service);
        BookingCountSummryRow row = new BookingCountSummryRow();
        row.setBookingType("Total");
        for (BookingCountSummryRow bc : bookingCountSummryRows) {
            row.setBilledCount(row.getBilledCount() + bc.getBilledCount());
            row.setCancelledCount(row.getCancelledCount() + bc.getCancelledCount());
            row.setRefundCount(row.getRefundCount() + bc.getRefundCount());
        }
        bookingCountSummryRows.add(row);
    }

    public void createChannelHospitalIncome(boolean sessionDate) {
        bookingCountSummryRows = new ArrayList<>();
        BillType[] billTypes = {BillType.ChannelCash,
            BillType.ChannelOnCall,
            BillType.ChannelStaff,
            BillType.ChannelAgent,};
        List<BillType> bts = Arrays.asList(billTypes);
        createSmmeryRowsHospitalIncome(bts, sessionDate, FeeType.OwnInstitution);
        createSmmeryRowsHospitalIncome(bts, sessionDate, FeeType.Service);
        BookingCountSummryRow row = new BookingCountSummryRow();
        row.setBookingType("Total");
        for (BookingCountSummryRow bc : bookingCountSummryRows) {
            row.setBilledCount(row.getBilledCount() + bc.getBilledCount());
            row.setCancelledCount(row.getCancelledCount() + bc.getCancelledCount());
            row.setRefundCount(row.getRefundCount() + bc.getRefundCount());
        }
        bookingCountSummryRows.add(row);
    }

    public void createChannelHospitalIncomeWithDoc(boolean sessionDate) {
        bookingCountSummryRows = new ArrayList<>();
        bookingCountSummryRowsScan = new ArrayList<>();
        BillType[] billTypes = {BillType.ChannelCash,
            BillType.ChannelOnCall,
            BillType.ChannelStaff,
            BillType.ChannelAgent,};
        List<BillType> bts = Arrays.asList(billTypes);
        bookingCountSummryRows = createSmmeryRowsHospitalIncomeWithDoc(bts, sessionDate, FeeType.OwnInstitution);
        BookingCountSummryRow row = new BookingCountSummryRow();
        row.setBookingType("Total");
        for (BookingCountSummryRow bc : bookingCountSummryRows) {
            row.setBillHos(row.getBillHos() + bc.getBillHos());
            row.setBillHosVat(row.getBillHosVat() + bc.getBillHosVat());
            row.setbDoc(row.getbDoc() + bc.getbDoc());
            row.setbDocVat(row.getbDocVat() + bc.getbDocVat());
            row.setCanHos(row.getCanHos() + bc.getCanHos());
            row.setCanHosVat(row.getCanHosVat() + bc.getCanHosVat());
            row.setCanDoc(row.getCanDoc() + bc.getCanDoc());
            row.setCanDocVat(row.getCanDocVat() + bc.getCanDocVat());
            row.setRefHos(row.getRefHos() + bc.getRefHos());
            row.setRefHosVat(row.getRefHosVat() + bc.getRefHosVat());
            row.setRefDoc(row.getRefDoc() + bc.getRefDoc());
            row.setRefDocVat(row.getRefDocVat() + bc.getRefDocVat());
        }
        bookingCountSummryRows.add(row);

        bookingCountSummryRowsScan = createSmmeryRowsHospitalIncomeWithDoc(bts, sessionDate, FeeType.Service);

        row = new BookingCountSummryRow();
        row.setBookingType("Total");
        for (BookingCountSummryRow bc : bookingCountSummryRowsScan) {
            row.setBillHos(row.getBillHos() + bc.getBillHos());
            row.setBillHosVat(row.getBillHosVat() + bc.getBillHosVat());
            row.setbDoc(row.getbDoc() + bc.getbDoc());
            row.setbDocVat(row.getbDocVat() + bc.getbDocVat());
            row.setCanHos(row.getCanHos() + bc.getCanHos());
            row.setCanHosVat(row.getCanHosVat() + bc.getCanHosVat());
            row.setCanDoc(row.getCanDoc() + bc.getCanDoc());
            row.setCanDocVat(row.getCanDocVat() + bc.getCanDocVat());
            row.setRefHos(row.getRefHos() + bc.getRefHos());
            row.setRefHosVat(row.getRefHosVat() + bc.getRefHosVat());
            row.setRefDoc(row.getRefDoc() + bc.getRefDoc());
            row.setRefDocVat(row.getRefDocVat() + bc.getRefDocVat());
        }
        bookingCountSummryRowsScan.add(row);
    }

    public void createSmmeryRows(List<BillType> bts, boolean sessionDate, FeeType ft) {
        for (BillType bt : bts) {
            BookingCountSummryRow row = new BookingCountSummryRow();
            if (ft == FeeType.Service) {
                row.setBookingType("Scan " + bt.getLabel());
                row.setBilledCount(countBillByBillTypeAndFeeType(new BilledBill(), ft, bt, sessionDate, paid));
                row.setCancelledCount(countBillByBillTypeAndFeeType(new CancelledBill(), ft, bt, sessionDate, paid));
                row.setRefundCount(countBillByBillTypeAndFeeType(new RefundBill(), ft, bt, sessionDate, paid));
            } else {
                row.setBookingType(bt.getLabel());
                row.setBilledCount(countBillByBillTypeAndFeeType(new BilledBill(), ft, bt, sessionDate, paid));
                row.setCancelledCount(countBillByBillTypeAndFeeType(new CancelledBill(), ft, bt, sessionDate, paid));
                row.setRefundCount(countBillByBillTypeAndFeeType(new RefundBill(), ft, bt, sessionDate, paid));
            }

            bookingCountSummryRows.add(row);

        }
    }

    public void createSmmeryRowsHospitalIncome(List<BillType> bts, boolean sessionDate, FeeType ft) {
//        String[] ftpForScan = {"Doctor Fee", "Hospital Fee"};
//        List<String> ftpsForScan = Arrays.asList(ftpForScan);
//
//        String[] ftpForOnlyHos = {"Doctor Fee", "Scan Fee"};
//        List<String> ftpsForForOnlyHos = Arrays.asList(ftpForOnlyHos);

        for (BillType bt : bts) {
            BookingCountSummryRow row = new BookingCountSummryRow();
            if (ft == FeeType.Service) {
                row.setBookingType("Scan " + bt.getLabel());
                row.setBilledCount(hospitalTotalBillByBillTypeAndFeeType(new BilledBill(), FeeType.Service, bt, sessionDate, paid));
                row.setCancelledCount(hospitalTotalBillByBillTypeAndFeeType(new CancelledBill(), FeeType.Service, bt, sessionDate, paid));
//                row.setRefundCount(hospitalTotalBillByBillTypeAndFeeType(new RefundBill(), FeeType.Service, bt, sessionDate, paid));
                row.setRefundCount(0);
            } else {
                row.setBookingType(bt.getLabel());
                row.setBilledCount(hospitalTotalBillByBillTypeAndFeeType(new BilledBill(), FeeType.OwnInstitution, bt, sessionDate, paid));
                row.setCancelledCount(hospitalTotalBillByBillTypeAndFeeType(new CancelledBill(), FeeType.OwnInstitution, bt, sessionDate, paid));
//                row.setRefundCount(hospitalTotalBillByBillTypeAndFeeType(new RefundBill(), FeeType.OwnInstitution, bt, sessionDate, paid));
                row.setRefundCount(0);
            }

            bookingCountSummryRows.add(row);

        }
    }

    public List<BookingCountSummryRow> createSmmeryRowsHospitalIncomeWithDoc(List<BillType> bts, boolean sessionDate, FeeType ft) {

        List<BookingCountSummryRow> bcsrs = new ArrayList<>();

        for (BillType bt : bts) {
            BookingCountSummryRow row = new BookingCountSummryRow();
            if (ft == FeeType.Service) {
                row.setBookingType("Scan " + bt.getLabel());
                double d[] = new double[4];
                d = hospitalTotalBillByBillTypeAndFeeTypeWithdocfee(new BilledBill(), FeeType.Service, bt, sessionDate, paid);
                row.setBillHos(d[0]);
                row.setBillHosVat(d[1]);
                row.setbDoc(d[2]);
                row.setbDocVat(d[3]);
                d = hospitalTotalBillByBillTypeAndFeeTypeWithdocfee(new CancelledBill(), FeeType.Service, bt, sessionDate, paid);
                row.setCanHos(d[0]);
                row.setCanHosVat(d[1]);
                row.setCanDoc(d[2]);
                row.setCanDocVat(d[3]);
                d = hospitalTotalBillByBillTypeAndFeeTypeWithdocfee(new RefundBill(), FeeType.Service, bt, sessionDate, paid);
                row.setRefHos(d[0]);
                row.setRefHosVat(d[1]);
                row.setRefDoc(d[2]);
                row.setRefDocVat(d[3]);
            } else {
                row.setBookingType(bt.getLabel());
                double d[] = new double[4];
                d = hospitalTotalBillByBillTypeAndFeeTypeWithdocfee(new BilledBill(), FeeType.OwnInstitution, bt, sessionDate, paid);
                row.setBillHos(d[0]);
                row.setBillHosVat(d[1]);
                row.setbDoc(d[2]);
                row.setbDocVat(d[3]);
                d = hospitalTotalBillByBillTypeAndFeeTypeWithdocfee(new CancelledBill(), FeeType.OwnInstitution, bt, sessionDate, paid);
                row.setCanHos(d[0]);
                row.setCanHosVat(d[1]);
                row.setCanDoc(d[2]);
                row.setCanDocVat(d[3]);
                d = hospitalTotalBillByBillTypeAndFeeTypeWithdocfee(new RefundBill(), FeeType.OwnInstitution, bt, sessionDate, paid);
                row.setRefHos(d[0]);
                row.setRefHosVat(d[1]);
                row.setRefDoc(d[2]);
                row.setRefDocVat(d[3]);
            }

            bcsrs.add(row);

        }
        return bcsrs;
    }

    public List<BillFee> getListBilledBillFees() {
        return listBilledBillFees;
    }

    public void setListBilledBillFees(List<BillFee> listBilledBillFees) {
        this.listBilledBillFees = listBilledBillFees;
    }

    public List<BillFee> getListCanceledBillFees() {
        return listCanceledBillFees;
    }

    public void setListCanceledBillFees(List<BillFee> listCanceledBillFees) {
        this.listCanceledBillFees = listCanceledBillFees;
    }

    public List<BillFee> getListRefundBillFees() {
        return listRefundBillFees;
    }

    public void setListRefundBillFees(List<BillFee> listRefundBillFees) {
        this.listRefundBillFees = listRefundBillFees;
    }

    public FeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }

    public List<FeetypeFee> getFeetypeFees() {
        return feetypeFees;
    }

    public void setFeetypeFees(List<FeetypeFee> feetypeFees) {
        this.feetypeFees = feetypeFees;
    }

    public List<BillFee> getBillFeeList() {
        if (billFeeList == null) {
            billFeeList = new ArrayList<>();
        }
        return billFeeList;
    }

    public void setBillFeeList(List<BillFee> billFeeList) {
        this.billFeeList = billFeeList;
    }

    public double calFeeTotal(Bill bill) {

        String sql;
        Map m = new HashMap();
        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelOnCall, BillType.ChannelStaff};
        List<BillType> bts = Arrays.asList(billTypes);

        FeeType[] feeTypes = {FeeType.Staff, FeeType.OwnInstitution, FeeType.OtherInstitution, FeeType.Service};
        List<FeeType> fts = Arrays.asList(feeTypes);

        sql = " select sum(bf.feeValue) from BillFee  bf where "
                + " bf.bill.retired=false "
                + " and bf.bill.singleBillSession.sessionDate between :fd and :td "
                + " and bf.bill.billType in :bt "
                + " and type(bf.bill)=:class "
                + " and bf.fee.feeType in :ft ";

        if (getWebUser() != null) {
            sql += " and bf.bill.creater=:wu ";
            m.put("wu", getWebUser());
        }

        sql += " order by bf.bill.insId ";

        m.put("fd", fromDate);
        m.put("td", toDate);
        m.put("class", bill.getClass());
        m.put("ft", fts);
        m.put("bt", bts);

        return getBillFeeFacade().findDoubleByJpql(sql, m, TemporalType.TIMESTAMP);
    }

    public double getBilledBillTotal() {
        return billedBillTotal;
    }

    public void setBilledBillTotal(double billedBillTotal) {
        this.billedBillTotal = billedBillTotal;
    }

    public double getCanceledBillTotl() {
        return canceledBillTotl;
    }

    public void setCanceledBillTotl(double canceledBillTotl) {
        this.canceledBillTotl = canceledBillTotl;
    }

    public double getRefundBillTotal() {
        return refundBillTotal;
    }

    public void setRefundBillTotal(double refundBillTotal) {
        this.refundBillTotal = refundBillTotal;
    }

    public List<BillFee> getBilledBillFeeList() {
        if (billedBillFeeList == null) {
            billedBillFeeList = new ArrayList<>();
        }
        return billedBillFeeList;
    }

    public void setBilledBillFeeList(List<BillFee> billedBillFeeList) {
        this.billedBillFeeList = billedBillFeeList;
    }

    public List<BillFee> getCancelledBillFeeList() {
        return cancelledBillFeeList;
    }

    public void setCancelledBillFeeList(List<BillFee> cancelledBillFeeList) {
        this.cancelledBillFeeList = cancelledBillFeeList;
    }

    public List<BillFee> getRefundBillFeeList() {
        return refundBillFeeList;
    }

    public void setRefundBillFeeList(List<BillFee> refundBillFeeList) {
        this.refundBillFeeList = refundBillFeeList;
    }

    public double getFinalCashTot() {
        return finalCashTot;
    }

    public void setFinalCashTot(double finalCashTot) {
        this.finalCashTot = finalCashTot;
    }

    public double getFinalAgentTot() {
        return finalAgentTot;
    }

    public void setFinalAgentTot(double finalAgentTot) {
        this.finalAgentTot = finalAgentTot;
    }

    public double getFinalCardTot() {
        return finalCardTot;
    }

    public void setFinalCardTot(double finalCardTot) {
        this.finalCardTot = finalCardTot;
    }

    public double getFinalChequeTot() {
        return finalChequeTot;
    }

    public void setFinalChequeTot(double finalChequeTot) {
        this.finalChequeTot = finalChequeTot;
    }

    public double getFinalSlipTot() {
        return finalSlipTot;
    }

    public void setFinalSlipTot(double finalSlipTot) {
        this.finalSlipTot = finalSlipTot;
    }

    public List<WebUserBillsTotal> getWebUserBillsTotals() {
        return webUserBillsTotals;
    }

    public void setWebUserBillsTotals(List<WebUserBillsTotal> webUserBillsTotals) {
        this.webUserBillsTotals = webUserBillsTotals;
    }

    public WebUserFacade getWebUserFacade() {
        return webUserFacade;
    }

    public void setWebUserFacade(WebUserFacade webUserFacade) {
        this.webUserFacade = webUserFacade;
    }

    public void setCashiers(List<WebUser> cashiers) {
        this.cashiers = cashiers;
    }

    public List<Department> getDeps() {
        return deps;
    }

    public void setDeps(List<Department> deps) {
        this.deps = deps;
    }

    public List<DepartmentBill> getDepBills() {
        return depBills;
    }

    public void setDepBills(List<DepartmentBill> depBills) {
        this.depBills = depBills;
    }

    public void channelBillListCreatedDate() {
        Date startTime = new Date();

        channelBillList(true);

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Bill report/Bill summery(Process Created Date)(/faces/channel/channel_report_all_booking.xhtml)");
    }

    public void channelBillListSessionDate() {
        Date startTime = new Date();

        channelBillList(false);
        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Bill report/Bill summery(Process Session Date)(/faces/channel/channel_report_all_booking.xhtml)");

    }

    public void channelBillList(boolean createdDate) {
        BillType[] billTypes = {BillType.ChannelAgent, BillType.ChannelCash, BillType.ChannelOnCall, BillType.ChannelStaff};
        List<BillType> bts = Arrays.asList(billTypes);
        HashMap hm = new HashMap();

        String sql = " select b from Bill b "
                + " where b.billType in :bt "
                + " and b.retired=false ";
        if (createdDate) {
            sql += " and b.createdAt between :fDate and :tDate ";
        } else {
            sql += " and b.singleBillSession.sessionDate between :fDate and :tDate ";
        }
        sql += " order by b.singleBillSession.sessionDate,b.singleBillSession.serviceSession.startingTime ";

        hm.put("bt", bts);
        hm.put("fDate", getFromDate());
        hm.put("tDate", getToDate());

        channelBills = billFacade.findBySQL(sql, hm, TemporalType.TIMESTAMP);

    }

    public void createChannelFees() {
        Date startTime = new Date();

        valueList = new ArrayList<>();
        FeeType[] fts = {FeeType.Staff, FeeType.OwnInstitution, FeeType.OtherInstitution, FeeType.Service};

        for (FeeType ft : fts) {
            setFeeTotals(valueList, ft);
        }
        calTotals(valueList);

        //System.out.println("***Done***");
        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Income buy feetype bu session date(/faces/channel/channel_report_by_fee_1.xhtml)");
    }

    public void setFeeTotals(List<String1Value3> s1v3s, FeeType feeType) {
        double totBill = 0.0;
        double totCan = 0.0;
        double totRef = 0.0;
        String1Value3 s1v3 = new String1Value3();
        totBill = getFeeTotal(new BilledBill(), feeType);
        totCan = getFeeTotal(new CancelledBill(), feeType);
        totRef = getFeeTotal(new RefundBill(), feeType);

        switch (feeType) {
            case Staff:
                s1v3.setString("Staff Fee");
                break;
            case OwnInstitution:
                s1v3.setString("Hospital Fee ");
                break;

            case OtherInstitution:
                s1v3.setString("Agency Fee");
                break;

            case Service:
                s1v3.setString("Scan Fee ");
                break;

        }
        s1v3.setValue1(totBill);
        s1v3.setValue2(totCan);
        s1v3.setValue3(totRef);

        //System.out.println("*************");
        //System.out.println("Fee - " + s1v3.getString());
        //System.out.println("Bill - " + s1v3.getValue1());
        //System.out.println("Can - " + s1v3.getValue2());
        //System.out.println("Ref - " + s1v3.getValue3());
        s1v3s.add(s1v3);
        //System.out.println("Add");
        //System.out.println("*************");
    }

    public double getFeeTotal(Bill bill, FeeType feeType) {

        String sql;
        Map m = new HashMap();
        BillType[] bts = {BillType.ChannelCash, BillType.ChannelPaid, BillType.ChannelStaff,};
        List<BillType> bt = Arrays.asList(bts);
        sql = " select sum(bf.feeValue) from BillFee  bf where "
                + " bf.bill.retired=false "
                + " and bf.bill.singleBillSession.sessionDate between :fd and :td "
                + " and bf.bill.billType in :bt "
                + " and type(bf.bill)=:class "
                + " and bf.fee.feeType=:ft ";

        m.put("fd", fromDate);
        m.put("td", toDate);
        m.put("class", bill.getClass());
        m.put("ft", feeType);
        m.put("bt", bt);

        return getBillFeeFacade().findDoubleByJpql(sql, m, TemporalType.TIMESTAMP);
    }

    public void calTotals(List<String1Value3> string1Value3s) {
        totalBilled = 0.0;
        totalCancel = 0.0;
        totalRefund = 0.0;
        for (String1Value3 s1v3 : string1Value3s) {
            totalBilled += s1v3.getValue1();
            totalCancel += s1v3.getValue2();
            totalRefund += s1v3.getValue3();
        }

    }

    public List<ChannelReportColumnModel> getChannelReportColumnModels() {
        return channelReportColumnModels;
    }

    public void setChannelReportColumnModels(List<ChannelReportColumnModel> channelReportColumnModels) {
        this.channelReportColumnModels = channelReportColumnModels;
    }

    public Date getFromDate() {
        if (fromDate == null) {
            fromDate = CommonFunctions.getStartOfDay(new Date());
        }
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        if (toDate == null) {
            toDate = CommonFunctions.getEndOfDay(toDate);
        }
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public ReportKeyWord getReportKeyWord() {
        if (reportKeyWord == null) {
            reportKeyWord = new ReportKeyWord();
        }
        return reportKeyWord;
    }

    public void setReportKeyWord(ReportKeyWord reportKeyWord) {
        this.reportKeyWord = reportKeyWord;
    }

    public void makeNull() {
        reportKeyWord = null;
        serviceSession = null;
        billSessions = null;
        valueList = null;
    }

    List<BillSession> nurseViewSessions;
    List<BillSession> doctorViewSessions;

    public List<BillSession> getNurseViewSessions() {
        return nurseViewSessions;
    }

    public void setNurseViewSessions(List<BillSession> nurseViewSessions) {
        this.nurseViewSessions = nurseViewSessions;
    }

    public List<BillSession> getDoctorViewSessions() {
        return doctorViewSessions;
    }

    public void setDoctorViewSessions(List<BillSession> doctorViewSessions) {
        this.doctorViewSessions = doctorViewSessions;
    }

    public void markAsAbsent() {
        if (selectedBillSessions == null) {
            UtilityController.addSuccessMessage("Please Select Sessions");
            return;
        }

        for (BillSession bs : selectedBillSessions) {
            System.out.println("bs = " + bs.isAbsent());
            bs.setAbsent(true);
            billSessionFacade.edit(bs);
            UtilityController.addSuccessMessage("Marked Succesful");
        }
    }

    public void fillNurseView() {
        nurseViewSessions = new ArrayList<>();
        if (bookingController.getSelectedServiceSession() == null) {
            UtilityController.addErrorMessage("Please Select Session");
            return;
        }
        System.out.println("bookingController.getSelectedServiceSession() = " + bookingController.getSelectedServiceSession());

        String sql = "Select bs From BillSession bs "
                + " where bs.retired=false and "
                + " type(bs.bill)=:class and "
                //+ " bs.bill.refunded=false and "
                + " bs.bill.billType in :tbs and "
                + " bs.serviceSession.id=" + bookingController.getSelectedServiceSession().getId() + " order by bs.serialNo";
        HashMap hh = new HashMap();
        hh.put("class", BilledBill.class);
        List<BillType> bts = new ArrayList<>();
        bts.add(BillType.ChannelAgent);
        bts.add(BillType.ChannelCash);
        bts.add(BillType.ChannelOnCall);
        bts.add(BillType.ChannelStaff);
        hh.put("tbs", bts);
        nurseViewSessions = getBillSessionFacade().findBySQL(sql, hh, TemporalType.TIMESTAMP);
        System.out.println("nurseViewSessions = " + nurseViewSessions);

    }

    public void fillDoctorView() {
        doctorViewSessions = new ArrayList<>();
        if (serviceSession != null) {
            String sql = "Select bs From BillSession bs "
                    + " where bs.retired=false and "
                    + " type(bs.bill)=:class and "
                    //                    + " bs.bill.cancelled=false and "
                    //                    + " bs.bill.refunded=false and "
                    + " bs.bill.billType in :tbs and "
                    + " bs.serviceSession.id=" + serviceSession.getId() + " and bs.sessionDate= :ssDate"
                    + " order by bs.serialNo";
            HashMap hh = new HashMap();
            hh.put("ssDate", serviceSession.getSessionDate());
            List<BillType> bts = new ArrayList<>();
            bts.add(BillType.ChannelAgent);
            bts.add(BillType.ChannelCash);
            bts.add(BillType.ChannelOnCall);
            bts.add(BillType.ChannelStaff);
            hh.put("tbs", bts);
            hh.put("class", BilledBill.class);
            doctorViewSessions = getBillSessionFacade().findBySQL(sql, hh, TemporalType.DATE);
            System.out.println("hh = " + hh);
            System.out.println("sql = " + sql);
            System.out.println("doctorViewSessions.size() = " + doctorViewSessions.size());
            netTotal = 0.0;
            grantNetTotal = 0.0;
            //Totals
            sql = "Select bs From BillSession bs "
                    + " where bs.retired=false and "
                    + " bs.bill.billType in :tbs and "
                    + " bs.serviceSession.id=" + serviceSession.getId() + " and bs.sessionDate= :ssDate"
                    + " order by bs.serialNo";
            HashMap h = new HashMap();
            h.put("ssDate", serviceSession.getSessionDate());
            List<BillType> bts2 = new ArrayList<>();
            bts2.add(BillType.ChannelAgent);
            bts2.add(BillType.ChannelCash);
            bts2.add(BillType.ChannelOnCall);
            bts2.add(BillType.ChannelStaff);
            h.put("tbs", bts2);
            List<BillSession> list = getBillSessionFacade().findBySQL(sql, h, TemporalType.DATE);

            for (BillSession bs : list) {
                System.out.println("bs.getBill().getDeptId() = " + bs.getBill().getDeptId());
                System.out.println("bs.isAbsent() = " + bs.isAbsent());
                System.out.println("bs.getBill().isRefunded() = " + bs.getBill().isRefunded());
                System.out.println("bs.getBill().isCancelled() = " + bs.getBill().isCancelled());
                if (bs.getBill().getBalance() == 0.0) {
                    if (!bs.getServiceSession().getOriginatingSession().isRefundable()) {
                        System.out.println("bs.getServiceSession().isRefundable() = " + bs.getServiceSession().isRefundable());
                        System.out.println("bs.getServiceSession().getOriginatingSession().isRefundable() = " + bs.getServiceSession().getOriginatingSession().isRefundable());
                        netTotal += bs.getBill().getStaffFee();
                        grantNetTotal += bs.getBill().getNetTotal();
                    } else if (bs.isAbsent() && !bs.getBill().isCancelled() && !bs.getBill().isRefunded()) {
//                        netTotal += bs.getBill().getStaffFee();
//                        grantNetTotal += bs.getBill().getNetTotal();
                    } else {
                        netTotal += bs.getBill().getStaffFee();
                        grantNetTotal += bs.getBill().getNetTotal();
                    }
                    System.out.println("netTotal = " + netTotal);
                    System.out.println("grantNetTotal = " + grantNetTotal);

                }
            }
        }
    }

    public List<BillSession> getBillSessionsNurse() {
        billSessions = new ArrayList<>();
        if (serviceSession != null) {
            String sql = "Select bs From BillSession bs "
                    + " where bs.retired=false and "
                    + " bs.bill.cancelled=false and "
                    + " bs.bill.refunded=false and "
                    + " bs.bill.billType in :tbs and "
                    + " bs.serviceSession.id=" + serviceSession.getId() + " and bs.sessionDate= :ssDate"
                    + " order by bs.serialNo";
            HashMap hh = new HashMap();
            hh.put("ssDate", serviceSession.getSessionDate());
            List<BillType> bts = new ArrayList<>();
            bts.add(BillType.ChannelAgent);
            bts.add(BillType.ChannelCash);
            bts.add(BillType.ChannelOnCall);
            hh.put("tbs", bts);
            billSessions = getBillSessionFacade().findBySQL(sql, hh, TemporalType.DATE);
        }
        return billSessions;
    }

    public List<ChannelDoctor> getTotalPatient() {

        channelDoctors = new ArrayList<>();

        String sql = "Select bs.bill From BillSession bs where bs.bill.staff is not null and bs.retired=false and bs.sessionDate= :ssDate";
        HashMap hh = new HashMap();
        hh.put("ssDate", Calendar.getInstance().getTime());
        List<Bill> bills = getBillFacade().findBySQL(sql, hh, TemporalType.DATE);

        Set<Staff> consultant = new HashSet();
        for (Bill b : bills) {
            consultant.add(b.getStaff());
        }

        for (Staff c : consultant) {
            ChannelDoctor cd = new ChannelDoctor();
            cd.setConsultant(c);
            channelDoctors.add(cd);
        }

        for (ChannelDoctor cd : channelDoctors) {

            for (Bill b : bills) {

                if (b.getStaff().getId() == cd.getConsultant().getId()) {

                    if (b.getBillType() == BillType.ChannelPaid && b.getReferenceBill() != null && b instanceof BilledBill) {
                        cd.setBillCount_c(cd.getBillCount_c() + 1);
                    } else if (b.getBillType() == BillType.ChannelPaid && b.getReferenceBill() != null && b instanceof CancelledBill) {
                        cd.setBillCanncelCount_c(cd.getBillCanncelCount_c() + 1);
                    } else if (b.getBillType() == BillType.ChannelPaid && b.getReferenceBill() != null && b instanceof RefundBill) {
                        cd.setRefundedCount_c(cd.getRefundedCount_c() + 1);
                    } else if (b.getBillType() == BillType.ChannelPaid && b instanceof BilledBill) {
                        cd.setBillCount(cd.getBillCount() + 1);
                    } else if (b.getBillType() == BillType.ChannelPaid && b instanceof CancelledBill) {
                        cd.setBillCanncelCount(cd.getBillCanncelCount() + 1);
                    } else if (b.getBillType() == BillType.ChannelPaid && b instanceof RefundBill) {
                        cd.setRefundedCount(cd.getRefundedCount() + 1);
                    } else if (b.getBillType() == BillType.ChannelCredit && b instanceof BilledBill) {
                        cd.setCreditCount(cd.getCreditCount() + 1);
                    } else if (b.getBillType() == BillType.ChannelCredit && b instanceof CancelledBill) {
                        cd.setCreditCancelledCount(cd.getCreditCancelledCount() + 1);
                    }

                    if ((b.getBillType() == BillType.ChannelPaid && b.getReferenceBill() == null && b instanceof BilledBill)
                            || (b.getBillType() == BillType.ChannelCredit && b instanceof BilledBill)) {
                        BillSession bs = getBillSessionFacade().findFirstBySQL("select b from BillSession b where b.retired=false and b.bill.id=" + b.getId());
                        if (bs.isAbsent()) {
                            cd.setAbsentCount(cd.getAbsentCount() + 1);
                        }
                    }
                }
            }

        }

        return channelDoctors;
    }

    public List<BillSession> getBillSessions() {
        return billSessions;
    }

    public List<ChannelDoctor> getTotalDoctor() {

        channelDoctors = new ArrayList<ChannelDoctor>();

        String sql = "Select bs.bill From BillSession bs where bs.bill.staff is not null and bs.retired=false and bs.sessionDate= :ssDate";
        HashMap hh = new HashMap();
        hh.put("ssDate", Calendar.getInstance().getTime());
        List<Bill> bills = getBillFacade().findBySQL(sql, hh, TemporalType.DATE);
        System.out.println("bills = " + bills);
        Set<Staff> consultant = new HashSet();
        for (Bill b : bills) {
            consultant.add(b.getStaff());
        }

        for (Staff c : consultant) {
            ChannelDoctor cd = new ChannelDoctor();
            cd.setConsultant(c);
            channelDoctors.add(cd);
        }

        for (ChannelDoctor cd : channelDoctors) {

            for (Bill b : bills) {
                if (b.getStaff().getId() == cd.getConsultant().getId()) {

                    if (b.getBillType() == BillType.ChannelPaid && b.getReferenceBill() != null && b instanceof BilledBill) {

                        cd.setBillCount_c(cd.getBillCount_c() + 1);
                        cd.setBillFee_c(cd.getBillFee_c() + getBillFees(b));

                    } else if (b.getBillType() == BillType.ChannelPaid && b.getReferenceBill() != null && b instanceof CancelledBill) {

                        cd.setBillCanncelCount_c(cd.getBillCanncelCount_c() + 1);
                        cd.setBillCanncelFee_c(cd.getBillCanncelFee_c() + getBillFees(b));

                    } else if (b.getBillType() == BillType.ChannelPaid && b.getReferenceBill() != null && b instanceof RefundBill) {

                        cd.setRefundedCount_c(cd.getRefundedCount_c() + 1);
                        cd.setRefundedFee_c(cd.getRefundedFee_c() + getBillFees(b));

                    } else if (b.getBillType() == BillType.ChannelPaid && b instanceof BilledBill) {

                        cd.setBillCount(cd.getBillCount() + 1);
                        cd.setBillFee(cd.getBillFee() + getBillFees(b));

                    } else if (b.getBillType() == BillType.ChannelPaid && b instanceof CancelledBill) {

                        cd.setBillCanncelCount(cd.getBillCanncelCount() + 1);
                        cd.setBillCanncelFee(cd.getBillCanncelFee() + getBillFees(b));

                    } else if (b.getBillType() == BillType.ChannelPaid && b instanceof RefundBill) {

                        cd.setRefundedCount(cd.getRefundedCount() + 1);
                        cd.setRefundFee(cd.getRefundFee() + getBillFees(b));

                    } else if (b.getBillType() == BillType.ChannelCredit && b instanceof BilledBill) {

                        cd.setCreditCount(cd.getCreditCount() + 1);
                        cd.setCreditFee(cd.getCreditFee() + getBillFees(b));

                    } else if (b.getBillType() == BillType.ChannelCredit && b instanceof CancelledBill) {

                        cd.setCreditCancelledCount(cd.getCreditCancelledCount() + 1);
                        cd.setCreditCancelFee(cd.getCreditCancelFee() + getBillFees(b));

                    }

                    if ((b.getBillType() == BillType.ChannelPaid && b.getReferenceBill() == null && b instanceof BilledBill)
                            || (b.getBillType() == BillType.ChannelCredit && b instanceof BilledBill)) {
                        BillSession bs = getBillSessionFacade().findFirstBySQL("select b from BillSession b where b.retired=false and b.bill.id=" + b.getId());
                        if (bs.isAbsent()) {
                            cd.setAbsentCount(cd.getAbsentCount() + 1);
                        }
                    }
                }
            }

        }

        calTotal();

        return channelDoctors;
    }

    public void createTotalDoctor() {

        channelDoctors = new ArrayList<ChannelDoctor>();
        HashMap m = new HashMap();
        String sql;
        sql = "Select bs.bill From BillSession bs "
                + " where bs.bill.staff is not null "
                + " and bs.retired=false "
                + " and bs.sessionDate= :ssDate "
                + " order by bs.bill.staff.person.name ";

        m.put("ssDate", Calendar.getInstance().getTime());
        List<Bill> bills = getBillFacade().findBySQL(sql, m, TemporalType.DATE);
        System.out.println("bills = " + bills.size());
        Set<Staff> consultant = new HashSet();
        for (Bill b : bills) {
            consultant.add(b.getStaff());
        }

        for (Staff c : consultant) {
            ChannelDoctor cd = new ChannelDoctor();
            cd.setConsultant(c);
            channelDoctors.add(cd);
        }

        for (ChannelDoctor cd : channelDoctors) {
            System.err.println("cd = " + cd.getConsultant().getPerson().getName());
            for (Bill b : bills) {
                System.out.println("b = " + b.getStaff().getPerson().getName());
                System.out.println("b = " + b.getBillClass());
                if (Objects.equals(b.getStaff().getId(), cd.getConsultant().getId())) {
                    if (b.getBillType() == BillType.ChannelCash || b.getBillType() == BillType.ChannelPaid) {
                        if (b instanceof BilledBill) {
                            cd.setBillCount(cd.getBillCount() + 1);
                            cd.setBillFee(cd.getBillFee() + getBillFees(b, FeeType.Staff));
                        } else if (b instanceof CancelledBill) {
                            cd.setBillCanncelCount(cd.getBillCanncelCount() + 1);
                            cd.setBillCanncelFee(cd.getBillCanncelFee() + getBillFees(b, FeeType.Staff));
                        } else if (b instanceof RefundBill) {
                            cd.setRefundedCount(cd.getRefundedCount() + 1);
                            cd.setRefundFee(cd.getRefundFee() + getBillFees(b, FeeType.Staff));
                        }
                    }
                }
            }

        }

        calTotal();

    }

    public void createTotalDoctor(Date date) {

        channelDoctors = new ArrayList<ChannelDoctor>();
        HashMap m = new HashMap();
        String sql;
        sql = "Select bs.bill From BillSession bs "
                + " where bs.bill.staff is not null "
                + " and bs.retired=false "
                + " and bs.sessionDate= :ssDate "
                + " order by bs.bill.staff.person.name ";

        m.put("ssDate", date);
        List<Bill> bills = getBillFacade().findBySQL(sql, m, TemporalType.DATE);
        System.out.println("bills = " + bills.size());
        Set<Staff> consultant = new HashSet();
        for (Bill b : bills) {
            consultant.add(b.getStaff());
        }

        for (Staff c : consultant) {
            ChannelDoctor cd = new ChannelDoctor();
            cd.setConsultant(c);
            channelDoctors.add(cd);
        }

        for (ChannelDoctor cd : channelDoctors) {
            System.err.println("cd = " + cd.getConsultant().getPerson().getName());
            for (Bill b : bills) {
                System.out.println("b = " + b.getStaff().getPerson().getName());
                System.out.println("b = " + b.getBillClass());
                if (Objects.equals(b.getStaff().getId(), cd.getConsultant().getId())) {
                    if (b.getBillType() == BillType.ChannelCash || b.getBillType() == BillType.ChannelPaid) {
                        if (b instanceof BilledBill) {
                            cd.setBillCount(cd.getBillCount() + 1);
                            cd.setBillFee(cd.getBillFee() + getBillFees(b, FeeType.Staff));
                        } else if (b instanceof CancelledBill) {
                            cd.setBillCanncelCount(cd.getBillCanncelCount() + 1);
                            cd.setBillCanncelFee(cd.getBillCanncelFee() + getBillFees(b, FeeType.Staff));
                        } else if (b instanceof RefundBill) {
                            cd.setRefundedCount(cd.getRefundedCount() + 1);
                            cd.setRefundFee(cd.getRefundFee() + getBillFees(b, FeeType.Staff));
                        }
                    }
                }
            }

        }

        calTotal();

    }

    public void createTotalDoctorAll() {
        Date startTime = new Date();

        channelDoctors = new ArrayList<ChannelDoctor>();
        HashMap m = new HashMap();
        String sql;
        sql = "Select distinct(bs.bill.staff) From BillSession bs "
                + " where bs.bill.staff is not null "
                + " and bs.retired=false "
                + " and bs.sessionDate between :fd and :td "
                + " order by bs.bill.staff.person.name ";

        m.put("fd", fromDate);
        m.put("td", toDate);

        List<Staff> staffs = staffFacade.findBySQL(sql, m, TemporalType.TIMESTAMP);
        System.out.println("staffs.size() = " + staffs.size());

        BillType[] types = {BillType.ChannelCash, BillType.ChannelPaid, BillType.ChannelAgent};

        for (Staff s : staffs) {
            System.out.println("s.getPerson().getName() = " + s.getPerson().getName());
            ChannelDoctor cd = new ChannelDoctor();
            cd.setConsultant(s);
            cd.setBillCount(fetchBillCount(s, fromDate, toDate, new BilledBill(), Arrays.asList(types)));
            cd.setBillCanncelCount(fetchBillCount(s, fromDate, toDate, new CancelledBill(), Arrays.asList(types)));
            cd.setRefundedCount(fetchBillCount(s, fromDate, toDate, new RefundBill(), Arrays.asList(types)));

            cd.setBillFee(fetchBillFees(new BilledBill(), FeeType.Staff, s, fromDate, toDate, Arrays.asList(types)));
            cd.setBillCanncelFee(fetchBillFees(new CancelledBill(), FeeType.Staff, s, fromDate, toDate, Arrays.asList(types)));
            cd.setRefundFee(fetchBillFees(new RefundBill(), FeeType.Staff, s, fromDate, toDate, Arrays.asList(types)));

            channelDoctors.add(cd);

        }

        calTotal();

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Consultant report/BTT(/faces/channel/channel_doctor_view_today_all.xhtml)");

    }

    public void createTodayAbsentList() {

        createTodayList(true, false);

    }

    public void createTodayCancelList() {

        createTodayList(false, true);

    }

    public void createTodayList(boolean absent, boolean cancel) {

        billSessions = new ArrayList<>();
        HashMap m = new HashMap();
        String sql;
        sql = "Select bs From BillSession bs "
                + " where bs.bill.staff is not null "
                + " and bs.retired=false "
                + " and type(bs.bill)=:class "
                + " and bs.bill.billType in :bts "
                + " and bs.sessionDate=:ssDate ";
        if (absent) {
            sql += " and bs.absent=true ";
        }
        if (cancel) {
            sql += " and bs.bill.cancelled=true ";
        }
        m.put("bts", Arrays.asList(new BillType[]{BillType.ChannelCash, BillType.ChannelPaid}));
        m.put("ssDate", getDate());
        m.put("class", BilledBill.class);
        billSessions = getBillSessionFacade().findBySQL(sql, m, TemporalType.DATE);
        System.out.println("m = " + m);
        System.out.println("billSessions = " + billSessions.size());
        calTotalBS(billSessions);

    }

    private void calTotal() {
        total = 0.0;
        for (ChannelDoctor cd : channelDoctors) {
            total += cd.getBillFee() + cd.getBillCanncelFee() + cd.getRefundFee();

        }

    }

    private void calTotalBS(List<BillSession> billSessions) {
        netTotal = 0.0;
        netTotalDoc = 0.0;
        for (BillSession bs : billSessions) {
            netTotal += bs.getBill().getNetTotal();
            netTotalDoc += bs.getBill().getStaffFee();
        }

    }

    private double getBillFees(Bill b) {
        String sql = "Select sum(b.feeValue) From BillFee b where b.retired=false and b.bill.id=" + b.getId();

        return getBillFeeFacade().findAggregateDbl(sql);
    }

    private double getBillFees(Bill b, FeeType ft) {
        Map m = new HashMap();
        String sql;

        sql = "Select sum(b.feeValue) From BillFee b "
                + " where b.retired=false "
                + " and b.fee.feeType=:ft "
                + " and b.bill.id=" + b.getId();

        m.put("ft", ft);

        return getBillFeeFacade().findDoubleByJpql(sql, m);
    }

    double fetchBillCount(Staff s, Date fd, Date td, Bill b, List<BillType> billTypes) {
        HashMap m = new HashMap();
        String sql;
        sql = "Select count(bs.bill) From BillSession bs "
                + " where bs.bill.staff=:s "
                + " and type(bs.bill)=:class "
                + " and bs.retired=false "
                + " and bs.sessionDate between :fd and :td "
                + " and bs.bill.billType in :bts ";

        m.put("fd", fd);
        m.put("td", td);
        m.put("s", s);
        m.put("class", b.getClass());
        m.put("bts", billTypes);
        System.out.println("sql = " + sql);
        System.out.println("m = " + m);
        double d = getBillFacade().findAggregateLong(sql, m, TemporalType.TIMESTAMP);
        System.out.println("d = " + d);

        return d;

    }

    double fetchBillFees(Bill b, FeeType ft, Staff s, Date fd, Date td, List<BillType> billTypes) {
        Map m = new HashMap();
        String sql;

        sql = "Select sum(bf.feeValue) From BillFee bf "
                + " where bf.retired=false "
                + " and bf.fee.feeType=:ft "
                + " and type(bf.bill)=:class "
                + " and bf.bill.staff=:s "
                + " and bf.bill.singleBillSession.sessionDate between :fd and :td "
                + " and bf.bill.billType in :bts ";

        m.put("ft", ft);
        m.put("class", b.getClass());
        m.put("fd", fd);
        m.put("td", td);
        m.put("s", s);
        m.put("bts", billTypes);
        System.out.println("sql = " + sql);
        System.out.println("m = " + m);
        double d = getBillFeeFacade().findDoubleByJpql(sql, m);
        System.out.println("d = " + d);

        return d;
    }

    public List<BillSession> getBillSessionsUser() {
        billSessions = new ArrayList<BillSession>();
        if (serviceSession != null) {
            String sql = "Select bs From BillSession bs where bs.retired=false and bs.serviceSession.id=" + serviceSession.getId() + " and bs.sessionDate= :ssDate";
            HashMap hh = new HashMap();
            hh.put("ssDate", serviceSession.getSessionAt());
            billSessions = getBillSessionFacade().findBySQL(sql, hh, TemporalType.DATE);

            for (BillSession bs : billSessions) {
                bs.setDoctorFee(getChannelBean().getChannelFee(bs, FeeType.Staff));
                bs.setTax(getChannelBean().getChannelFee(bs, FeeType.Tax));

                setBilledTotalFee(getBilledTotalFee() + bs.getDoctorFee().getBilledFee().getFeeValue());
                setRepayTotalFee(getRepayTotalFee() + bs.getDoctorFee().getPrevFee().getFeeValue());
                setTaxTotal(getTaxTotal() + bs.getTax().getBilledFee().getFeeValue());
            }
        }

        return billSessions;
    }

    public List<BillSession> getBillSessionsDoctor() {
        billedTotalFee = 0.0;
        repayTotalFee = 0.0;
        taxTotal = 0.0;
        billSessions = new ArrayList<BillSession>();
        if (serviceSession != null) {
            String sql = "Select bs From BillSession bs where bs.retired=false and bs.serviceSession.id=" + serviceSession.getId() + " and bs.sessionDate= :ssDate";
            HashMap hh = new HashMap();
            hh.put("ssDate", serviceSession.getSessionAt());
            billSessions = getBillSessionFacade().findBySQL(sql, hh, TemporalType.DATE);

            for (BillSession bs : billSessions) {
                bs.setDoctorFee(getChannelBean().getChannelFee(bs, FeeType.Staff));
                bs.setTax(getChannelBean().getChannelFee(bs, FeeType.Tax));

                setBilledTotalFee(getBilledTotalFee() + bs.getDoctorFee().getBilledFee().getFeeValue());
                setRepayTotalFee(getRepayTotalFee() + bs.getDoctorFee().getPrevFee().getFeeValue());
                setTaxTotal(getTaxTotal() + bs.getTax().getBilledFee().getFeeValue());
            }
        }

        return billSessions;
    }

    public List<BillSession> getBillSessionsDoctorToday() {
        billedTotalFee = 0.0;
        repayTotalFee = 0.0;
        taxTotal = 0.0;
        billSessions = new ArrayList<BillSession>();

        String sql = "Select bs From BillSession bs where bs.retired=false and bs.sessionDate= :ssDate";
        HashMap hh = new HashMap();
        hh.put("ssDate", Calendar.getInstance().getTime());
        billSessions = getBillSessionFacade().findBySQL(sql, hh, TemporalType.DATE);

        for (BillSession bs : billSessions) {
            bs.setDoctorFee(getChannelBean().getChannelFee(bs, FeeType.Staff));
            bs.setTax(getChannelBean().getChannelFee(bs, FeeType.Tax));

            setBilledTotalFee(getBilledTotalFee() + bs.getDoctorFee().getBilledFee().getFeeValue());
            setRepayTotalFee(getRepayTotalFee() + bs.getDoctorFee().getPrevFee().getFeeValue());
            setTaxTotal(getTaxTotal() + bs.getTax().getBilledFee().getFeeValue());
        }

        return billSessions;
    }

    public void createAgentHistoryTable() {
        Date startTime = new Date();
        agentHistorys = new ArrayList<>();

        agentHistorys = createAgentHistory(fromDate, toDate, institution, null);

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Agent Reports/Agent History(/faces/channel/channel_report_agent_history.xhtml)");

    }

    public void createAgentBookings() {
        Date startTime = new Date();

        HashMap m = new HashMap();

        String sql = " select b from Bill b "
                + " where b.retired=false ";
//                + " and type(b)=:class ";

        if (webUser != null) {
            sql += " and b.creater=:web ";
            m.put("web", webUser);
        }
        if (sessoinDate) {
            sql += " and b.singleBillSession.sessionDate between :fd and :td ";
        } else {
            sql += " and b.createdAt between :fd and :td ";
        }

        if (institution != null) {
            sql += " and b.creditCompany=:a ";
            m.put("a", institution);
        } else {
            sql += " and b.creditCompany is not null ";
        }
        sql += " and b.billType=:bt ";
        m.put("bt", BillType.ChannelAgent);

        sql += " order by b.creditCompany.name ";
//        m.put("class", BilledBill.class);
        m.put("fd", getFromDate());
        m.put("td", getToDate());

        channelBills = getBillFacade().findBySQL(sql, m, TemporalType.TIMESTAMP);
        for (Bill b : channelBills) {
            if (b.getPaymentMethod() == PaymentMethod.Cash && (b instanceof CancelledBill || b instanceof RefundBill)) {
                b.setStaffFee(0);
                b.setNetTotal(0);
            }
        }
        netTotal = calTotal(channelBills);
        netTotalDoc = calTotalDoc(channelBills);

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/New Channel report/All agent booking detail list(/faces/channel/channel_report_agent_channels.xhtml)");

    }

    public void createCollectingCentreHistoryTable() {
        Date startTime = new Date();
        agentHistorys = new ArrayList<>();
        HistoryType[] hts = {HistoryType.CollectingCentreBalanceUpdateBill, HistoryType.CollectingCentreDeposit, HistoryType.CollectingCentreDepositCancel, HistoryType.CollectingCentreBilling};
        List<HistoryType> historyTypes = Arrays.asList(hts);

        agentHistorys = createAgentHistory(fromDate, toDate, institution, historyTypes);

        commonController.printReportDetails(fromDate, toDate, startTime, "Payments/Receieve/Credit Company/OPD(/faces/store/store_report_transfer_receive_bill_item.xhtml)");

    }

    public void createAgentHistorySubTable() {
        Date startTime = new Date();
        if (institution == null) {
            JsfUtil.addErrorMessage("Please Select Agency.");
            return;
        }
        HistoryType[] ht = {HistoryType.ChannelBooking, HistoryType.ChannelDeposit, HistoryType.ChannelDepositCancel, HistoryType.ChannelDebitNote,
            HistoryType.ChannelDebitNoteCancel, HistoryType.ChannelCreditNote, HistoryType.ChannelCreditNoteCancel};
        List<HistoryType> historyTypes = Arrays.asList(ht);

        agentHistoryWithDate = new ArrayList<>();
        Date nowDate = getFromDate();

        while (nowDate.before(getToDate())) {
            Date fd = commonFunctions.getStartOfDay(nowDate);
            Date td = commonFunctions.getEndOfDay(nowDate);
            System.out.println("td = " + td);
            System.out.println("fd = " + fd);
            System.out.println("nowDate = " + nowDate);
            AgentHistoryWithDate ahwd = new AgentHistoryWithDate();
            if (createAgentHistory(fd, td, institution, historyTypes).size() > 0) {
                ahwd.setDate(nowDate);
                ahwd.setAhs(createAgentHistory(fd, td, institution, historyTypes));
                agentHistoryWithDate.add(ahwd);
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(nowDate);
            cal.add(Calendar.DATE, 1);
            nowDate = cal.getTime();
        }

        commonController.printReportDetails(fromDate, toDate, startTime, "Channeling/Reports/Income report/Agent Reports/Agent statement(/faces/channel/channel_report_agent_history_1.xhtml)");
    }

    public void createCollectingCenterHistorySubTable() {
        Date startTime = new Date();

        if (institution == null) {
            JsfUtil.addErrorMessage("Please Select Agency.");
            return;
        }
        HistoryType[] ht = {HistoryType.CollectingCentreBalanceUpdateBill,
            HistoryType.CollectingCentreDeposit,
            HistoryType.CollectingCentreDepositCancel,
            HistoryType.CollectingCentreBilling};
        List<HistoryType> historyTypes = Arrays.asList(ht);

        agentHistoryWithDate = new ArrayList<>();
        Date nowDate = getFromDate();

        while (nowDate.before(getToDate())) {
            Date fd = commonFunctions.getStartOfDay(nowDate);
            Date td = commonFunctions.getEndOfDay(nowDate);
            System.out.println("td = " + td);
            System.out.println("fd = " + fd);
            System.out.println("nowDate = " + nowDate);
            AgentHistoryWithDate ahwd = new AgentHistoryWithDate();
            if (createAgentHistory(fd, td, institution, historyTypes).size() > 0) {
                ahwd.setDate(nowDate);
                ahwd.setAhs(createAgentHistory(fd, td, institution, historyTypes));
                agentHistoryWithDate.add(ahwd);
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(nowDate);
            cal.add(Calendar.DATE, 1);
            nowDate = cal.getTime();
        }

        commonController.printReportDetails(fromDate, toDate, startTime, "Payments/Book issuing/Collecting center booki issuing/Collecting center statements(/faces/reportLab/collecting_center_report_history_1.xhtml)");
    }

    public List<AgentHistory> createAgentHistory(Date fd, Date td, Institution i, List<HistoryType> hts) {
        String sql;
        Map m = new HashMap();

        sql = " select ah from AgentHistory ah where ah.retired=false "
                + " and ah.bill.retired=false "
                + " and ah.createdAt between :fd and :td ";

        if (i != null) {
            sql += " and (ah.bill.fromInstitution=:ins"
                    + " or ah.bill.creditCompany=:ins) ";

            m.put("ins", i);
        }

        if (hts != null) {
            sql += " and ah.historyType in :hts ";

            m.put("hts", hts);
        }

        m.put("fd", fd);
        m.put("td", td);

        sql += " order by ah.createdAt ";

        System.out.println("m = " + m);
        System.out.println("sql = " + sql);
        System.out.println("getAgentHistoryFacade().findBySQL(sql, m, TemporalType.TIMESTAMP).size() = " + getAgentHistoryFacade().findBySQL(sql, m, TemporalType.TIMESTAMP).size());

        return getAgentHistoryFacade().findBySQL(sql, m, TemporalType.TIMESTAMP);

    }

    /**
     * Creates a new instance of ChannelReportController
     */
    public List<Bill> getBilledBills() {
        return billedBills;
    }

    public void setBilledBills(List<Bill> billedBills) {
        this.billedBills = billedBills;
    }

    public List<Bill> getCancelBills() {
        return cancelBills;
    }

    public void setCancelBills(List<Bill> cancelBills) {
        this.cancelBills = cancelBills;
    }

    public List<Bill> getRefundBills() {
        return refundBills;
    }

    public void setRefundBills(List<Bill> refundBills) {
        this.refundBills = refundBills;
    }

    public ChannelReportController() {
    }

    public ServiceSession getServiceSession() {
        return serviceSession;
    }

    public void setServiceSession(ServiceSession serviceSession) {
        this.serviceSession = serviceSession;
    }

    public void setBillSessions(List<BillSession> billSessions) {
        this.billSessions = billSessions;
    }

    public BillSessionFacade getBillSessionFacade() {
        return billSessionFacade;
    }

    public void setBillSessionFacade(BillSessionFacade billSessionFacade) {
        this.billSessionFacade = billSessionFacade;
    }

    public BillFeeFacade getBillFeeFacade() {
        return billFeeFacade;
    }

    public void setBillFeeFacade(BillFeeFacade billFeeFacade) {
        this.billFeeFacade = billFeeFacade;
    }

    public double getBilledTotalFee() {
        return billedTotalFee;
    }

    public void setBilledTotalFee(double billedTotalFee) {
        this.billedTotalFee = billedTotalFee;
    }

    public double getRepayTotalFee() {
        return repayTotalFee;
    }

    public void setRepayTotalFee(double repayTotalFee) {
        this.repayTotalFee = repayTotalFee;
    }

    public double getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(double taxTotal) {
        this.taxTotal = taxTotal;
    }

    public BillsTotals getBilledBillList() {
        if (billedBillList == null) {
            billedBillList = new BillsTotals();
        }
        return billedBillList;
    }

    public void setBilledBillList(BillsTotals billedBillList) {
        this.billedBillList = billedBillList;
    }

    public BillsTotals getCanceledBillList() {
        if (canceledBillList == null) {
            canceledBillList = new BillsTotals();
        }
        return canceledBillList;
    }

    public void setCanceledBillList(BillsTotals canceledBillList) {
        this.canceledBillList = canceledBillList;
    }

    public BillsTotals getRefundBillList() {
        if (refundBillList == null) {
            refundBillList = new BillsTotals();
        }
        return refundBillList;
    }

    public void setRefundBillList(BillsTotals refundBillList) {
        this.refundBillList = refundBillList;
    }

    public List<ChannelDoctor> getChannelDoctors() {
        return channelDoctors;
    }

    public void setChannelDoctors(List<ChannelDoctor> channelDoctors) {
        this.channelDoctors = channelDoctors;
    }

    public BillFacade getBillFacade() {
        return billFacade;
    }

    public void setBillFacade(BillFacade billFacade) {
        this.billFacade = billFacade;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ChannelBean getChannelBean() {
        return channelBean;
    }

    public void setChannelBean(ChannelBean channelBean) {
        this.channelBean = channelBean;
    }

    public List<BillSession> getBillSessionsBilled() {
        return billSessionsBilled;
    }

    public void setBillSessionsBilled(List<BillSession> billSessionsBilled) {
        this.billSessionsBilled = billSessionsBilled;
    }

    public List<BillSession> getBillSessionsReturn() {
        return billSessionsReturn;
    }

    public void setBillSessionsReturn(List<BillSession> billSessionsReturn) {
        this.billSessionsReturn = billSessionsReturn;
    }

    public List<BillSession> getBillSessionsCancelled() {
        return billSessionsCancelled;
    }

    public void setBillSessionsCancelled(List<BillSession> billSessionsCancelled) {
        this.billSessionsCancelled = billSessionsCancelled;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public List<String1Value3> getValueList() {
        if (valueList == null) {
            valueList = new ArrayList<>();
        }
        return valueList;
    }

    public void setValueList(List<String1Value3> valueList) {
        this.valueList = valueList;
    }

    public double getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(double netTotal) {
        this.netTotal = netTotal;
    }

    public double getCancelTotal() {
        return cancelTotal;
    }

    public List<Bill> getChannelBills() {
        return channelBills;
    }

    public void setChannelBills(List<Bill> channelBills) {
        this.channelBills = channelBills;
    }

    public void setCancelTotal(double cancelTotal) {
        this.cancelTotal = cancelTotal;
    }

    public double getRefundTotal() {
        return refundTotal;
    }

    public void setRefundTotal(double refundTotal) {
        this.refundTotal = refundTotal;
    }

    public double getTotalBilled() {
        return totalBilled;
    }

    public void setTotalBilled(double totalBilled) {
        this.totalBilled = totalBilled;
    }

    public double getTotalCancel() {
        return totalCancel;
    }

    public void setTotalCancel(double totalCancel) {
        this.totalCancel = totalCancel;
    }

    public double getTotalRefund() {
        return totalRefund;
    }

    public void setTotalRefund(double totalRefund) {
        this.totalRefund = totalRefund;
    }

    public DepartmentFacade getDepartmentFacade() {
        return departmentFacade;
    }

    public void setDepartmentFacade(DepartmentFacade departmentFacade) {
        this.departmentFacade = departmentFacade;
    }

    List<ChannelReportColumnModel> columns;

    public List<ChannelReportColumnModel> getColumns() {
        return columns;
    }

    public void setColumns(List<ChannelReportColumnModel> columns) {
        this.columns = columns;
    }

    public List<AgentHistory> getAgentHistorys() {
        return agentHistorys;
    }

    public void setAgentHistorys(List<AgentHistory> agentHistorys) {
        this.agentHistorys = agentHistorys;
    }

    public AgentHistoryFacade getAgentHistoryFacade() {
        return agentHistoryFacade;
    }

    public void setAgentHistoryFacade(AgentHistoryFacade agentHistoryFacade) {
        this.agentHistoryFacade = agentHistoryFacade;
    }

    public List<AgentHistoryWithDate> getAgentHistoryWithDate() {
        return agentHistoryWithDate;
    }

    public void setAgentHistoryWithDate(List<AgentHistoryWithDate> agentHistoryWithDate) {
        this.agentHistoryWithDate = agentHistoryWithDate;
    }

    public List<BookingCountSummryRow> getBookingCountSummryRows() {
        return bookingCountSummryRows;
    }

    public void setBookingCountSummryRows(List<BookingCountSummryRow> bookingCountSummryRows) {
        this.bookingCountSummryRows = bookingCountSummryRows;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isSessoinDate() {
        return sessoinDate;
    }

    public void setSessoinDate(boolean sessoinDate) {
        this.sessoinDate = sessoinDate;
    }

    public boolean isWithDates() {
        return withDates;
    }

    public void setWithDates(boolean withDates) {
        this.withDates = withDates;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isAgncyOnCall() {
        return agncyOnCall;
    }

    public void setAgncyOnCall(boolean agncyOnCall) {
        this.agncyOnCall = agncyOnCall;
    }

    public double getTotalRefundDoc() {
        return totalRefundDoc;
    }

    public void setTotalRefundDoc(double totalRefundDoc) {
        this.totalRefundDoc = totalRefundDoc;
    }

    public double getTotalBilledDoc() {
        return totalBilledDoc;
    }

    public void setTotalBilledDoc(double totalBilledDoc) {
        this.totalBilledDoc = totalBilledDoc;
    }

    public double getTotalCancelDoc() {
        return totalCancelDoc;
    }

    public void setTotalCancelDoc(double totalCancelDoc) {
        this.totalCancelDoc = totalCancelDoc;
    }

    public double getNetTotalDoc() {
        return netTotalDoc;
    }

    public void setNetTotalDoc(double netTotalDoc) {
        this.netTotalDoc = netTotalDoc;
    }

    public Date getDate() {
        if (date == null) {
            date = new Date();
        }
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public class ChannelReportColumnModelBundle implements Serializable {

        List<ChannelReportColumnModel> rows;
        ChannelReportColumnModel bundle;

        public ChannelReportColumnModelBundle() {
        }

        public List<ChannelReportColumnModel> getRows() {
            return rows;
        }

        public void setRows(List<ChannelReportColumnModel> rows) {
            this.rows = rows;
        }

        public ChannelReportColumnModel getBundle() {
            return bundle;
        }

        public void setBundle(ChannelReportColumnModel bundle) {
            this.bundle = bundle;
        }

    }

    public class ChannelReportColumnModel implements Serializable {

        Bill bill;
        List<Bill> bills;
        BillItem billItem;
        PaymentMethod paymentMethod;
        BillType billType;
        double value;
        double doctorFee;
        double hospitalFee;
        double scanFee;
        double tax;
        double agencyFee;
        String name;
        String comments;
        int intNo;
        double cashTotal;
        double agentTotal;
        double chequeTotal;
        double slipTotal;
        double creditTotal;
        double cardTotal;
        double staffTotal;
        double onCallTotal;
        double collectionTotal;
        //for channell cashier report
        double billedTotal;
        double refundTotal;
        double cancellTotal;
        double total;

        public ChannelReportColumnModel() {
        }

        public List<Bill> getBills() {
            return bills;
        }

        public void setBills(List<Bill> bills) {
            this.bills = bills;
        }

        public double getCollectionTotal() {
            return collectionTotal;
        }

        public void setCollectionTotal(double collectionTotal) {
            this.collectionTotal = collectionTotal;
        }

        public double getCashTotal() {
            return cashTotal;
        }

        public void setCashTotal(double cashTotal) {
            this.cashTotal = cashTotal;
        }

        public double getAgentTotal() {
            return agentTotal;
        }

        public void setAgentTotal(double agentTotal) {
            this.agentTotal = agentTotal;
        }

        public double getChequeTotal() {
            return chequeTotal;
        }

        public void setChequeTotal(double chequeTotal) {
            this.chequeTotal = chequeTotal;
        }

        public double getSlipTotal() {
            return slipTotal;
        }

        public void setSlipTotal(double slipTotal) {
            this.slipTotal = slipTotal;
        }

        public double getCreditTotal() {
            return creditTotal;
        }

        public void setCreditTotal(double creditTotal) {
            this.creditTotal = creditTotal;
        }

        public double getCardTotal() {
            return cardTotal;
        }

        public void setCardTotal(double cardTotal) {
            this.cardTotal = cardTotal;
        }

        public double getStaffTotal() {
            return staffTotal;
        }

        public void setStaffTotal(double staffTotal) {
            this.staffTotal = staffTotal;
        }

        public double getOnCallTotal() {
            return onCallTotal;
        }

        public void setOnCallTotal(double onCallTotal) {
            this.onCallTotal = onCallTotal;
        }

        public double getBilledTotal() {
            return billedTotal;
        }

        public void setBilledTotal(double billedTotal) {
            this.billedTotal = billedTotal;
        }

        public double getRefundTotal() {
            return refundTotal;
        }

        public void setRefundTotal(double refundTotal) {
            this.refundTotal = refundTotal;
        }

        public double getCancellTotal() {
            return cancellTotal;
        }

        public void setCancellTotal(double cancellTotal) {
            this.cancellTotal = cancellTotal;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public int getIntNo() {
            return intNo;
        }

        public void setIntNo(int intNo) {
            this.intNo = intNo;
        }

        public Bill getBill() {
            return bill;
        }

        public void setBill(Bill bill) {
            this.bill = bill;
        }

        public BillItem getBillItem() {
            return billItem;
        }

        public void setBillItem(BillItem billItem) {
            this.billItem = billItem;
        }

        public PaymentMethod getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public BillType getBillType() {
            return billType;
        }

        public void setBillType(BillType billType) {
            this.billType = billType;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public double getDoctorFee() {
            return doctorFee;
        }

        public void setDoctorFee(double doctorFee) {
            this.doctorFee = doctorFee;
        }

        public double getHospitalFee() {
            return hospitalFee;
        }

        public void setHospitalFee(double hospitalFee) {
            this.hospitalFee = hospitalFee;
        }

        public double getScanFee() {
            return scanFee;
        }

        public void setScanFee(double scanFee) {
            this.scanFee = scanFee;
        }

        public double getTax() {
            return tax;
        }

        public void setTax(double tax) {
            this.tax = tax;
        }

        public double getAgencyFee() {
            return agencyFee;
        }

        public void setAgencyFee(double agencyFee) {
            this.agencyFee = agencyFee;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

//        private void calTot() {
//            cashTotal = staffTotal = onCallTotal = agentTotal = 0.0;
//            for (Bill b : bills) {
//                if (b.getPaymentMethod() == PaymentMethod.Cash) {
//                    setCashTotal(getCashTotal() + b.getNetTotal());
//                } else if (b.getPaymentMethod() == PaymentMethod.OnCall) {
//                    setOnCallTotal(getOnCallTotal() + b.getNetTotal());
//                } else if (b.getPaymentMethod() == PaymentMethod.Agent) {
//                    setAgentTotal(getAgentTotal() + b.getNetTotal());
//                } else if (b.getPaymentMethod() == PaymentMethod.Staff) {
//                    setStaffTotal(getStaffTotal() + b.getNetTotal());
//                }
//
//            }
//
//        }
    }

    public class ChannelBillTotals {

        String name;
        List<Bill> bills;
        double cash;
        double agent;
        double staff;
        double onCall;
        double total;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Bill> getBills() {
            return bills;
        }

        public void setBills(List<Bill> bills) {
            this.bills = bills;
        }

        public double getCash() {
            return cash;
        }

        public void setCash(double cash) {
            this.cash = cash;
        }

        public double getAgent() {
            return agent;
        }

        public void setAgent(double agent) {
            this.agent = agent;
        }

        public double getStaff() {
            return staff;
        }

        public void setStaff(double staff) {
            this.staff = staff;
        }

        public double getOnCall() {
            return onCall;
        }

        public void setOnCall(double onCall) {
            this.onCall = onCall;
        }

    }

    public class DepartmentBill {

        Department billDepartment;
        List<Bill> bills;
        double departmentBillTotal;

        public Department getBillDepartment() {
            return billDepartment;
        }

        public void setBillDepartment(Department billDepartment) {
            this.billDepartment = billDepartment;
        }

        public List<Bill> getBills() {
            return bills;
        }

        public void setBills(List<Bill> bills) {
            this.bills = bills;
        }

        public double getDepartmentBillTotal() {
            return departmentBillTotal;
        }

        public void setDepartmentBillTotal(double departmentBillTotal) {
            this.departmentBillTotal = departmentBillTotal;
        }

    }

    public class FeetypeFee {

        List<BillFee> billedBillFees;
        List<BillFee> canceledBillFees;
        List<BillFee> refunBillFees;
        FeeType feeType;
        double billedBillFeeTypeTotal;
        double canceledBillFeeTypeTotal;
        double refundBillFeeTypeTotal;

        public List<BillFee> getBilledBillFees() {
            return billedBillFees;
        }

        public void setBilledBillFees(List<BillFee> billedBillFees) {
            this.billedBillFees = billedBillFees;
        }

        public List<BillFee> getCanceledBillFees() {
            return canceledBillFees;
        }

        public void setCanceledBillFees(List<BillFee> canceledBillFees) {
            this.canceledBillFees = canceledBillFees;
        }

        public List<BillFee> getRefunBillFees() {
            return refunBillFees;
        }

        public void setRefunBillFees(List<BillFee> refunBillFees) {
            this.refunBillFees = refunBillFees;
        }

        public FeeType getFeeType() {
            return feeType;
        }

        public void setFeeType(FeeType feeType) {
            this.feeType = feeType;
        }

        public double getBilledBillFeeTypeTotal() {
            return billedBillFeeTypeTotal;
        }

        public void setBilledBillFeeTypeTotal(double billedBillFeeTypeTotal) {
            this.billedBillFeeTypeTotal = billedBillFeeTypeTotal;
        }

        public double getCanceledBillFeeTypeTotal() {
            return canceledBillFeeTypeTotal;
        }

        public void setCanceledBillFeeTypeTotal(double canceledBillFeeTypeTotal) {
            this.canceledBillFeeTypeTotal = canceledBillFeeTypeTotal;
        }

        public double getRefundBillFeeTypeTotal() {
            return refundBillFeeTypeTotal;
        }

        public void setRefundBillFeeTypeTotal(double refundBillFeeTypeTotal) {
            this.refundBillFeeTypeTotal = refundBillFeeTypeTotal;
        }

    }

    public class AgentHistoryWithDate {

        Date date;
        List<AgentHistory> ahs;

        public AgentHistoryWithDate() {
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public List<AgentHistory> getAhs() {
            return ahs;
        }

        public void setAhs(List<AgentHistory> ahs) {
            this.ahs = ahs;
        }
    }

    public class BookingCountSummryRow {

        String bookingType;
        Staff consultant;
        double billedCount;
        double cancelledCount;
        double refundCount;
        double cashCount;
        double agentCount;
        double oncallCount;
        double staffCount;
        double bookingCount;

        double billHos;
        double billHosVat;
        double billDoc;
        double billDocVat;
        double canHos;
        double canHosVat;
        double canDoc;
        double canDocVat;
        double refHos;
        double refHosVat;
        double refDoc;
        double refDocVat;

        public String getBookingType() {
            return bookingType;
        }

        public void setBookingType(String bookingType) {
            this.bookingType = bookingType;
        }

        public double getBilledCount() {
            return billedCount;
        }

        public void setBilledCount(double billedCount) {
            this.billedCount = billedCount;
        }

        public double getCancelledCount() {
            return cancelledCount;
        }

        public void setCancelledCount(double cancelledCount) {
            this.cancelledCount = cancelledCount;
        }

        public double getRefundCount() {
            return refundCount;
        }

        public void setRefundCount(double refundCount) {
            this.refundCount = refundCount;
        }

        public double getCashCount() {
            return cashCount;
        }

        public void setCashCount(double cashCount) {
            this.cashCount = cashCount;
        }

        public double getAgentCount() {
            return agentCount;
        }

        public void setAgentCount(double agentCount) {
            this.agentCount = agentCount;
        }

        public double getOncallCount() {
            return oncallCount;
        }

        public void setOncallCount(double oncallCount) {
            this.oncallCount = oncallCount;
        }

        public double getStaffCount() {
            return staffCount;
        }

        public void setStaffCount(double staffCount) {
            this.staffCount = staffCount;
        }

        public double getBookingCount() {
            return bookingCount;
        }

        public void setBookingCount(double bookingCount) {
            this.bookingCount = bookingCount;
        }

        public Staff getConsultant() {
            return consultant;
        }

        public void setConsultant(Staff consultant) {
            this.consultant = consultant;
        }

        public double getBillHos() {
            return billHos;
        }

        public void setBillHos(double billHos) {
            this.billHos = billHos;
        }

        public double getBillHosVat() {
            return billHosVat;
        }

        public void setBillHosVat(double billHosVat) {
            this.billHosVat = billHosVat;
        }

        public double getbDoc() {
            return billDoc;
        }

        public void setbDoc(double bDoc) {
            this.billDoc = bDoc;
        }

        public double getbDocVat() {
            return billDocVat;
        }

        public void setbDocVat(double bDocVat) {
            this.billDocVat = bDocVat;
        }

        public double getCanHos() {
            return canHos;
        }

        public void setCanHos(double canHos) {
            this.canHos = canHos;
        }

        public double getCanHosVat() {
            return canHosVat;
        }

        public void setCanHosVat(double canHosVat) {
            this.canHosVat = canHosVat;
        }

        public double getCanDoc() {
            return canDoc;
        }

        public void setCanDoc(double canDoc) {
            this.canDoc = canDoc;
        }

        public double getCanDocVat() {
            return canDocVat;
        }

        public void setCanDocVat(double canDocVat) {
            this.canDocVat = canDocVat;
        }

        public double getRefHos() {
            return refHos;
        }

        public void setRefHos(double refHos) {
            this.refHos = refHos;
        }

        public double getRefHosVat() {
            return refHosVat;
        }

        public void setRefHosVat(double refHosVat) {
            this.refHosVat = refHosVat;
        }

        public double getRefDoc() {
            return refDoc;
        }

        public void setRefDoc(double refDoc) {
            this.refDoc = refDoc;
        }

        public double getRefDocVat() {
            return refDocVat;
        }

        public void setRefDocVat(double refDocVat) {
            this.refDocVat = refDocVat;
        }

    }

    public class DoctorPaymentSummeryRowSub {

        Date date;
        List<Bill> bills;
        ServiceSession serviceSession;
        List<ServiceSession> serviceSessions;
        Staff consultant;
        double hospitalFeeTotal;
        double staffFeeTotal;

        double cashCount;
        double onCallCount;
        double staffCount;
        double agentCount;
        double totalCount;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public List<Bill> getBills() {
            return bills;
        }

        public void setBills(List<Bill> bills) {
            this.bills = bills;
        }

        public ServiceSession getServiceSession() {
            return serviceSession;
        }

        public void setServiceSession(ServiceSession serviceSession) {
            this.serviceSession = serviceSession;
        }

        public List<ServiceSession> getServiceSessions() {
            return serviceSessions;
        }

        public void setServiceSessions(List<ServiceSession> serviceSessions) {
            this.serviceSessions = serviceSessions;
        }

        public Staff getConsultant() {
            return consultant;
        }

        public void setConsultant(Staff consultant) {
            this.consultant = consultant;
        }

        public double getHospitalFeeTotal() {
            return hospitalFeeTotal;
        }

        public void setHospitalFeeTotal(double hospitalFeeTotal) {
            this.hospitalFeeTotal = hospitalFeeTotal;
        }

        public double getStaffFeeTotal() {
            return staffFeeTotal;
        }

        public void setStaffFeeTotal(double staffFeeTotal) {
            this.staffFeeTotal = staffFeeTotal;
        }

        public double getCashCount() {
            return cashCount;
        }

        public void setCashCount(double cashCount) {
            this.cashCount = cashCount;
        }

        public double getOnCallCount() {
            return onCallCount;
        }

        public void setOnCallCount(double onCallCount) {
            this.onCallCount = onCallCount;
        }

        public double getStaffCount() {
            return staffCount;
        }

        public void setStaffCount(double staffCount) {
            this.staffCount = staffCount;
        }

        public double getAgentCount() {
            return agentCount;
        }

        public void setAgentCount(double agentCount) {
            this.agentCount = agentCount;
        }

        public double getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(double totalCount) {
            this.totalCount = totalCount;
        }

    }

    public class DoctorPaymentSummeryRow {

        Staff consultant;
        List<DoctorPaymentSummeryRowSub> doctorPaymentSummeryRowSubs;
        Date date;

        public Staff getConsultant() {
            return consultant;
        }

        public void setConsultant(Staff consultant) {
            this.consultant = consultant;
        }

        public List<DoctorPaymentSummeryRowSub> getDoctorPaymentSummeryRowSubs() {
            return doctorPaymentSummeryRowSubs;
        }

        public void setDoctorPaymentSummeryRowSubs(List<DoctorPaymentSummeryRowSub> doctorPaymentSummeryRowSubs) {
            this.doctorPaymentSummeryRowSubs = doctorPaymentSummeryRowSubs;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

    }

    public class ChannelTotal {

        double hosFee;
        double staffFee;
        double Vat;
        double discount;
        double netTotal;

        public double getNetTotal() {
            return netTotal;
        }

        public void setNetTotal(double netTotal) {
            this.netTotal = netTotal;
        }

        public double getHosFee() {
            return hosFee;
        }

        public void setHosFee(double hosFee) {
            this.hosFee = hosFee;
        }

        public double getStaffFee() {
            return staffFee;
        }

        public void setStaffFee(double staffFee) {
            this.staffFee = staffFee;
        }

        public double getVat() {
            return Vat;
        }

        public void setVat(double Vat) {
            this.Vat = Vat;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

    }

    public CommonController getCommonController() {
        return commonController;
    }

    public void setCommonController(CommonController commonController) {
        this.commonController = commonController;
    }

    public ChannelTotal getChannelTotal() {
        return channelTotal;
    }

    public void setChannelTotal(ChannelTotal channelTotal) {
        this.channelTotal = channelTotal;
    }

    public List<BookingCountSummryRow> getBookingCountSummryRowsScan() {
        return bookingCountSummryRowsScan;
    }

    public void setBookingCountSummryRowsScan(List<BookingCountSummryRow> bookingCountSummryRowsScan) {
        this.bookingCountSummryRowsScan = bookingCountSummryRowsScan;
    }

    public boolean isSummery() {
        return summery;
    }

    public void setSummery(boolean summery) {
        this.summery = summery;
    }

}
