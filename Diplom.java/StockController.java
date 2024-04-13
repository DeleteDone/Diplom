package com.example.website.controllers;
import com.example.website.models.Stock;
import com.example.website.models.StockItem;
import com.example.website.services.ItemsService;
import com.example.website.services.StockItemsService;
import com.example.website.services.StocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import java.util.Optional;
@Controller
public class StockController {
@Autowired
private StocksService stocksService;
@Autowired
private StockItemsService stockItemsService;
@GetMapping("/stocks")
public String getAllStocks(Model model){
List<Stock> stocks = stocksService.findAll();
model.addAttribute("stocks", stocks); return "stocks";
}
@GetMapping("/stocks/{id}")
public String getStock(@PathVariable("id") Long id, Model model){ Optional<Stock> optionalStock = stocksService.getById(id);
if (optionalStock.isPresent()) {
Stock stock = optionalStock.get();
List<StockItem> stockitems = stockItemsService.findByStockId(stock.getId());
model.addAttribute("stock", stock);
model.addAttribute("stockitems", stockitems);
return "stock";
} else {
return "error";
}
}
@GetMapping("/newStock")
public String getNewStockPage() {
return "stockForm";
}
@PostMapping("/newStock")
public String createStock(Stock stock) {
stocksService.save(stock);
return "redirect:/stocks/" + stock.getid();
}
@GetMapping("/editStock/{id}")
public String editStock(@PathVariable Long id, Model model){
Optional<Stock> optionalStock = stocksService.getByid(id);
if (optionalStock.isPresent()) {
Stock stock = optionalStock.get();
model.addAttribute("stock", stock);
return "editStockForm";
} else {
System.err.println("Could not find a stock #"	+ id);
return "error";
}
}
@PostMapping ("/editStock/{id}")
public String editStock(@PathVariable Long id, Stock stock){
Optional<Stock> optionalStock = stocksService.getByid(id);
if (optionalStock.isPresent()) {
Stock editedStock = optionalStock.get();
editedStock.setName(stock.getName());
editedStock.setAddress(stock.getAddress());
stocksService.save(editedStock);
return "redirect:/stocks/" + editedStock.getid();
} else {
return "error";
}
}
@DeleteMapping("/deleteStock/{id}")
public String deleteStock(@PathVariable Long id){
Optional<Stock> optionalStock = stocksService.getByid(id);
if (optionalStock.isPresent()){
stocksService.deleteByid(id);
return "redirect:/stocks";
} else {
return "error";
}
}
}
