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
public class Transfercontroller {
@Autowired
private TransfersService transfersService;
@Autowired
private UsersService usersService;
@Autowired
private TransferItemsService transferItemsService;
@Autowired
private StockItemsService stockItemsService;
@Autowired
private StocksService stocksService;
@Autowired
private ItemsService itemsService;
@Autowired
private ExcelGenerationService excelGenerationService;
@GetMapping("/transfers")
public String getTransfers(Model model){
List<Transfer> transfers = transfersService.findAll();
model.addAttribute("transfers", transfers);
return "transfers";
}
@GetMapping("/transfers/{id}")
public String getTransfer(@PathVariable("id") Long id, Model model){
Optional<Transfer> optionalTransfer = transfersService.getById(id);
if (optionalTransfer.isPresent()) {
Transfer transfer = optionalTransfer.get();
model.addAttribute("transfer", transfer);
return "transfer";
} else { return "error";
}
}
@GetMapping("/newTransfer")
public String getTransferPage() {
return "transferForm";
}
@PostMapping("/newTransfer")
public String createTransfer(@RequestParam("fromStockId") Long fromStockId, @RequestParam("toStockId") Long toStockId,
@RequestParam("item_id[]") Long[] itemId, @RequestParam("item_qty[]") Integer[] quantity) {
String auth = SecurityContextHolder.getContext().getAuthentication().getName();
Optional<User> optionalUser = usersService.findUserByEmail(auth);
if (optionalUser.isPresent()) {
Optional<Stock>	optionalFromStock	= stocksService.getById(fromStockId);
Optional<Stock>	optionalToStock =	stocksService.getById(toStockId);
if (optionalFromStock.isPresent() && optionalToStock.isPresent()){
Stock fromStock =	optionalFromStock.get();
Stock toStock = optionalToStock.get();
Transfer transfer = new Transfer();
transfer.setUser(optionalUser.get());
transfer.setFromStock(fromStock);
transfer.setToStock(toStock);
transfer.setCreatedAt(LocalDateTime.now ());
transfersService.save(transfer);
for (int i =	0; i < itemId.length; i++){
Optional<Item> optionalItem = itemsService.getById(itemId[i]);
if (optionalItem.isPresent()){
// проверить что этот товар есть на from складе и его кол-во больше чем quantity
if (!stockItemsService.findByItemId(itemId[i]).isEmpty()){
if
(stockItemsService.existsStockItemByItemIdAndStockIdAndQuantityGreaterThan(itemId[i], fromStock.getId(), quantity[i])){
// проверить есть ли товар в новом складе, если нет то создать его
if
(!stockItemsService.existsStockItemByItemIdAndStockId(itemId[i], toStock.getId())){
// если товара нет в данном складе, то создаем его
StockItem stockItem = new StockItem();
stockItem.setStock(toStock);
stockItem.setItem(optionalItem.get());
stockItem.setQuantity(0);
stockItemsService.save(stockItem);
}
stockItemsService.transfer(itemId[i], fromStock.getId(), toStock.getId(), quantity[i]);
TransferItem transferItem = new TransferItem();
transferItem.setTransfer(transfer);
transferItem.setItem(optionalItem.get());
transferItem.setQuantity(quantity[i]);
transfer.addTransferItem(transferItem);
transferItemsService.save(transferItem);
} else {
System. out .println("Ko,n-BO товара с id #"	+ itemId[i] +	" на from
складе меньше, чем "	+ quantity[i] +	" или же нет в наличии");
}
} else {
System.out.println("Товар с id #"	+ itemId[i] +	" нет в наличии.");
}
} else {
System.out.println("Товара с id #"	+ itemId[i] +	" нет в базе данных.");
}
}
transfer.setQuantity(transfer.getListSize());
transfersService.save(transfer);
return "redirect:/transfers/" + transfer.getId();
} else {
return "error";
}
} else {
return "error";
}
}
}
