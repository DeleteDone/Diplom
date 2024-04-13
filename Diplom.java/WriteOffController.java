package com.example.website.controllers;
import com.example.website.models.*;
import com.example.website.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Controller
public class WriteOffController {
@Autowired
private DocumentsService documentsService;
@Autowired
private UsersService usersService;
@Autowired
private ExcelGenerationService excelGenerationService;
@Autowired
private WriteOffService writeOffService;
@Autowired
private WriteOffitemsService writeOffitemsService;
@Autowired
private StockItemsService stockItemsService;
@Autowired
private StocksService stocksService;
@Autowired
private ItemsService itemsService;
@GetMapping("/writeOffs")
public String getAllWriteOffs(Model model){
List<WriteOff> writeoffs = writeOffService.findAll();
model.addAttribute("writeOffs", writeOffs);
return	"write_offs";
}	”
@GetMapping("/writeOffs/{id}")
public String getWriteOff(@PathVariable("id") Long id, Model model){
Optional<WriteOff> optionalWriteOff = writeOffService.getById(id);
if (optionalWriteOff.isPresent()) {
WriteOff writeOff = optionalWriteOff.get();
model.addAttribute("writeOff", writeOff); return	"write_off";
} else { return "error";
}
}
@GetMapping("/newWriteOff")
public String getNewWriteOffPage() { return "writeOffForm";
}
@PostMapping("/newWriteOff")
public String	createWriteOff(@RequestParam("item_id[]")	Long[] itemId,
@RequestParam("item_qty[]")	Integer[] quantity,
@RequestParam("stockId") Long stockId) {
String auth = SecurityContextHolder.getContext().getAuthentication().getName();
Optional<User> optionalUser = usersService.findUserByEmail(auth);
if (optionalUser.isPresent()) {
Optional<Stock> optionalStock = stocksService.getById(stockId);
if (optionalStock.isPresent()) {
Stock stock = optionalStock.get();
WriteOff writeOff = new WriteOff();
writeOff.setUser(optionalUser.get());
writeOff.setStock(stock);
writeOff.setCreatedAt(LocalDateTime.now ());
writeOffService.save(writeOff);
for (int i =	0; i < itemId.length; i++)	{
Optional<Item> optionalItem = itemsService.getById(itemId[i]);
if (optionalItem.isPresent()) {
// проверить что этот товар есть на данном складе и его кол-во больше чем quantity
if
(stockItemsService.existsStockItemByItemIdAndStockIdAndQuantityGreaterThan(itemId[i], stock.getId(), quantity[i])) {
stockItemsService.writeOff(itemId[i], stock.getId(), quantity[i]);
WriteOffItem writeOffItem = new WriteOffItem();
writeOffItem.setWriteOff(writeOff);
writeOffItem.setItem(optionalItem.get());
writeOffItem.setQuantity(quantity[i]);
writeOffItemsService.save(writeOffItem);
writeOff.addWriteOffItem(writeOffItem);
} else {
System.out.println("Toeapa нет на складе, либо его количество меньше quantity");
}
} else {
System. out .printlnC’^eap с id #"	+ itemId[i] +	" не существует.");
}
}
writeOff.setQuantity(writeOff.getListSize());
writeOffService.save(writeOff);
return "redirect:/writeOffs/" + writeOff.getId();
} else {
System. out .println("C^ag не найден.");
}
} else {
return "error";
}
return "error";
}
}
