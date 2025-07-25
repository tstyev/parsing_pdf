import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelParse {
    public static List<Transaction> readTransactions(String filePath) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean skipHeader = true;

            for (Row row : sheet) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String creationDate = formatter.formatCellValue(row.getCell(0));
                String npqwReference = formatter.formatCellValue(row.getCell(1));
                String businessUnit = formatter.formatCellValue(row.getCell(2));
                String amountStr = formatter.formatCellValue(row.getCell(3));
                double amount = Double.parseDouble(amountStr);
                String currency = formatter.formatCellValue(row.getCell(4));
                String cardType = formatter.formatCellValue(row.getCell(5));
                String status = formatter.formatCellValue(row.getCell(6));

                transactions.add(new Transaction(
                        creationDate, npqwReference, businessUnit,
                        amount, currency, cardType, status
                ));
            }
        }

        return transactions;
    }

}
