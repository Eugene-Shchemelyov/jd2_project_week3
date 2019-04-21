package com.gmail.eugene.shchemelyov.chest.controller.app;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gmail.eugene.shchemelyov.chest.service.ItemService;
import com.gmail.eugene.shchemelyov.chest.service.model.ItemDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemService.class)
@ActiveProfiles("test")
public class ItemControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private WebClient webClient;
    @MockBean
    private ItemService itemService;
    private List<ItemDTO> items = asList(
            new ItemDTO(1L, "nameOne", "READY", false),
            new ItemDTO(2L, "nameTwo", "READY", false)
    );

    @Before
    public void init() {
        when(itemService.getItems()).thenReturn(items);
        webClient = MockMvcWebClientBuilder.mockMvcSetup(mockMvc)
                .useMockMvcForHosts("app.com")
                .build();
    }

    @Test
    public void requestForItemsIsSuccessfullyProcessedWithAvailableItemsList() throws Exception {
        this.mockMvc.perform(get("/items").accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(allOf(
                        containsString("nameOne"),
                        containsString("nameTwo")))
                );
    }

    @Test
    public void itemsPageContentIsRenderedAsHtmlWithListOfItems() throws IOException {
        HtmlPage page = webClient.getPage("http://app.com/items");
        List<String> booksList = page.getElementsByTagName("li")
                .stream().map(DomNode::asText).collect(toList());
        assertThat(booksList, hasItems("1 nameOne", "2 nameTwo"));
    }
}
