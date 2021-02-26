package com.lambdaschool.shoppingcart.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.shoppingcart.ShoppingCartApplication;
import com.lambdaschool.shoppingcart.models.*;
import com.lambdaschool.shoppingcart.repository.CartItemRepository;
import com.lambdaschool.shoppingcart.repository.ProductRepository;
import com.lambdaschool.shoppingcart.repository.RoleRepository;
import com.lambdaschool.shoppingcart.repository.UserRepository;
import com.lambdaschool.shoppingcart.services.*;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ShoppingCartApplication.class, properties = {"command.line.runner.enabled=false"})
@AutoConfigureMockMvc
public class CartControllerTestNoDB {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private CartItemService cartItemService;

    @MockBean
    private CartItemRepository cartItemRepos;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductRepository productRepos;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepos;

    @MockBean
    private RoleService roleService;

    @MockBean
    private RoleRepository roleRepos;

    @MockBean
    private HelperFunctions helperFunctions;

    private List<CartItem> cartItemList;
    private List<Product> productList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();

    @Before
    public void setUp() throws Exception
    {
        cartItemList = new ArrayList<>();

        Role r1 = new Role("ADMIN");
        Role r2 = new Role("USER");

        r1.setRoleid(1);
        r2.setRoleid(2);

        Product p1 = new Product();
        p1.setProductid(1);

        p1.setName("pen");
        p1.setDescription("make words");
        p1.setPrice(2.50);
        p1.setComments("");

        productList.add(p1);

        Product p2 = new Product();
        p2.setProductid(2);

        p2.setName("pencil");
        p2.setDescription("does math");
        p2.setPrice(1.50);
        p2.setComments("");

        productList.add(p2);

        Product p3 = new Product();
        p3.setProductid(3);

        p3.setName("coffee");
        p3.setDescription("everyone needs coffee");
        p3.setPrice(4.00);
        p3.setComments("");

        productList.add(p3);

        User u1 = new User("carl",
                "admin@lambdaschool.local",
                "password",
                "");
        u1.setUserid(1);

        u1.getRoles()
                .add(new UserRoles(u1,
                        r1));
        u1.getRoles()
                .add(new UserRoles(u1,
                        r2));

        u1.getCarts()
                .add(new CartItem(u1,
                        productList.get(0),
                        4,
                        ""));

        userList.add(u1);


        User u2 = new User("barnbarn",
                "barnbarn@lambdaschool.local",
                "password",
                "");

        u2.setUserid(2);

        u2.getRoles()
                .add(new UserRoles(u2,
                        r2));

        u2.getCarts()
                .add(new CartItem(u2,
                        productList.get(0),
                        3,
                        ""));

        userList.add(u2);

        User u3 = new User("cinnamon",
                "cinnamon@lambdaschool.local",
                "password",
                "");

        u3.setUserid(3);

        u3.getRoles()
                .add(new UserRoles(u3,
                        r2));

        u3.getCarts()
                .add(new CartItem(u3,
                        productList.get(0),
                        4,
                        ""));

        userList.add(u3);

        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void listCartItemsByUserId() throws Exception
    {
        String apiURL = "/carts/user/1";
        Mockito.when(userService.findUserById(any(Long.class)))
                .thenReturn(userList.get(0));


        RequestBuilder rb = MockMvcRequestBuilders.get(apiURL)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(0));

        assertEquals(er, tr);
    }

    @Test
    public void addToCart() throws Exception
    {
        String apiURL = "/add/user/1/product/1";

        Mockito.when(productService.findProductById(1L))
                .thenReturn(productList.get(0));

        Mockito.when(userService.findUserById(2L))
                .thenReturn(userList.get(1));

//        RequestBuilder rb = MockMvcRequestBuilders.get(apiURL)
//                .accept(MediaType.APPLICATION_JSON);
//        MvcResult r = mockMvc.perform(rb)
//                .andReturn();
//        String tr = r.getResponse()
//                .getContentAsString();

        CartItem workingCartItem = cartItemService.addToCart(2L, 1L, "I hate this");

        workingCartItem.setQuantity(workingCartItem.getQuantity() + 1);

        assertNotNull(workingCartItem);
        assertEquals(productList.get(0), workingCartItem.getProduct());

    }

    @Test
    public void removeFromCart()
    {
        String apiURL = "/remove/user/1/product/1";

//        RequestBuilder rb = MockMvcRequestBuilders.get(apiURL)
//                .accept(MediaType.APPLICATION_JSON);
//        MvcResult r = mockMvc.perform(rb)
//                .andReturn();
//        String tr = r.getResponse()
//                .getContentAsString();

        CartItem workingCartItem = cartItemService.removeFromCart(2L, 1L, "Ugh");

        workingCartItem.setQuantity(workingCartItem.getQuantity() - 1);

        assertEquals(-1, workingCartItem.getQuantity());
    }
}