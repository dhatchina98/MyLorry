package com.dcode.mylorry;

import android.content.Context;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import androidx.annotation.NonNull;

final class PDFUtility {
    private static final String TAG = PDFUtility.class.getSimpleName();
    private static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

    private static Font FONT_CELL = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    private static Font FONT_COLUMN = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);

    public interface OnDocumentClose {
        void onPDFDocumentClose(File file);
    }

    static void createPdf(@NonNull Context mContext, OnDocumentClose mCallback, List<String[]> items, @NonNull String filePath, boolean isPortrait, String BillType, String title, String subtitle, List<String[]> Amount_data) throws Exception {
        if (filePath.equals("")) {
            throw new NullPointerException("PDF File Name can't be null or blank. PDF File can't be created");
        }

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            Thread.sleep(50);
        }
        Document document = new Document();
        document.setMargins(24f, 24f, 32f, 32f);
        document.setPageSize(isPortrait ? PageSize.A4 : PageSize.A4.rotate());
        PdfWriter pdfWriter = PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(filePath)));
        pdfWriter.setFullCompression();
        pdfWriter.setPageEvent(new PageNumeration());
        document.open();
        setMetaData(document);
        document.add(addHeader(title, subtitle));
        addEmptyLine(document, 1);
        document.add(createDataTable(items, BillType));
        addEmptyLine(document, 1);
        if (BillType.equals("Customer_Bill")) {
            boolean is_Amount_data = false;
            int Total = 0;
            if (!Amount_data.isEmpty()) {
                for (int i = 0; i < Amount_data.size(); i++) {
                    Total += Integer.parseInt(Amount_data.get(i)[1]);
                }
                String[] total_column = new String[]{"  Total:", String.valueOf(Total)};
                Amount_data.add(total_column);
                is_Amount_data = true;
            }
            int load_debit_Total = 0;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i)[7].equals("Credit")) {
                    load_debit_Total += Integer.parseInt(items.get(i)[6]);
                }
            }
            String[] amount;
            String[] load_total;
            String[] final_total;
            if (is_Amount_data) {
                load_total = new String[]{"Total Bill:", String.valueOf(load_debit_Total)};
                amount = new String[]{"Received Money:", Amount_data.get(Amount_data.size() - 1)[1]};
                String total_balance ="0";
                if((load_debit_Total - Total)>0){
                    total_balance=String.valueOf(load_debit_Total - Total);
                }
                final_total = new String[]{"Total Balance:",total_balance};
            } else {
                load_total = new String[]{"Total Balance:", String.valueOf(load_debit_Total)};
                amount = null;
                final_total = null;
            }
            PdfPTable parentAmountTable=new PdfPTable(1);
            if(is_Amount_data){
            parentAmountTable.setWidthPercentage(100);

            // Create cells for the parent table
            PdfPCell TopCell = new PdfPCell(addReceivedMoneyHeader("Received Money :-"));
            PdfPCell BottomCell = new PdfPCell(createAmountTable(Amount_data));

            // Set cell properties for alignment
            TopCell.setBorder(PdfPCell.NO_BORDER);
            BottomCell.setBorder(PdfPCell.NO_BORDER);

            // Add cells to the parent table
            parentAmountTable.addCell(TopCell);
            parentAmountTable.addCell(BottomCell);
        }

            PdfPTable parentbillTable = new PdfPTable(1);
            parentbillTable.setWidthPercentage(100);

            // Create cells for the parent table
            PdfPCell TopCell1 = new PdfPCell(createBalanceTable(load_total, amount, final_total, is_Amount_data));
            PdfPCell BottomCell1 = new PdfPCell();

            // Set cell properties for alignment
            TopCell1.setBorder(PdfPCell.NO_BORDER);
            BottomCell1.setBorder(PdfPCell.NO_BORDER);
//            BottomCell1.setBorderWidth(0);

            // Add cells to the parent table
            parentbillTable.addCell(TopCell1);
            parentbillTable.addCell(BottomCell1);

            float[] arr = new float[]{1f, 0.75f,1.25f};
            PdfPTable parentTable = new PdfPTable(3);
            parentTable.setWidthPercentage(100);
            parentTable.setWidths(arr);

            // Create cells for the parent table
            PdfPCell leftCell = new PdfPCell(parentAmountTable);
            PdfPCell gapCell = new PdfPCell(); // Cell for the gap between tables
            PdfPCell rightCell = new PdfPCell(parentbillTable);

            // Set cell properties for alignment
            leftCell.setBorder(PdfPCell.NO_BORDER);
            gapCell.setBorder(PdfPCell.NO_BORDER);
            rightCell.setBorder(PdfPCell.NO_BORDER);
            gapCell.setFixedHeight(10f); // Set the height of the gap cell

           // Add cells to the parent table
            parentTable.addCell(leftCell);
            parentTable.addCell(gapCell);
            parentTable.addCell(rightCell);
            document.add(parentTable);
        }
        document.close();

        try {
            pdfWriter.close();
        } catch (Exception ex) {
            Log.e(TAG, "Error While Closing pdfWriter : " + ex.toString());
        }

        if (mCallback != null) {
            mCallback.onPDFDocumentClose(file);
        }
    }

    private static void addEmptyLine(Document document, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            document.add(new Paragraph(" "));
        }
    }

    private static void setMetaData(Document document) {
        document.addCreationDate();
        document.addAuthor("MyLorryApp");
        document.addCreator("Dhatchinamoorthi.M");
        document.addHeader("SoftwareDeveloper", "");
    }

    private static PdfPTable addHeader(String title, String subtitle) throws Exception {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{7});
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell;
        {
            /*MIDDLE TEXT*/
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(8f);
            cell.setUseAscender(true);

            Paragraph temp = new Paragraph(title, FONT_TITLE);
            temp.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(temp);

            temp = new Paragraph(subtitle, FONT_SUBTITLE);
            temp.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(temp);

            table.addCell(cell);
        }
        return table;
    }

    private static PdfPTable createDataTable(List<String[]> dataTable, String BillType) throws DocumentException {
        int len = 0;
        float[] arr = new float[0];
        String[] columnArr = new String[0];
        if (BillType.equals("Customer_Bill")) {
            len = 8;
            arr = new float[]{1f, 1.2f, 1f, 1f, 0.75f, 1.4f, 0.90f, 0.75f};
            columnArr = new String[]{"Date", "Vehicle", "Product", "Quantity", "Unit", "Site", "Amount", "Cash"};
        } else if (BillType.equals("Vendor_Bill")) {
            len = 4;
            arr = new float[]{1f, 1f, 1f, 1f};
            columnArr = new String[]{"Date", "Vehicle", "Product", "Quantity"};
        } else if (BillType.equals("Order_Bill")) {
            len = 10;
            arr = new float[]{1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f};
            columnArr = new String[]{"Date", "Vehicle", "From", "Site", "Customer", "Product", "Quantity", "Unit", "Amount", "Cash"};
        }
        PdfPTable table1 = new PdfPTable(len);
        table1.setWidthPercentage(100);
        table1.setWidths(arr);
        table1.setHeaderRows(1);
        table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell;
        {
            for (int i = 0; i < columnArr.length; i++) {
                cell = new PdfPCell(new Phrase(columnArr[i], FONT_COLUMN));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(4f);
                table1.addCell(cell);
            }
        }

        float top_bottom_Padding = 8f;
        float left_right_Padding = 4f;
        boolean alternate = false;

        BaseColor lt_gray = new BaseColor(221, 221, 221); //#DDDDDD
        BaseColor cell_color;

        int size = dataTable.size();

        for (int i = 0; i < size; i++) {
            cell_color = alternate ? lt_gray : BaseColor.WHITE;
            String[] temp = dataTable.get(i);
            for (int j = 0; j < temp.length; j++) {
                cell = new PdfPCell(new Phrase(temp[j], FONT_CELL));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPaddingLeft(left_right_Padding);
                cell.setPaddingRight(left_right_Padding);
                cell.setPaddingTop(top_bottom_Padding);
                cell.setPaddingBottom(top_bottom_Padding);
                cell.setBackgroundColor(cell_color);
                table1.addCell(cell);
            }
            alternate = !alternate;
        }
        return table1;
    }

//    private static PdfPTable createSignBox() throws DocumentException {
//        PdfPTable outerTable = new PdfPTable(1);
//        outerTable.setWidthPercentage(100);
//        outerTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
//
//        PdfPTable innerTable = new PdfPTable(1);
//        {
//            innerTable.setWidthPercentage(100);
//            innerTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
//            //ROW-4 : Content Right Aligned
//            PdfPCell cell = new PdfPCell(new Phrase("Signature of Customer", FONT_SUBTITLE));
//            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//            cell.setBorder(PdfPCell.NO_BORDER);
//            cell.setPadding(4f);
//            innerTable.addCell(cell);
//        }
//
//        PdfPCell signRow = new PdfPCell(innerTable);
//        signRow.setHorizontalAlignment(Element.ALIGN_LEFT);
//        signRow.setBorder(PdfPCell.NO_BORDER);
//        signRow.setPadding(4f);
//
//        outerTable.addCell(signRow);
//
//        return outerTable;
//    }

    private static PdfPTable addReceivedMoneyHeader(String title) throws Exception {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{7});
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell cell;
        {
            /*MIDDLE TEXT*/
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(8f);
            cell.setUseAscender(true);

            Paragraph temp = new Paragraph(title, FONT_TITLE);
            temp.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(temp);

            table.addCell(cell);
        }
        table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        return table;
    }

    private static PdfPTable createAmountTable(List<String[]> dataTable) throws DocumentException {

        float[] arr = new float[]{1f, 1f};
        String[] columnArr = new String[]{"Date", "Amount"};

        PdfPTable table1 = new PdfPTable(2);
        table1.setWidthPercentage(30);
        table1.setWidths(arr);
        table1.setHeaderRows(1);
        table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell cell;
        {
            for (int i = 0; i < columnArr.length; i++) {
                cell = new PdfPCell(new Phrase(columnArr[i], FONT_COLUMN));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(4f);
                table1.addCell(cell);
            }
        }

        float top_bottom_Padding = 8f;
        float left_right_Padding = 4f;
        boolean alternate = false;

        BaseColor lt_gray = new BaseColor(221, 221, 221); //#DDDDDD
        BaseColor cell_color;

        int size = dataTable.size();

        for (int i = 0; i < size; i++) {
            cell_color = alternate ? lt_gray : BaseColor.WHITE;
            String[] temp = dataTable.get(i);
            for (int j = 0; j < temp.length; j++) {
                cell = new PdfPCell(new Phrase(temp[j], FONT_CELL));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPaddingLeft(left_right_Padding);
                cell.setPaddingRight(left_right_Padding);
                cell.setPaddingTop(top_bottom_Padding);
                cell.setPaddingBottom(top_bottom_Padding);
                cell.setBackgroundColor(cell_color);
                table1.addCell(cell);
            }
            alternate = !alternate;
        }
        table1.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        return table1;
    }

    private static PdfPTable createBalanceTable(String[] load_total, String[] Amount_data, String[] final_total, boolean is_there) throws DocumentException {
        float[] arr = new float[]{1f, 1f};

        PdfPTable table1 = new PdfPTable(2);
        table1.setWidthPercentage(40);
        table1.setWidths(arr);
        table1.setHeaderRows(0);
        table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell;

        float top_bottom_Padding = 8f;
        float left_right_Padding = 4f;
        boolean alternate = false;

        BaseColor lt_gray = new BaseColor(221, 221, 221); //#DDDDDD
        BaseColor cell_color;
        int size = 1;
        if (is_there) {
            size = 3;
        }
        for (int i = 0; i < size; i++) {
            cell_color = alternate ? lt_gray : BaseColor.WHITE;
            String[] temp = null;
            if (i == 0) {
                temp = load_total;
            } else if (i == 1) {
                temp = Amount_data;
            } else {
                temp = final_total;
            }

            for (int j = 0; j < temp.length; j++) {
                cell = new PdfPCell(new Phrase(temp[j], FONT_CELL));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPaddingLeft(left_right_Padding);
                cell.setPaddingRight(left_right_Padding);
                cell.setPaddingTop(top_bottom_Padding);
                cell.setPaddingBottom(top_bottom_Padding);
                cell.setBackgroundColor(cell_color);
                table1.addCell(cell);
            }
            alternate = !alternate;
        }
        table1.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
        return table1;
    }

}

