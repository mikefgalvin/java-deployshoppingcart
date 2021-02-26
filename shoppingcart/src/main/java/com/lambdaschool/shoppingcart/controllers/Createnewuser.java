//package com.lambdaschool.shoppingcart.controllers;
//
//import com.lambdaschool.shoppingcart.models.User;
//import com.lambdaschool.shoppingcart.models.UserMinimum;
//import com.lambdaschool.shoppingcart.models.UserRoles;
//import com.lambdaschool.shoppingcart.services.RoleService;
//import com.lambdaschool.shoppingcart.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.HashSet;
//import java.util.Set;
//
//@RestController
//public class Createnewuser {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private RoleService roleService;
//
//    @PostMapping(value = "/createnewuser",
//    consumes = {"application/json"},
//    produces = {"application/json"})
//    public ResponseEntity<?> addSelf(
//            HttpServletRequest httpServletRequest,
//            @Valid
//            @RequestBody
//                    UserMinimum newminuser)
//            throws
//            URISyntaxException
//    {
//        User newuser = new User();
//
//        newuser.setUsername(newminuser.getUsername());
//        newuser.setPassword(newminuser.getPassword());
//        newuser.setPrimaryemail(newminuser.getPrimaryemail());
//
//        Set<UserRoles> newRoles = new HashSet<>();
//        newRoles.add(new UserRoles(newuser, roleService.findByName("user")));
//        newuser.setRoles(newRoles);
//
//        newuser = userService.save(newuser);
//
//        HttpHeaders responseHeaders = new HttpHeaders();
//        URI newUserURI = ServletUriComponentsBuilder.fromUriString(httpServletRequest.getServerName() + ":" + httpServletRequest.g)
//                .buildAndExpand(newuser.getUserid())
//                .toUri();
//        responseHeaders.setLocation(newUserURI);
//    }
//}
