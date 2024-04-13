package com.example.website.controllers;
import com.example.website.models.Document;
import com.example.website.models.Receipt;
import com.example.website.models.Transfer;
import com.example.website.models.WriteOff;
import com.example.website.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Controller
public class Documentcontroller {
@Autowired
private ReceiptsService receiptsService;
@Autowired
private TransfersService transfersService;
@Autowired
private WriteOffService writeOffService;
@Autowired
private ExcelGenerationService excelGenerationService;
@Autowired
private DocumentsService documentsService;
@GetMapping("/documents")
public String getAllDocuments(Model model){
List<Document> documents = documentsService.findAll();
model.addAttribute("documents", documents); return "docs";
}
@GetMapping("/documents/{id}")
public String getDocument(@PathVariable("id") Long id, Model model){ Optional<Document> optionalDocument = documentsService.getById(id); if (optionalDocument.isPresent()) {
Document document = optionalDocument.get();
model.addAttribute("document", document); return "doc";
} else { return "error";
}
}
@GetMapping("/download/{fileName}")
public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
String filePath = "static/documents/" + fileName;
Resource resource = new ClassPathResource(filePath);
if (resource.exists()) {
Path file = Paths.get(resource.getURI());
byte[] fileContent = Files.readAllBytes(file);
HttpHeaders headers = new HttpHeaders();
headers.setContentDispositionFormData("attachment", fileName);
headers.setContentType(MediaType. APPLICATION_OCTET_STRE^);
return ResponseEntity.ok() .headers(headers) .body(resource);
} else { return ResponseEntity.notFound().build();
}
}
@GetMapping("/generateTorg16/{id}")
public String generateTorg16(@PathVariable("id") Long id, Model model){ Optional<WriteOff> optionalWriteOff = writeOffService.getById(id); if (optionalWriteOff.isPresent()) {
Writeoff writeoff	=	optionalWriteOff.get();
Document	document	=	new Document();
document.setName("T0Pr-16");
document.setCreatedAt(LocalDateTime.now());
document.setOperation_id(writeOff.getId());
document.setPath(excelGenerationService.generateTorg16(writeOff)); documentsService.save(document);
model.addAttribute("writeOff", writeoff); return	"write_off";
} else { return "error";
}
}
@GetMapping("/generateTorg13/{id}")
public String generateTorg13(@PathVariable("id") Long id, Model model){ Optional<Transfer> optionalTransfer = transfersService.getById(id);
if (optionalTransfer.isPresent()) {
Transfer	transfer	=	optionalTransfer.get();
Document	document	=	new Document();
document.setName("T0Pr-13");
document.setCreatedAt(LocalDateTime.now());
document.setOperation_id(transfer.getId());
document.setPath(excelGenerationService.generateTorg13(transfer)); documentsService.save(document);
model.addAttribute("transfer", transfer); return "transfer";
} else { return "error";
}
}
@GetMapping("/generateNakladnaya/{id}")
public String generateNakladnaya(@PathVariable("id") Long id, Model model){
Optional<Receipt> optionalReceipt = receiptsService.getById(id);
if (optionalReceipt.isPresent()) {
Receipt receipt =	optionalReceipt.get();
Document document	= new Document();
document.setName("Накладная");
document.setCreatedAt(LocalDateTime.now() );
document.setOperation_id(receipt.getId());
document.setPath(excelGenerationService.generateNakladnaya(receipt));
documentsService.save(document);
model.addAttribute("receipt", receipt); return "receipt";
} else { return "error";
}
}
@GetMapping("/generateTorg1/{id}")
public String generateTorg1(@PathVariable("id") Long id, Model model){
Optional<Receipt> optionalReceipt = receiptsService.getById(id);
if (optionalReceipt.isPresent()) {
Receipt receipt =	optionalReceipt.get();
Document document	= new Document();
document.setName("T0Pr-1");
document.setCreatedAt(LocalDateTime.now());
document.setOperation_id(receipt.getId());
document.setPath(excelGenerationService.generateTorg1(receipt)); documentsService.save(document);
model.addAttribute("receipt", receipt); return "receipt";
} else { return "error";
}
}
