package com.gmail.eugene.shchemelyov.chest.controller;

import com.gmail.eugene.shchemelyov.chest.service.ItemService;
import com.gmail.eugene.shchemelyov.chest.service.model.ItemDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {
    private ItemController itemController;
    private List<ItemDTO> items = asList(
            new ItemDTO(1L, "nameOne", "READY", false),
            new ItemDTO(2L, "nameTwo", "READY", false),
            new ItemDTO(3L, "nameThree", "READY", false)
    );
    private ItemDTO itemDTO = new ItemDTO(
            1L,
            null,
            "READY",
            false
    );
    @Mock
    private ItemService itemService;
    private MockMvc mockMvc;

    @Before
    public void init() {
        itemController = new ItemController(itemService);
    }

    @Test
    public void allItemsAreAddedToModelForItemsView() {
        when(itemService.getItems()).thenReturn(items);
        Model model = new ExtendedModelMap();
        String url = itemController.findAllItems(model);
        assertThat(url, equalTo("items"));
        assertThat(model.asMap(), hasEntry("items", this.items));
    }

    @Test
    public void requestForItemsIsSuccessfullyProcessedWithAvailableItemList() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        when(itemService.getItems()).thenReturn(items);
        this.mockMvc.perform(get("/items.html"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("items", equalTo(items)))
                .andExpect(forwardedUrl("items"));
    }

    @Test
    public void requestForAddIsSuccessfullyProcessed() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        this.mockMvc.perform(get("/add.html"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("add"));
    }

    @Test
    public void requestForResultIsSuccessfullyProcessed() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        this.mockMvc.perform(get("/result.html"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("result"));
    }

    @Test
    public void shouldReturnARedirectItemsView() {
        String url = itemController.changeStatus(itemDTO);
        assertThat(url, equalTo("redirect:/items"));
    }

    @Test
    public void shouldReturnAnAddView() {
        Model model = new ExtendedModelMap();
        String url = itemController.addItem(itemDTO, model);
        assertThat(url, equalTo("add"));
    }

    @Test
    public void shouldReturnAnResultView() {
        String url = itemController.showResult();
        assertThat(url, equalTo("result"));
    }

    @Test
    public void shouldReturnARedirectResultViewWhenBindingResultHasErrorsFalse() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(itemService.add(null)).thenReturn(null);
        String url = itemController.addItem(null, bindingResult);
        assertThat(url, equalTo("redirect:/result"));
    }

    @Test
    public void shouldReturnAnAddViewWhenBindingResultHasErrorsTrue() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        String url = itemController.addItem(null, bindingResult);
        assertThat(url, equalTo("add"));
    }

    @Test
    public void shouldReturnARedirectItems() {
        when(itemService.update(1L, "READY")).thenReturn(3);
        String url = itemController.changeStatus(itemDTO);
        assertThat(url, equalTo("redirect:/items"));
    }
}
