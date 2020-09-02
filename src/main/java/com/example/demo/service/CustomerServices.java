package com.example.demo.service;

import com.example.demo.entity.Customer;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CustomerServices {
    @Autowired
    private CustomerRepository repository;
    @Autowired
    private JavaMailSender javaMailSender;

    public List<Customer> list(){
        return (List<Customer>) repository.findAll();
    }
    public void Save(Customer customer){
        repository.save(customer);
    }
    public Customer registerCustomer(Customer customer){
        customer.setCreatedTime(new Date());
        customer.setEnabled(false);
        String randomCode = RandomString.make(64);
        customer.setVerification(randomCode);
        return repository.save(customer);
    }
    public void sendVerificationEmail(Customer customer , String siteUrl) throws UnsupportedEncodingException, MessagingException {
        String subject = "Please verify your register";
        String senderName = "Shop Mobile";
        String mailContent = "<p> Dear " + customer.getFullName() + ", </p>";
        mailContent += "<p> Please click the link below to verify to your registration </p>";
        String verifyUrl = siteUrl + "/verify?code=" + customer.getVerification();
        mailContent += "<h3><a href=\"" + verifyUrl + "\">VERIFY</a></h3>";
        mailContent += "<p>Thank you <br> The Mobile Shop</p>";
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("dong19069999@gmail.com" , senderName);
        helper.setTo(customer.getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent , true);
        javaMailSender.send(message);
    }
    public boolean verify(String verificationCode){
        Customer customer = repository.findByVeritification(verificationCode);
        if (customer == null || customer.isEnabled()){
            return false;
        }
        else {
            repository.enable(customer.getId());
            return true;
        }
    }
}







