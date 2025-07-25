import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        String pathToPdf = "transactions.pdf";
//        String pdfText = "";
//
//        try (PDDocument document = Loader.loadPDF(new File(pathToPdf))) {
//            PDFTextStripper pdfStripper = new PDFTextStripper();
//            pdfText = pdfStripper.getText(document);
//        } catch (IOException e) {
//            System.err.println("Ошибка чтения PDF: " + e.getMessage());
//            return;
//        }
//
//        List<Transaction> transactions = Parse.parseTransactionsFromText(pdfText);
//
//        for (Transaction t : transactions) {
//            System.out.println(t);
//        }

        String filePath = "test.xlsx";

        try {
            List<Transaction> tr = ExcelParse.readTransactions(filePath);
            tr.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
