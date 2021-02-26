package com.lambdaschool.shoppingcart.controllers;

import com.lambdaschool.shoppingcart.ShoppingCartApplication;
import com.lambdaschool.shoppingcart.models.CartItem;
import com.lambdaschool.shoppingcart.models.Product;
import com.lambdaschool.shoppingcart.models.User;
import com.lambdaschool.shoppingcart.repository.CartItemRepository;
import com.lambdaschool.shoppingcart.repository.ProductRepository;
import com.lambdaschool.shoppingcart.repository.RoleRepository;
import com.lambdaschool.shoppingcart.repository.UserRepository;
import com.lambdaschool.shoppingcart.services.*;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ShoppingCartApplication.class)
@AutoConfigureMockMvc
@WithUserDetails(value = "barnbarn")
public class CartControllerTestWithDB
{
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


    @Before
    public void setUp() throws Exception
    {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        List<Product> myList = productService.findAll();

        for (Product p : myList)
        {
            System.out.println("Product id: " + p.getProductid() + "Product name: " + p.getName());
        }
        System.out.println();

        List<User> yourList = userService.findAll();

        for (User u : yourList)
        {
            System.out.println("User id: " + u.getUserid() + "User name: " + u.getUsername());
        }
        System.out.println();

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void listCartItemsByUserId() throws Exception
    {
        long aUser = 1;

         given().when()
                .get("/user/" + aUser)
                 .then()
                 .statusCode(200)
                 .and()
                 .body(containsString("Pen"));
    }

    @Test
    public void addToCart()
    {
//        long aUser = 2;
//        long aProduct = 1;
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/add/user/" + aUser + "product/" + aProduct)
//                .content(jsonInput)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk());
    }

    @Test
    public void removeFromCart()
    {
        long aUser = 2;
        long aProduct = 1;

        given().when()
                .delete("/add/user/" + aUser + "product/" + aProduct)
                .then()
                .statusCode(200);
    }
}