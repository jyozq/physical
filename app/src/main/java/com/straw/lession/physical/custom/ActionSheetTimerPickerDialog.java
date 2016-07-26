package com.straw.lession.physical.custom;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.straw.lession.physical.R;
import com.straw.lession.physical.adapter.wheelview.NumericWheelAdapter;
import com.straw.lession.physical.custom.wheelview.OnWheelScrollListener;
import com.straw.lession.physical.custom.wheelview.WheelView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/12/17.
 */
public class ActionSheetTimerPickerDialog {
    private Context context;
    private Dialog dialog;
    //private TextView txt_cancel;
    //private TextView txt_no , txt_yes;

    private boolean showTitle = false;
    private Display display;
    private View.OnClickListener onClickListener;

    private View timeView=null;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private int mYear=1996;
    private int mMonth=0;
    private int mDay=1;

    private EditText juzhuTimeText;

    private boolean isCalu = true;  //是否计算

    private int type;
    public ActionSheetTimerPickerDialog(Context context , View.OnClickListener onClickListener , EditText juzhuTimeText , int type) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        this.onClickListener = onClickListener;
        this.juzhuTimeText = juzhuTimeText;
        this.type = type;

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth =  c.get(Calendar.MONTH);
        mDay =  c.get(Calendar.DAY_OF_MONTH);

        current_year = mYear;//年
        current_month = mMonth + 1;//月
        current_day = mDay;
    }

    public ActionSheetTimerPickerDialog(Context context , View.OnClickListener onClickListener , EditText juzhuTimeText , boolean  isCalu ) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        this.onClickListener = onClickListener;
        this.juzhuTimeText = juzhuTimeText;
        this.isCalu = isCalu;

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth =  c.get(Calendar.MONTH);
        mDay =  c.get(Calendar.DAY_OF_MONTH);

        current_year = mYear;//年
        current_month = mMonth + 1;//月
        current_day = mDay;
    }



    public ActionSheetTimerPickerDialog builder() {
        getDataPick();
        timeView.setMinimumWidth(display.getWidth());



        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(timeView);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        return this;
    }


    private View getDataPick() {
        Calendar c = Calendar.getInstance();
        int norYear = c.get(Calendar.YEAR) + 20;

        int curYear = mYear;
        int curMonth =mMonth+1;
        int curDate = mDay;

        timeView = LayoutInflater.from(context).inflate(R.layout.wheel_date_picker, null);

        Button okbtn = (Button) timeView.findViewById(R.id.ok_btn);
        Button cancel_btn = (Button) timeView.findViewById(R.id.cancel_btn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showText();
                dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        year = (WheelView) timeView.findViewById(R.id.year);
        NumericWheelAdapter numericWheelAdapter1=new NumericWheelAdapter(context,1950, norYear);
        numericWheelAdapter1.setLabel("年");
        year.setViewAdapter(numericWheelAdapter1);
        year.setCyclic(false);//是否可循环滑动
        year.addScrollingListener(scrollListener);

        month = (WheelView) timeView.findViewById(R.id.month);
        NumericWheelAdapter numericWheelAdapter2=new NumericWheelAdapter(context,1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        month.setViewAdapter(numericWheelAdapter2);
        month.setCyclic(false);
        month.addScrollingListener(scrollListener);

        day = (WheelView) timeView.findViewById(R.id.day);
        initDay(curYear,curMonth);
        day.setCyclic(false);

        year.setVisibleItems(7);//设置显示行数
        month.setVisibleItems(7);
        day.setVisibleItems(7);

        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);
        return timeView;
    }

    private int current_year;
    private int current_month;
    private int current_day;


    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }
        @Override
        public void onScrollingFinished(WheelView wheel) {
           /* int n_year = year.getCurrentItem() + 1950;//年
            int n_month = month.getCurrentItem() + 1;//月
            int n_day = day.getCurrentItem();*/

            current_year = year.getCurrentItem() + 1950;//年
            current_month = month.getCurrentItem() + 1;//月
            current_day = day.getCurrentItem();


            /*if (isCalu){  //计算
                initDay(n_year,n_month);
                String birthday=new StringBuilder().append((year.getCurrentItem()+1950)).append("-").append((month.getCurrentItem() + 1) < 10 ? "0" + (month.getCurrentItem() + 1) : (month.getCurrentItem() + 1)).append("-").append(((day.getCurrentItem()+1) < 10) ? "0" + (day.getCurrentItem()+1) : (day.getCurrentItem()+1)).toString();
                juzhuTimeText.setText(calculateDatePoor(birthday) + "年");
            }else {
                juzhuTimeText.setText(n_year + "/" + n_month + "/" + n_day );
            }*/

        }
    };

    private void showText(){
        if (isCalu){  //计算
            initDay(current_year,current_month);
            //String birthday=new StringBuilder().append((year.getCurrentItem()+1950)).append("-").append((month.getCurrentItem() + 1) < 10 ? "0" + (month.getCurrentItem() + 1) : (month.getCurrentItem() + 1)).append("-").append(((day.getCurrentItem()+1) < 10) ? "0" + (day.getCurrentItem()+1) : (day.getCurrentItem()+1)).toString();
            String birthday=new StringBuilder().append((year.getCurrentItem()+1950)).append((month.getCurrentItem() + 1) < 10 ? "0" + (month.getCurrentItem() + 1) : (month.getCurrentItem() + 1)).append(((day.getCurrentItem()+1) < 10) ? "0" + (day.getCurrentItem()+1) : (day.getCurrentItem()+1)).toString();
            //juzhuTimeText.setText(calculateDatePoor(birthday) + "年");
            if (type ==1){
                juzhuTimeText.setText(calculateDatePoor(birthday) + "年");
            }else {
                String yearmonth = calculateDatePoor2(birthday);
                juzhuTimeText.setTag( yearmonth );
                String []arr = yearmonth.split(",");
                String year = arr[0];
                String month = arr[1];
                if ("0".equals(year)){
                    juzhuTimeText.setText(month +"月" );
                }else if ("0".equals(month)){
                    juzhuTimeText.setText(year +"年" );
                }else {
                    juzhuTimeText.setText(year +"年" + month +"月");
                }

            }

            //current_day = day.getCurrentItem() + 1;
            //String juzhu = current_year + "/" + current_month + "/" + current_day ;
            //juzhuTimeText.setTag(juzhu);
        }else {
            current_day = day.getCurrentItem() + 1;
            juzhuTimeText.setText(current_year + "/" + current_month + "/" + current_day );
        }
    }

    public static final String calculateDatePoor2(String birthday) {
        if (TextUtils.isEmpty(birthday))
            return "0";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currTimeStr = sdf.format(new Date());

        int ret[] = getDateLength(birthday , currTimeStr);
        int year = ret[1] / 12 ;
        int month = ret[1] % 12 ;
        //System.out.println(ret[0]  +  ": "  +  ret[1]  +  ": "  +  ret[2]);
        if (month == 0){
            return String.valueOf(year) +"," + month;
        }else {
            if (year ==0) {
                //return  String.valueOf( month ) +"月";
                return year +"," + month;
            }else {
                //return String.valueOf( year ) + "年" + String.valueOf( month ) +"月";
                return year +"," + month;
            }
        }
    }
    static int[]  getDateLength(String  fromDate, String  toDate)  {
        Calendar  c1  =  getCal(fromDate);
        Calendar  c2  =  getCal(toDate);
        int[]  p1  =  {  c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH)  };
        int[]  p2  =  {  c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DAY_OF_MONTH)  };
        return  new  int[]  {  p2[0]  -  p1[0], p2[0]  *  12  +  p2[1]  -  p1[0]  *  12  -  p1[1], (int)  ((c2.getTimeInMillis()  -  c1.getTimeInMillis())  /  (24  *  3600  *  1000))  };
    }
    static Calendar  getCal(String  date)  {
        Calendar  cal  =  Calendar.getInstance();
        cal.clear();
        cal.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6))  -  1, Integer.parseInt(date.substring(6, 8)));
        return  cal;
    }


    /**
     * 根据日期计算年龄
     * @param birthday
     * @return
     */
    public static final String calculateDatePoor(String birthday) {
        try {
            if (TextUtils.isEmpty(birthday))
                return "0";
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date birthdayDate = sdf.parse(birthday);
            String currTimeStr = sdf.format(new Date());
            Date currDate = sdf.parse(currTimeStr);
            if (birthdayDate.getTime() > currDate.getTime()) {
                return "0";
            }
            long age = (currDate.getTime() - birthdayDate.getTime()) / (24 * 60 * 60 * 1000) + 1;
            String year = new DecimalFormat("0.00").format(age / 365f);
            if (TextUtils.isEmpty(year))
                return "0";
            return String.valueOf(new Double(year).intValue());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0";
    }


    /**
     *
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    private void initDay(int arg1, int arg2) {
        NumericWheelAdapter numericWheelAdapter=new NumericWheelAdapter(context,1, getDay(arg1, arg2), "%02d");
        numericWheelAdapter.setLabel("日");
        day.setViewAdapter(numericWheelAdapter);
    }





    public void dismiss(){
        dialog.dismiss();
    }

    public ActionSheetTimerPickerDialog setTitle(String title) {
        showTitle = true;
        return this;
    }

    public ActionSheetTimerPickerDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public ActionSheetTimerPickerDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void show() {
        //setSheetItems();
        dialog.show();
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }


}
