package com.biswa1045.tnpnotice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PDFActivity extends AppCompatActivity {

private PdfDocument document;
    EditText date,passout_edit;
    DatePickerDialog datePickerDialog;
    EditText notice_no,position,ctc,deadline,text,link,company;
    long length_of_notice;
    private StreamListAdapter adapter_stream;
    private ArrayList<StreamList> streamlist = new ArrayList<>();
    private BranchListAdapter adapter_branch;
    private ArrayList<StreamList> branchlist = new ArrayList<>();
    String current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfactivity);
        RecyclerView stream_recycle = findViewById(R.id.streamslist_rec);
        RecyclerView branchlist_rec = findViewById(R.id.branchlist_rec);
        GridLayoutManager layoutManager=new GridLayoutManager(this,4);
        GridLayoutManager layoutManager2=new GridLayoutManager(this,3);
        stream_recycle.setLayoutManager(layoutManager);
        branchlist_rec.setLayoutManager(layoutManager2);
        adapter_stream = new StreamListAdapter(this,streamlist);
        stream_recycle.setAdapter(adapter_stream);
        adapter_branch = new BranchListAdapter(this,branchlist);
        branchlist_rec.setAdapter(adapter_branch);
        branchlist.add(new StreamList("ETC",false));
        branchlist.add(new StreamList("Electrical",false));
        branchlist.add(new StreamList("Chemical",false));
        branchlist.add(new StreamList("CSE",false));
        branchlist.add(new StreamList("Mechanical",false));
        branchlist.add(new StreamList("Production",false));
        branchlist.add(new StreamList("Metallurgy",false));
        branchlist.add(new StreamList("Civil",false));
        branchlist.add(new StreamList("Applied Physics",false));
        branchlist.add(new StreamList("Applied Chemistry",false));
        branchlist.add(new StreamList("Applied Mathematics",false));
        adapter_branch.setOptionList(branchlist);
        streamlist.add(new StreamList("B.Tech",false));
        streamlist.add(new StreamList("M.Tech",false));
        streamlist.add(new StreamList("MCA",false));
        streamlist.add(new StreamList("M.Sc",false));
        streamlist.add(new StreamList("Diploma",false));
        adapter_stream.setOptionList(streamlist);
        notice_no = findViewById(R.id.notice_no);
        position = findViewById(R.id.position);
        ctc = findViewById(R.id.ctc);
        company= findViewById(R.id.company);
        deadline = findViewById(R.id.deadline);
        text = findViewById(R.id.text);
        date = (EditText) findViewById(R.id.date);
        link = findViewById(R.id.link);
        passout_edit = findViewById(R.id.passout_edit);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                length_of_notice= s.length();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(PDFActivity
                        .this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.setText(dayOfMonth + "."
                                        + (monthOfYear + 1) + "." + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        findViewById(R.id.create_pdf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
                 current = sdf.format(new Date());
                ArrayList<String> streamlist_s = null;
                if (adapter_stream.getSelected().size() > 0) {
                    streamlist_s = new ArrayList<>();
                    for (int i = 0; i < adapter_stream.getSelected().size(); i++) {
                        streamlist_s.add(adapter_stream.getSelected().get(i).getName());
                    }
                }
                ArrayList<String> branchlist_s = null;
                if (adapter_branch.getSelected().size() > 0) {
                    branchlist_s = new ArrayList<>();
                    for (int i = 0; i < adapter_branch.getSelected().size(); i++) {
                        branchlist_s.add(adapter_branch.getSelected().get(i).getName());
                    }
                }
              //  if(branchlist_s!=null&&streamlist_s!=null&&!date.getText().toString().equals("")&&!notice_no.getText().toString().equals("")&&!text.getText().toString().equals("")) {
                    String com = company.getText().toString();
                    String dl = deadline.getText().toString();
                    String p = position.getText().toString();
                    String c = ctc.getText().toString();
                    String n = notice_no.getText().toString();
                    String d = date.getText().toString();
                    String t = text.getText().toString();
                    String l = link.getText().toString();
                    String passout_s = passout_edit.getText().toString();
                    if(streamlist_s!=null && branchlist_s!=null){
                        createPdfFromView(n, d, t, l, length_of_notice, dl, p, c, com, streamlist_s, branchlist_s,passout_s);
                    }else{
                        Toast.makeText(PDFActivity.this, "Enter Branch and Stream...", Toast.LENGTH_SHORT).show();
                    }

            }
        });
    }

    private void createFile(){
        Intent in = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        in.addCategory(Intent.CATEGORY_OPENABLE);
        in.setType("application/pdf");
        in.putExtra(Intent.EXTRA_TITLE,current+"Notice.pdf");
        startActivityForResult(in,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode== Activity.RESULT_OK){
            Uri uri ;
            if(data!=null){
                uri = data.getData();
                if(document!=null){
                    ParcelFileDescriptor pdf ;
                    try{
                        pdf = getContentResolver().openFileDescriptor(uri,"w");
                        FileOutputStream fileOutputStream= new FileOutputStream(pdf.getFileDescriptor());
                        document.writeTo(fileOutputStream);
                        document.close();
                        Toast.makeText(this, "Notice Saved!!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }catch (IOException e){
                        try {
                            DocumentsContract.deleteDocument(getContentResolver(),uri);

                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        e.printStackTrace();

                    }
                }
            }else {

            }
        }
    }

    public void createPdfFromView(String notice,String date,String text,String link,long length_of_notice,String dl,String p,String c,String com,ArrayList<String> streamlist,ArrayList<String> branchlist,String passout_s){
        final Dialog dialog =new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.notice_lauout);
        TextView notice_pdf = dialog.findViewById(R.id.notice_no_pdf);
        notice_pdf.setText(""+notice);
        TextView date_pdf = dialog.findViewById(R.id.date_pdf);
        date_pdf.setText("Date: "+date);
        TextView text_pdf = dialog.findViewById(R.id.text_notice);
        TextView company_pdf = dialog.findViewById(R.id.company);
        TextView stream_t = dialog.findViewById(R.id.stream_t);
        TextView branch_t = dialog.findViewById(R.id.branch_t);
        TextView position_t = dialog.findViewById(R.id.position_t);
        TextView ctc_t = dialog.findViewById(R.id.ctc_t);
        TextView deadline_t = dialog.findViewById(R.id.deadline_t);
        TextView stream_pdf = dialog.findViewById(R.id.stream);
        TextView branch_pdf = dialog.findViewById(R.id.branch);
        TextView position_pdf = dialog.findViewById(R.id.position);
        TextView deadline_pdf = dialog.findViewById(R.id.deadline);
        TextView ctc_pdf = dialog.findViewById(R.id.ctc);
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"Calibri Regular.ttf");
        Typeface typeFace2=Typeface.createFromAsset(getAssets(),"AbhayaLibre_ExtraBold.ttf");
        TextView b1 = dialog.findViewById(R.id.b1);
        TextView h1 = dialog.findViewById(R.id.h1);
        TextView h2 = dialog.findViewById(R.id.h2);
        TextView h3 = dialog.findViewById(R.id.h3);
        TextView h4 = dialog.findViewById(R.id.h4);
        TextView passout_t = dialog.findViewById(R.id.passout_t);
        TextView passout = dialog.findViewById(R.id.passout);
        passout.setTypeface(typeFace);
        passout.setText(passout_s);
        passout_t.setTypeface(typeFace,Typeface.BOLD);
        passout_t.setText("Pass Out: ");
        TextView link_t = dialog.findViewById(R.id.link_t);
        TextView link_text = dialog.findViewById(R.id.link_text);
        link_text.setTypeface(typeFace);
        link_text.setText(link+"");
        b1.setTypeface(typeFace);
        h1.setTypeface(typeFace2);
        h2.setTypeface(typeFace2);
        h3.setTypeface(typeFace2);
        h4.setTypeface(typeFace2);
        company_pdf.setTypeface(typeFace2);
        company_pdf.setText("--  "+com+"  --");
        stream_pdf.setTypeface(typeFace);
        branch_pdf.setTypeface(typeFace);
        position_pdf.setTypeface(typeFace);
        deadline_pdf.setTypeface(typeFace);
        deadline_pdf.setText(""+dl);
        deadline_t.setTypeface(typeFace,Typeface.BOLD);
        deadline_t.setText("DeadLine:");
        ctc_t.setTypeface(typeFace,Typeface.BOLD);
        ctc_t.setText("CTC:");
        branch_t.setTypeface(typeFace,Typeface.BOLD);
        branch_t.setText("Branch:");
        stream_t.setTypeface(typeFace,Typeface.BOLD);
        stream_t.setText("Stream:");
        position_t.setTypeface(typeFace,Typeface.BOLD);
        position_t.setText("Position:");
        link_t.setTypeface(typeFace,Typeface.BOLD);
        link_t.setText("Link:");
        position_pdf.setText(""+p);
        ctc_pdf.setTypeface(typeFace);
        ctc_pdf.setText(""+c);
        text_pdf.setTypeface(typeFace);
        stream_pdf.setText(""+streamlist.toString());
        branch_pdf.setText(""+branchlist.toString());
        text_pdf.setText(""+text);
        long notice_line=(length_of_notice/90)+2;
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width=1654;
        lp.height=2339;
        if(com.equals("")){
            company_pdf.setVisibility(View.GONE);
        }
        if(dl.equals("")){
            dialog.findViewById(R.id.deadline_layout).setVisibility(View.GONE);
        }
        if(p.equals("")){
            dialog.findViewById(R.id.position_layout).setVisibility(View.GONE);
        }
        if(c.equals("")){
            dialog.findViewById(R.id.ctc_layout).setVisibility(View.GONE);

        }if(link.equals("")){
            dialog.findViewById(R.id.link_layout).setVisibility(View.GONE);
        }if(passout_s.equals("")){
            dialog.findViewById(R.id.passout_layout).setVisibility(View.GONE);
        }
      //  lp.width = WindowManager.LayoutParams.MATCH_PARENT;
      //  lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                generatePDFFromView(dialog.findViewById(R.id.noticeView), notice_line,link);
            }
        }, 10);
    }

    private void generatePDFFromView(View view,long line,String link){
        Bitmap bitmap = getBitmapFromView(view);
        document =new PdfDocument();
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(1654,2339,1).create();
        PdfDocument.Page mypage = document.startPage(mypageInfo);
        Canvas canvas = mypage.getCanvas();
        canvas.drawBitmap(bitmap,70,50,null);
        Paint paintText =new Paint();
        paintText.setTypeface(Typeface.createFromAsset(getAssets(),"Calibri Regular.ttf"));
        paintText.setTextSize(42);
        paintText.setColor(ContextCompat.getColor(this,R.color.black));
        paintText.setTextAlign(Paint.Align.LEFT);
        long h =  900;
     //   canvas.drawText(""+link,220,h,paintText);

        document.finishPage(mypage);
        createFile();
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(1654,2339,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if(bgDrawable!=null){
            bgDrawable.draw(canvas);

        }else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    private void generatePDF(){
        document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1654,2339,1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paintText =new Paint();
        paintText.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        paintText.setTextSize(25);
        paintText.setColor(ContextCompat.getColor(this,R.color.black));
        paintText.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("",0,50,paintText);
        document.finishPage(page);

    }
}