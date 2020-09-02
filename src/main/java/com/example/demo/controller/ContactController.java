package com.example.demo.controller;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Utility;
import com.example.demo.service.CustomerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class ContactController {
    @Autowired
    JavaMailSender javaMailSender;
    @GetMapping("/contact")
    public String showContactForm() {
        return "contact_form";
    }
    @Autowired
    private CustomerServices services;
    @RequestMapping("/")
    public String ShowCustomer(Model model){
        List<Customer> customers = services.list();
        model.addAttribute("customer" , customers);
        return "listcustomer";
    }
    @RequestMapping(value = "/register")
    public ModelAndView ShowCustomerRegistration(Model model){
        ModelAndView modelAndView = new ModelAndView("form_register");
        model.addAttribute("customer" , new Customer());
        model.addAttribute("pageTitle" , "Customer Registration");
        return modelAndView;
    }
    @RequestMapping(value = "/createCustomer" , method = RequestMethod.POST)
    public String createCustomer(@ModelAttribute("customer") Customer customer
            , Model model , HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        services.registerCustomer(customer);
        String siteUrl = Utility.getSiteUrl(request);
        services.sendVerificationEmail(customer , siteUrl);
        model.addAttribute("pageTitle" , "Register Success");
        return "register_success";
    }
    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code , Model model){
        boolean verified = services.verify(code);
        String pageTitle = verified ? "Verification Successed !" : "Verification Failed";
        model.addAttribute("pageTitle" , pageTitle);
        return (verified ? "verify_success" : "verify_fail");
    }
}
