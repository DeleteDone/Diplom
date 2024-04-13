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
public class Receiptcontroller {
@Autowired
private UsersService usersService;
@Autowired
private StockItemsService stockItemsService;
@Autowired
private StocksService stocksService;
@Autowired
private ReceiptsService receiptsService;
@Autowired
private ReceiptItemsService receiptItemsService;
@GetMapping("/receipts")
public String getAllReceipts(Model model) {
List<Receipt> receipts = receiptsService.findAll();
model.addAttribute("receipts", receipts);
return "receipts";
}
@GetMapping("/receipts/{id}")
public String getReceipt(@PathVariable("id") Long id, Model model) { Optional<Receipt> optionalReceipt = receiptsService.getById(id); if (optionalReceipt.isPresent()) {
Receipt receipt = optionalReceipt.get();
model.addAttribute("receipt", receipt); return "receipt";
} else { return "error";
}
}
@GetMapping("/newReceipt")
public String getNewReceiptPage() { return "receiptForm";
}
@PostMapping("/newReceipt")
public String createReceipt(@RequestParam("stockId") Long stockId, @RequestParam("supplier") String supplier, @RequestParam("item_id[]")	Long[] itemId,
@RequestParam("item_qty[]")	Integer[] quantity) {
String auth = SecurityContextHolder.getContext().getAuthentication().getName();
Optional<User> optionalUser = usersService.findUserByEmail(auth);
if (optionalUser.isPresent()) {
Optional<Stock> optionalStock = stocksService.getById(stockId);
if (optionalStock.isPresent()) {
Receipt receipt = new Receipt();
receipt.setUser(optionalUser.get());
receipt.setStock(optionalStock.get());
receipt.setSupplier(supplier);
receipt.setCreatedAt(LocalDateTime. now());
receiptsService.save(receipt);
// если товара с id нет, то вывести ошибку добавьте товар с id, после этого выполните приемку
for (int i =	0; i < itemId.length; i++)	{
if (!stockItemsService.findByItemId(itemId[i]).isEmpty()) {
if (stockItemsService.existsStockItemByItemIdAndStockId(itemId[i], stockId)) {
System.out.println("moeap и склад уже есть");
// найти данный товар и добавить количество
StockItem stockItem =
stockItemsService.findByItemIdAndStockId(itemId[i], stockId);
stockItem.setQuantity(stockItem.getQuantity() + quantity[i]);
stockItemsService.save(stockItem);
ReceiptItem receiptItem = new ReceiptItem();
receiptItem.setItem(stockItem.getItem());
receiptItem.setQuantity(quantity[i]);
receiptltem.setReceipt(receipt);
receiptltemsService.save(receiptltem);
receipt.addReceiptltem(receiptltem);
} else {
System.out.println("moeap есть, но в др складе");
// создать новую запись, скопировав товарное id
List<StockItem> stockItemList = stockItemsService.findByItemId(itemId[i]);
Stockitem newStockItem = new Stockitem();
newStockItem.setStock(optionalStock.get());
newStockItem.setItem(stockItemList.get(0).getItem());
newStockitem.setQuantity(quantity[i]); stockitemsService.save(newStockitem);
Receiptitem receiptitem = new Receiptitem();
receiptItem.setItem(newStockItem.getitem());
receiptitem.setQuantity(newStockitem.getQuantity());
receiptitem.setReceipt(receipt);
receiptitemsService.save(receiptitem);
receipt.addReceiptitem(receiptitem); }
} else {
System.out.println("Toeapa с id #"	+ itemid[i] +	" нет в базе данных.
Прошу добавьте товар в базу данных и после повторите функцию");
}
}
receipt.setQuantity(receipt.getListSize());
receiptsService.save(receipt);
return "redirect:/receipts";
} else { return "error";
}
}
else { return "error";
}
}
}
