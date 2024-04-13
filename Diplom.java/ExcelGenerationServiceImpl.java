package com.example.website.services;
import com.example.website.models.Receipt;
import com.example.website.models.Transfer;
import com.example.website.models.WriteOff;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import java.io.*;
import java.time.LocalDateTime;
import java.time.Month;
@Service
public class ExcelGenerationServicelmpl implements ExcelGenerationService {
public String generateTorg16(WriteOff writeoff) { try {
Resource resource = new ClassPathResource("static/documents/blank-torg-16.xlsx");
File file =	resource.getFile();
InputStream inputStream	= new FileInputStream(file);
Workbook workbook = new	XSSFWorkbook(inputStream);
Sheet sheet	= workbook.getSheetAt(0);
Row row = sheet.getRow(6); // организация
CellStyle cellStyle = workbook.createCellStyle(); cellStyle.setAlignment(HorizontalAlignment.CENTER) ;
for (int column =	0; column <=	55; column++) {
Cell cell = row.createCell(column);
cell.setCellValue("OOO АВТО-ПРО");
cell.setCellStyle(cellStyle); }
Row row2 = sheet.getRow(8); // склад как подразделение
CellStyle cellStyle2 = workbook.createCellStyle();
cellStyle2.setAlignment(HorizontalAlignment.CENTER) ;
for (int column =	0; column <=	67; column++) {
Cell cell2 = row2.createCell(column);
cell2.setCellValue(writeOff.getStock().getName());
cell2.setCellStyle(cellStyle2); }
Row row3 =	sheet.getRow(17);
Cell cell3	= row3.getCell(51);
cell3.setCellValue(writeOff.getId());
LocalDateTime today = LocalDateTime.now();
Month month	=	today.getMonth();
int monthNumber = month.getValue();
String date	=	today.getDayOfMonth()	+	"."	+ String.format("%02d", monthNumber) +	"."
+ today.getYear();
Cell cell4	= row3.getCell(57);
cell4.setCellValue(date);
Row row4 =	sheet.getRow(25);
Cell cell5 =	row4.getCell(11);
cell5.setCellValue(date);
Sheet sheet2	= workbook.getSheetAt(1);
int n =	0;
int price =	0;
for (int i =	5; i < writeOff.getListSize() +	5;	i++){
Row row5 =	sheet2.getRow(i);
Cell cell6	= row5.createCell(1);
cell6.setCellValue(writeOff.getWriteOffItems().get(n).getItem().getName());
Cell cell7	= row5.createCell(18);
cell7.setCellValue(writeOff.getWriteOffItems().get(n).getItem().getId());
Cell	cell8	=	row5.createCell(37);
cell8.setCellValue(writeOff.getWriteOffItems().get(n).getQuantity());
Cell	cell9	=	row5.createCell(57);
cell9.setCellValue(writeOff.getWriteOffItems().get(n).getItem().getPrice());
Cell cell10 = row5.createCell(66);
price += writeOff.getWriteOffItems().get(n).getQuantity() * writeOff.getWriteOffItems().get(n).getItem().getPrice();
cell10.setCellValue(writeOff.getWriteOffItems().get(n).getQuantity() * writeOff.getWriteOffItems().get(n).getItem().getPrice());
n++;
if (i <	5	+ writeOff.getListSize() -	1){
int insertRowIndex = i +	1;
sheet2.shiftRows(insertRowIndex, sheet2.getLastRowNum(), 1);
Row newRow = sheet2.createRow(insertRowIndex);
copyRow(row5, newRow, sheet);
}
if (i ==	5	+ writeOff.getListSize() -	1){
Row row6 = sheet2.getRow(i +	1);
Cell cell11 = row6.getCell(66);
cell11.setCellValue(price);
} }
File outputDirectory = ResourceUtils.getFile("classpath:static/documents");
if (!outputDirectory.exists()) {
outputDirectory.mkdirs();
}
String timestamp = String.valueOf(System.currentTimeMillis());
String newFileName =	"generated_torg_16_"	+ timestamp + ".xlsx";
File outputFile	=	new File(outputDirectory, newFileName);
String filePath	=	outputFile.getAbsolutePath();
OutputStream outputStream = new FileOutputStream(outputFile);
workbook.write(outputStream);
inputStream.close();
outputStream.close();
System.out.println("Файл ТОРГ-16 сгенерирован и сохранен");
return filePath;
} catch (Exception e) {
e.printStackTrace();
} return "error";
}
public String generateTorg1(Receipt receipt) { try {
Resource resource = new ClassPathResource("static/documents/blank-torg-1.xlsx");
File file	= resource.getFile();
InputStream inputStream = new FileInputStream(file);
Workbook workbook = new XSSFWorkbook(inputStream);
Sheet sheet	= workbook.getSheetAt(0);
Row row =	sheet.getRow(6);
Cell cell =	row.getCell(4);
cell.setCellValue("OOO АВТО-ПРО");
Row row2 =	sheet.getRow(8);
Cell cell2	= row2.getCell(4);
cell2.setCellValue(receipt.getStock().getName());
Row row3 =	sheet.getRow(19);
Cell cell3	= row3.getCell(53);
cell3.setCellValue(receipt.getId());
LocalDateTime today = LocalDateTime.now();	// дата в нужном формате
Month month	=	today.getMonth();
int monthNumber = month.getValue();
String date	=	today.getDayOfMonth()	+	"."	+ String.format("%02d", monthNumber) +	"."
+ today.getYear();
Row row4 =	sheet.getRow(19);
Cell cell4	= row4.getCell(64);
cell4.setCellValue(date);
Row row5 =	sheet.getRow(23);
Cell cell5	= row5.getCell(26);
cell5.setCellValue(receipt.getStock().getName());
Row row6 =	sheet.getRow(24);
Cell cell6	= row6.getCell(64);
cell6.setCellValue(today.getDayOfMonth());
Cell	cell7	=	row6.getCell(74);
cell7.setCellValue(String.format("%02d", monthNumber));
Cell	cell8	=	row6.getCell(97);
cell8.setCellValue(today.getYear());
Row	row7 =	sheet.getRow(25);
Cell	cell9	=	row7.getCell(39);
cell9.setCellValue("накладная №	"	+ receipt.getId() +	" от "	+
receipt.getCreatedAt().getDayOfMonth() +	"."	+ String.format("%02d", monthNumber) +	"."	+
receipt.getCreatedAt().getYear());
Row row8 = sheet.getRow(33);
Cell cell10 = row8.getCell(22);
cell10.setCellValue(receipt.getSupplier());
Row row9 = sheet.getRow(61);
Cell cellll = row9.getCell(3);
cell11.setCellValue(date);
Cell	cell12	=	row9.getCell(24);
cell12.setCellValue(date);
Cell	cell13	=	row9.getCell(89);
cell13.setCellValue(date);
Sheet sheet2 = workbook.getSheetAt(l);
int price =	0;
if (receipt.getListSize() ==	1){
Row row10 =	sheet2.getRow(6);
Cell cell14	= row10.getCell(2);
cell14.setCellValue(receipt.getReceiptItems().get(0).getItem().getName());
Cell	cell15	=	row10.getCell(24);
cell15.setCellValue(receipt.getReceiptItems().get(0).getItem().getId());
Cell	cell16	=	row10.getCell(55);
cell16.setCellValue(receipt.getReceiptItems().get(0).getItem().getPrice());
Cell cell18= row10.getCell(73);
cell18.setCellValue(receipt.getReceiptItems().get(0).getQuantity());
Cell cell19 = row10.getCell(97);
price +=
receipt.getReceiptItems().get(0).getQuantity()*receipt.getReceiptItems().get(0).getItem().getPric e();
cell19.setCellValue(price);
Row row11 =	sheet2.getRow(8);
Cell cell20	= row11.getCell(97);
cell20.setCellValue(price);
} else {
int	k =	1;
int	n =	0;
for (int i =	6; i <	6	+ receipt.getListSize() *	2; i = i +	2)	{
Row row10 =	sheet2.getRow(i);
Cell number	= row10.getCell(0);
number.setCellValue(k +	".");
k++;
Cell cell14 = row10.getCell(2);
cell14.setCellValue(receipt.getReceiptItems().get(n).getItem().getName());
Cell	cell15	=	row10.getCell(24);
cell15.setCellValue(receipt.getReceiptItems().get(n).getItem().getId());
Cell	cell16	=	row10.getCell(55);
cell16.setCellValue(receipt.getReceiptItems().get(n).getItem().getPrice());
Cell cell18= row10.getCell(73);
cell18.setCellValue(receipt.getReceiptItems().get(n).getQuantity());
Cell cell19 = row10.getCell(97);
price += receipt.getReceiptItems().get(n).getQuantity() * receipt.getReceiptItems().get(0).getItem().getPrice();
cell19.setCellValue(receipt.getReceiptItems().get(n).getQuantity() * receipt.getReceiptItems().get(O).getItem().getPrice());
n++;
Row row11 = sheet2.getRow(i +	1);
if (i <	6	+ receipt.getListSize() *	2	-	2){
int	insertRowIndexl = i +	2;
int	insertRowIndex2 = i +	3;
sheet2.shiftRows(insertRowIndex1,	sheet2.getLastRowNum(),	1);
sheet2.shiftRows(insertRowIndex2,	sheet2.getLastRowNum(),	1);
Row	newRowl = sheet2.createRow(insertRowIndex1);
Row	newRow2 = sheet2.createRow(insertRowIndex2);
copyRow(row10,	newRowl,	sheet2);
copyRow(row11,	newRow2,	sheet2);
}
if (i ==	6	+ receipt.getListSize() *	2	-	2){
Row row12 =	sheet2.getRow(i +	2);
Cell cell20	= row12.getCell(97);
cell20.setCellValue(price);
}
}
}
Sheet sheet3 = workbook.getSheetAt(2);
Row row13 =	sheet3.getRow(27);
Cell cell21	= row13.getCell(2);
cell21.setCellValue(receipt.getSupplier());
Row row14 =	sheet3.getRow(34);
Cell cell22	= row14.getCell(2);
cell22.setCellValue(today.getDayOfMonth());
Cell	cell23	=	row14.getCell(9);
cell23.setCellValue(String.format("%02d", monthNumber));
Cell	cell24	=	row14.getCell(27);
cell24.setCellValue(today.getYear());
File outputDirectory = ResourceUtils.getFile("classpath:static/documents");
if (!outputDirectory.exists()) {
outputDirectory.mkdirs();
}
String timestamp = String.valueOf(System.currentTimeMillis());
String newFileName =	,,generated_blank-torg-1_"	+ timestamp + ".xlsx";
File outputFile	=	new File(outputDirectory, newFileName);
String filePath	=	outputFile.getAbsolutePath();
OutputStream outputStream = new FileOutputStream(outputFile);
workbook.write(outputStream);
inputStream.close();
outputStream.close();
System.out.println("Файл ТОРГ-1 сгенерирован и сохранен");
return filePath;
} catch (Exception e) {
e.printStackTrace();
} return "error";
}
private void copyRow(Row sourceRow, Row targetRow, Sheet sheet) { targetRow.setHeight(sourceRow.getHeight());
for (int i = sourceRow.getFirstCellNum(); i < sourceRow.getLastCellNum(); i++)	{
Cell sourceCell = sourceRow.getCell(i);
if (sourceCell != null) {
Cell targetCell = targetRow.createCell(i);
targetCell.setCellStyle(sourceCell.getCellStyle());
targetCell.setCellType(sourceCell.getCellType());
if (sourceCell.getCellType() == CellType.STRING)	{
targetCell.setCellValue(sourceCell.getStringCellValue());
}	else	if	(sourceCell.getCellType()	==	CellType.NUMERIC)	{
targetCell.setCellValue(sourceCell.getNumericCellValue());
}	else	if	(sourceCell.getCellType()	==	CellType.BOOLEAN)	{
targetCell.setCellValue(sourceCell.getBooleanCellValue());
} else if (sourceCell.getCellType() == CellType.ERROR)	{ targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
} else if (sourceCell.getCellType() == CellType.FORMULA)	{
targetCell.setCellFormula(sourceCell.getCellFormula());
} }
}
for (int i =	0; i < sheet.getNumMergedRegions(); i++)	{
CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
if (mergedRegion.isInRange(sourceRow.getRowNum(), sourceRow.getRowNum())) { CellRangeAddress newMergedRegion = new CellRangeAddress( targetRow.getRowNum(), targetRow.getRowNum() + mergedRegion.getLastRow() -
mergedRegion.getFirstRow(),
mergedRegion.getFirstColumn(), mergedRegion.getLastColumn() );
sheet.addMergedRegion(newMergedRegion); }
}
}
@Override
public String generateTorg13(Transfer transfer) { try {
Resource resource = new ClassPathResource("static/documents/blank-torg-13.xlsx"); File file =	resource.getFile();
InputStream inputStream	= new FileInputStream(file);
Workbook workbook = new	XSSFWorkbook(inputStream);
Sheet sheet	= workbook.getSheetAt(0);
Row row = sheet.getRow(5);
CellStyle cellStyle	= workbook.createCellStyle();
cellStyle.setAlignment(HorizontalAlignment.CENTER) ;
String value =	"ООО АВТО-ПРО";
for (int column =	0; column <=	19; column++) {
Cell cell = row.createCell(column);
cell.setCellValue(value); cell.setCellStyle(cellStyle);
}
Row row2 =	sheet.getRow(10);
Cell cell2	= row2.createCell(15);
cell2.setCellValue(transfer.getId());
LocalDateTime today = LocalDateTime.now();
Month month	=	today.getMonth();
int monthNumber = month.getValue();
String date	=	today.getDayOfMonth()	+	"."	+ String.format("%02d", monthNumber) +	"."
+ today.getYear();
Row row3 =	sheet.getRow(10);
Cell cell3	= row3.createCell(17);
cell3.setCellValue(date);
Row row4 =	sheet.getRow(15);
Cell cell4	= row4.createCell(0);
cell4.setCellValue(transfer.getFromStock().getName());
Row row5 =	sheet.getRow(15);
Cell cell5	= row5.createCell(6);
cell5.setCellValue(transfer.getToStock().getName());
int n =	0;
int price =	0;
for (int i =	21; i < transfer.getListSize() +	21;	i++){
Row row6 = sheet.getRow(i);
Cell	cell6	=	row6.createCell(0);
cell6.setCellValue(transfer.getTransferItems().get(n).getItem().getName());
Row	row6_1	=	sheet.getRow(i);
Cell	cell6_1	= row6_1.createCell(5);
cell6_1.setCellValue(transfer.getTransferItems().get(n).getItem().getId());
Cell	cell7	=	row6.createCell(15);
cell7.setCellValue(transfer.getTransferItems().get(n).getQuantity());
Cell	cell8	=	row6.createCell(20);
cell8.setCellValue(transfer.getTransferItems().get(n).getItem().getPrice());
Cell	cell9	=	row6.createCell(22);
price += transfer.getTransferItems().get(n).getQuantity() * transfer.getTransferItems().get(n).getItem().getPrice();
cell9.setCellValue(transfer.getTransferItems().get(n).getQuantity() * transfer.getTransferItems().get(n).getItem().getPrice());
n++;
if (i <	21	+ transfer.getListSize() -	1){
int insertRowIndex = i +	1;
sheet.shiftRows(insertRowIndex, sheet.getLastRowNum(), 1);
Row newRow = sheet.createRow(insertRowIndex);
copyRow(row6, newRow, sheet);
}
if (i ==	21	+ transfer.getListSize() -	1){
Row row7 = sheet.getRow(i +	1);
Cell cell10 = row7.getCell(22);
cell10.setCellValue(price);
Row row8 = sheet.getRow(i +	2);
Cell cell11 = row8.getCell(22);
cell11.setCellValue(price);
}
}
File outputDirectory = ResourceUtils.getFile("classpath:static/documents");
if (!outputDirectory.exists()) { outputDirectory.mkdirs();
}
String timestamp = String.valueOf(System.currentTimeMillis());
String newFileName =	"generated_torg_13_"	+ timestamp + ".xlsx";
File outputFile	=	new File(outputDirectory, newFileName);
String filePath	=	outputFile.getAbsolutePath();
OutputStream outputStream = new FileOutputStream(outputFile);
workbook.write(outputStream);
inputStream.close();
outputStream.close();
System.out.println("Файл ТОРГ-13 сгенерирован и сохранен");
return filePath;
} catch (Exception e) {
e.printStackTrace();
} return "error";
}
@Override
public String generateNakladnaya(Receipt receipt) { try {
Resource resource = new	ClassPathResource("static/documents/blank_nakladnaya.xlsx");
File file	= resource.getFile();
InputStream inputStream	= new FileInputStream(file);
Workbook workbook = new	XSSFWorkbook(inputStream);
Sheet sheet	= workbook.getSheetAt(0);
Row row =	sheet.getRow(2);
Cell cell =	row.getCell(4);
cell.setCellValue("№"+ receipt.getId() +	" от "	+
receipt.getCreatedAt().getDayOfMonth() +	"	"	+ receipt.getCreatedAt().getMonth() +	"	"	+
receipt.getCreatedAt().getYear());
Row row2 =	sheet.getRow(4);
Cell cell2	= row2.getCell(3);
cell2.setCellValue(receipt.getSupplier());
Row row3 =	sheet.getRow(6);
Cell cell3	= row3.getCell(3);
cell3.setCellValue("000 АВТО-ПРО");
if(receipt.getListSize() ==	1){
Row row4 =	sheet.getRow(14);
Cell cell4	= row4.getCell(1);
cell4.setCellValue(1);
Cell cell5	= row4.getCell(2);
cell5.setCellValue(receipt.getReceiptItems().get(0).getItem().getName());
Cell	cell6	=	row4.getCell(9);
cell6.setCellValue(receipt.getReceiptItems().get(0).getQuantity());
Cell	cell7	=	row4.getCell(11);
cell7.setCellValue(receipt.getReceiptItems().get(0).getItem().getPrice());
} else {
int	n	=	0;
int	k	=	1;
for (int i =	14; i <	14	+ receipt.getListSize(); i++)	{
Row row4 =	sheet.getRow(i);
Cell cell4	= row4.getCell(1);
cell4.setCellValue(k);
Cell cell5	= row4.getCell(2);
cell5.setCellValue(receipt.getReceiptItems().get(n).getItem().getName());
Cell	cell6	=	row4.getCell(9);
cell6.setCellValue(receipt.getReceiptItems().get(n).getQuantity());
Cell	cell7	=	row4.getCell(11);
cell7.setCellValue(receipt.getReceiptItems().get(n).getItem().getPrice()); k++;
n++;
if (i <	14	+ receipt.getListSize() -	1){
int insertRowIndex = i +	1;
sheet.shiftRows(insertRowIndex, sheet.getLastRowNum(), 1);
Row newRow = sheet.createRow(insertRowIndex);
copyRow(row4, newRow, sheet);
}
}
}
File outputDirectory = ResourceUtils.getFile("classpath:static/documents");
if (!outputDirectory.exists()) {
outputDirectory.mkdirs();
}
String timestamp = String.valueOf(System.currentTimeMillis());
String newFileName =	"generated_nakladnaya_"	+ timestamp + ".xlsx";
File outputFile	=	new File(outputDirectory, newFileName);
String filePath	=	outputFile.getAbsolutePath();
OutputStream outputStream = new FileOutputStream(outputFile);
workbook.write(outputStream);
inputStream.close();
outputStream.close();
System.out.println("Файл Приходная накладная сгенерирован и сохранен");
return filePath;
} catch (Exception e) {
e.printStackTrace();
}
return "error";
}
}









