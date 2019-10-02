package com.example.createpdf3;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfOutline;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    EditText mTextEt, mTextEt2;
    Button mSaveBtn;
    public static final int STORAGE_CODE = 1000;
    String[] f = {"o", "K", "c", "w", "q", "c", "t", "w", "x", "z"};
    String[] S = {"o", "K", "c", "w", "q", "c", "t", "w", "x", "z"};
    String[] T = {"o", "K", "c", "w", "q", "c", "t", "w", "x", "z"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextEt = findViewById(R.id.textEt);
        mTextEt2 = findViewById(R.id.textEt2);
        mSaveBtn = findViewById(R.id.saveBtn);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //1
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, STORAGE_CODE);
                    } else {
                        //2
                        savePDF();
                    }
                } else {
                    //3
                    savePDF();
                }


            }
        });
    }//================================================

    private void savePDF() {
        String mFileName = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss",
                Locale.getDefault()).format(System.currentTimeMillis());
        String mFilePath = Environment.getExternalStorageDirectory() + "/" + "ORZ.." + mFileName + ".pdf";
        Document document = new Document(PageSize.A4, 20, 20, 10, 40);
        try {
//            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(mFilePath));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(mFilePath));
            TOCCreation event = new TOCCreation();
            writer.setPageEvent(event);

            document.open();
            //======================================================================================
            document.add(new Paragraph("Title PDF"));

            event.setRoot(writer.getRootOutline());
            ColumnText ct = new ColumnText(writer.getDirectContent());
            PdfPTable tabbbb = new PdfPTable(9);
            tabbbb.setWidthPercentage(90);
            PdfPCell cell = new PdfPCell(new Paragraph(new Phrase(24f, "Time")));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBackgroundColor(BaseColor.GRAY);
            PdfPCell cell2 = new PdfPCell(new Paragraph(new Phrase(24f, "T")));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell2.setBackgroundColor(BaseColor.GRAY);
            PdfPCell cell3 = new PdfPCell(new Paragraph(new Phrase(24f, "H")));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell3.setBackgroundColor(BaseColor.GRAY);
            for (int i = 0; i < 3; i++) {
                tabbbb.addCell(cell);
                tabbbb.addCell(cell2);
                tabbbb.addCell(cell3);
            }

            document.add(tabbbb);
            int start;
            int end;
            for (int i = 0; i <= f.length-1; ) {
                start = (i) + 1;
                i++;
                end = i;
                String title = String.format("", start, end);
                Chunk c = new Chunk(title);
                c.setGenericTag(title);
                ct.addElement(c);
                ct.addElement(createTable(start, end));
            }
            int column = 0;
            do {
                if (column == 3) {

                    document.newPage();
                    document.add(new Paragraph("Title PDF"));
                    document.add(tabbbb);
                    column = 0;
                }
                ct.setSimpleColumn(COLUMNS[column++]);
            }
            while (ColumnText.hasMoreText(ct.go()));
            document.newPage();
//            for (TOCEntry entry : event.getToc()) {
//                Chunk c = new Chunk(entry.title);
//                c.setAction(entry.action);
//                document.add(new Paragraph(c));
//            }


            //======================================================================================
            document.close();
            Toast.makeText(this, "ORZ.." + mFileName + ".pdf\nis saved to\n" + mFilePath, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.d("BT", String.valueOf(e));
        } catch (DocumentException e) {
            Log.d("BT", String.valueOf(e));
        } catch (Exception e) {
            Log.d("BT", String.valueOf(e));
        }
    }//====================

    public static final Rectangle[] COLUMNS = {//X,Y,W,H
//            new Rectangle(36, 36, 192, 806),
//            new Rectangle(204, 36, 348, 806),
//            new Rectangle(360, 36, 504, 806)
            new Rectangle(48, 50, 213, 795),
            new Rectangle(215, 50, 380, 795),
            new Rectangle(381, 50, 547, 795)
    };//192是第一區塊的寬度

    public PdfPTable createTable(int start, int end) throws IOException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        for (int i = start; i <= end; i++) {
//            table.addCell(String.valueOf(i));
//            table.addCell("Test" + i);
//            table.addCell("Data");

            table.addCell(f[i-1]);
            table.addCell(S[i-1]);
            table.addCell(T[i-1]);
        }

        return table;
    }

    public class TOCCreation extends PdfPageEventHelper {
        protected PdfOutline root;
        protected List<TOCEntry> toc = new ArrayList<TOCEntry>();

        public TOCCreation() {

        }

        public void setRoot(PdfOutline root) {
            this.root = root;
        }

        public List<TOCEntry> getToc() {
            return toc;
        }

        @Override
        public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {
            PdfDestination dest = new PdfDestination(PdfDestination.XYZ, rect.getLeft(), rect.getTop(), 0);
            new PdfOutline(root, dest, text);
            TOCEntry entry = new TOCEntry();
            entry.action = PdfAction.gotoLocalPage(writer.getPageNumber(), dest, writer);
            entry.title = text;
            toc.add(entry);
        }
    }

    public class TOCEntry {
        protected PdfAction action;
        protected String title;
    }

}
