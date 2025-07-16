import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Parse {
    public static List<Transaction> parseTransactionsFromText(String text) {
        List<Transaction> transactions = new ArrayList<>();

        String[] lines = text.split("\\R");
        int i = 0;

        Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
        Pattern amountLinePattern = Pattern.compile(".*\\d+\\.\\d{2} [A-Z]{3} (MASTERCARD|VISA) (SUCCESS|FAILED|PENDING)");

        // Пропускаем заголовки и пустые строки
        while (i < lines.length && (lines[i].trim().isEmpty() || lines[i].contains("Creation Date"))) {
            i++;
        }

        while (i < lines.length) {
            try {
                // 1. Creation Date (GMT)
                if (i >= lines.length) break;
                String creationDate = lines[i].trim();
                if (!datePattern.matcher(creationDate).matches()) {
                    i++;
                    continue;
                }
                i++;

                // 2. NPGW reference: от 1 до 7 строк, пока не встретится пустая строка, дата или amountLine
                StringBuilder npgwRefBuilder = new StringBuilder();
                while (i < lines.length) {
                    String line = lines[i].trim();
                    if (line.isEmpty() || datePattern.matcher(line).matches() || amountLinePattern.matcher(line).matches()) {
                        break;
                    }
                    npgwRefBuilder.append(line);
                    i++;
                    if (npgwRefBuilder.length() >= 101) {
                        break;
                    }
                }
                String npgwReference = npgwRefBuilder.toString().trim();

                // 3. Business unit reference: от 1 до 3 строк, с теми же условиями, кроме суммы (которая должна идти строго после)
                StringBuilder businessUnitBuilder = new StringBuilder();
                String amountLine = null;

                while (i < lines.length) {
                    String line = lines[i].trim();
                    if (line.isEmpty() || datePattern.matcher(line).matches()) {
                        break;
                    }

                    if (amountLinePattern.matcher(line).matches()) {
                        // В строке есть amountLine, но перед ней может быть часть business unit
                        String[] parts = line.split("\\s+", 2);
                        businessUnitBuilder.append(parts[0]).append("");
                        if (parts.length > 1) {
                            amountLine = parts[1].trim();
                        } else {
                            amountLine = "";
                        }
                        i++;
                        break; // дальше amountLine
                    } else {
                        // Обычная строка business unit
                        businessUnitBuilder.append(line).append("");
                        i++;
                        if (businessUnitBuilder.length() >= 50) {
                            break;
                        }
                    }
                }

                String businessUnitReference = businessUnitBuilder.toString().trim();

                // Если amountLine не был взят из предыдущей строки, возьмем следующую
                if (amountLine == null) {
                    if (i < lines.length) {
                        amountLine = lines[i].trim();
                        i++;
                    } else {
                        System.out.println("Не найдена строка с суммой");
                        break;
                    }
                }

                // Проверяем, что это действительно строка с суммой и остальным
                if (!amountLinePattern.matcher(amountLine).matches()) {
                    System.out.println("Строка с суммой и остальным не распарсена: " + amountLine);
                    continue;
                }

                String[] tokens = amountLine.split("\\s+");
                if (tokens.length < 4) {
                    System.out.println("Неверный формат строки с суммой: " + amountLine);
                    continue;
                }

                double amount;
                try {
                    amount = Double.parseDouble(tokens[0]);
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка парсинга суммы: " + tokens[0]);
                    continue;
                }

                String currency = tokens[1];
                String cardType = tokens[2];
                String status = tokens[3];

                Transaction transaction = new Transaction(
                        creationDate,
                        npgwReference,
                        businessUnitReference,
                        amount,
                        currency,
                        cardType,
                        status
                );

                transactions.add(transaction);

            } catch (Exception ex) {
                System.out.println("Ошибка при парсинге записи: " + ex.getMessage());
                i++;
            }
        }

        return transactions;
    }
}
