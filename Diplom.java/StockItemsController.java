package com.example.website.controllers;
import com.example.website.models.Item;
import com.example.website.models.Stock;
import com.example.website.models.Stockitem;
import com.example.website.services.ItemsService;
import com.example.website.services.StockitemsService;
import com.example.website.services.StocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Optional;
@Controller
public class StockItemsController {
@Autowired
private StockItemsService stockItemsService;
@Autowired
private StocksService stocksService;
@Autowired
private ItemsService itemsService;
@GetMapping("/stockItems")
public String getAllStockItems(Model model){
List<StockItem> stockItems = stockItemsService.findAll();
model.addAttribute("stockItems", stockItems); return "stockItems";
}
@GetMapping("/addItem")
public String getAddItemPage() { return "addItem";
}
@PostMapping("/addItem")
private String addItemToStockItems(@RequestParam("itemId") Long itemId, @RequestParam("stockId") Long stockId, Integer quantity){
Optional<Item> optionalItem = itemsService.getById(itemId);
Optional<Stock> optionalStock = stocksService.getById(stockId);
if (optionalStock.isPresent() && optionalItem.isPresent()){
Stock stock = optionalStock.get();
Item item = optionalItem.get();
List<StockItem> stockItemList = stockItemsService.findByItemId(item.getId());
if (!stockItemList.isEmpty()){
for (int i =	0; i < stockItemList.size(); i++)	{
if (stockItemList.get(i).getStock().equals(stock)) {
stockItemList.get(i).setQuantity(stockItemList.get(i).getQuantity() + quantity);
stockItemsService.save(stockItemList.get(i)); return "redirect:/stocks/" + stockId;
} } StockItem newStockItem = new StockItem(); newStockItem.setItem(item);
newStockItem.setStock(stock);
newStockItem.setQuantity(quantity);
stockItemsService.save(newStockItem); } else {
StockItem stockItem = new StockItem();
stockItem.setItem(item);
stockItem.setStock(stock);
stockItem.setQuantity(quantity);
stockItemsService.save(stockItem); } return "redirect:/stocks/" + stockId; } else { return "error";
}
}
}
