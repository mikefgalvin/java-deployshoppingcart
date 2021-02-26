package com.lambdaschool.shoppingcart.services;

import com.lambdaschool.shoppingcart.ShoppingCartApplication;
import com.lambdaschool.shoppingcart.exceptions.ResourceNotFoundException;
import com.lambdaschool.shoppingcart.models.*;
import com.lambdaschool.shoppingcart.repository.CartItemRepository;
import com.lambdaschool.shoppingcart.repository.ProductRepository;
import com.lambdaschool.shoppingcart.repository.RoleRepository;
import com.lambdaschool.shoppingcart.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingCartApplication.class, properties = {"command.line.runner.enabled=false"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CartItemServiceImplTestNoDB {

    //products
    //users
    //roles
    //cartitems
    @Autowired
    private CartItemService cartItemService;

    @MockBean
    private CartItemRepository cartItemRepos;

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepos;

    @Autowired
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

        Role r1 = new Role("admin");
        Role r2 = new Role("user");

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

        User u1 = new User("admin",
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

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addToCart() throws Exception
    {

//        CartItemId workingCartItemId = new CartItemId(2L, 1L);

        CartItem workingCartItem = new CartItem(userList.get(1), productList.get(0), 1, "t");

        Mockito.when(productRepos.findById(1L))
                .thenReturn(Optional.of(productList.get(0)));

        Mockito.when(userRepos.findById(2L))
                .thenReturn(Optional.of(userList.get(1)));

        Mockito.when(cartItemRepos.findById(any(CartItemId.class)))
                .thenReturn(Optional.of(workingCartItem));

        Mockito.when(cartItemRepos.save(any(CartItem.class)))
                .thenReturn(workingCartItem);

        CartItem addedCartItem = cartItemService.addToCart(2L, 1L, "t");

        assertNotNull(addedCartItem);
        assertEquals(productList.get(0).getProductid(), addedCartItem.getProduct().getProductid());

    }

    @Test
    public void removeFromCart()
    {
        CartItem workingCartItem = new CartItem(userList.get(1), productList.get(0), 1, "Ugh");

        Mockito.when(productRepos.findById(1L))
                .thenReturn(Optional.of(productList.get(0)));

        Mockito.when(userRepos.findById(2L))
                .thenReturn(Optional.of(userList.get(1)));

        Mockito.when(cartItemRepos.findById(any(CartItemId.class)))
                .thenReturn(Optional.of(workingCartItem));

        Mockito.when(cartItemRepos.save(any(CartItem.class)))
                .thenReturn(workingCartItem);

       CartItem removeCartItem = cartItemService.removeFromCart(2L, 1L, "Ugh");

       assertNull(removeCartItem);
    }
}