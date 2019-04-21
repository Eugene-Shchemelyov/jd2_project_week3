package com.gmail.eugene.shchemelyov.chest.controller;

import com.gmail.eugene.shchemelyov.chest.service.ItemService;
import com.gmail.eugene.shchemelyov.chest.service.converter.ItemConverter;
import com.gmail.eugene.shchemelyov.chest.service.model.ItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ItemController {
    private final ItemService itemService;
    private static final Logger logger = LoggerFactory.getLogger(ItemConverter.class);

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public String findAllItems(Model model) {
        List<ItemDTO> items = itemService.getItems();
        model.addAttribute("items", items);
        logger.debug("Get items method");
        return "items";
    }

    @PostMapping("/changeStatus")
    public String changeStatus(ItemDTO itemDTO) {
        itemService.update(itemDTO.getId(), itemDTO.getStatus());
        logger.debug("Post changeStatus method");
        return "redirect:/items";
    }

    @GetMapping("/add")
    public String addItem(ItemDTO itemDTO, Model model) {
        model.addAttribute(itemDTO);
        logger.debug("Get add method");
        return "add";
    }

    @PostMapping("/add")
    public String addItem(
            @Valid ItemDTO itemDTO,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "add";
        }
        itemService.add(itemDTO);
        logger.debug("Post add method");
        return "redirect:/result";
    }

    @GetMapping("/result")
    public String showResult() {
        logger.debug("Get result method");
        return "result";
    }
}
