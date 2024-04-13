package com.example.website.controllers;
import com.example.website.models.Item;
import com.example.website.services.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Controller
public class ItemController {
@Autowired
private ItemsService itemsService;
@GetMapping("/items")
public String getAllItems(Model model, @Param("keyword") String keyword) {
List<Item> items = itemsService.findAll(keyword);
model.addAttribute("items", items);
return "items";
}
@GetMapping("/items/{id}")
public String getPost(@PathVariable("id") Long id, Model model) {
Optional<Item> optionalItem = itemsService.getById(id);
if (optionalItem.isPresent()) {
Item item = optionalItem.get();
model.addAttribute("item", item);
model.addAttribute("images", item.getImages());
return "item";
} else { return "error";
}
}
@GetMapping("/newItem")
public String getNewItemPage() { return "itemForm";
}
@PostMapping("/newItem")
public String createItem(Item item, @RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2, @RequestParam("file3") MultipartFile file3) throws IOException {
item.setCreatedAt(LocalDateTime.now ());
//	item.setQuantity(O); // дефолтное значение идет 0
itemsService.save(item, file1, file2, file3);
return "redirect:/items/" + item.getId();
}
@GetMapping("/editItem/{id}")
public String editItem(@PathVariable Long id, Model model) { Optional<Item> optionalItem = itemsService.getById(id);
if (optionalItem.isPresent()) {
Item item = optionalItem.get();
model.addAttribute("item", item);
return "editItemForm";
} else {
System.err.println("Could not find a item #"	+ id);
return "error";
}
}
@PostMapping("/editItem/{id}")
public String editPost(@PathVariable Long id, Item item) { Optional<Item> optionalItem = itemsService.getById(id);
if (optionalItem.isPresent()) {
Item editedItem = optionalItem.get();
editedItem.setName(item.getName());
editedItem.setDescription(item.getDescription());
editedItem.setPrice(item.getPrice());
itemsService.save(editedItem);
return "redirect:/items/" + editedItem.getId();
} else { return "error";
}
}
@DeleteMapping("/deleteItem/{id}")
public String deleteItem(@PathVariable Long id) {
Optional<Item> optionalItem = itemsService.getById(id);
if (optionalItem.isPresent()) {
itemsService.deleteById(id);
return "redirect:/items";
} else { return "error";
}
}
}
