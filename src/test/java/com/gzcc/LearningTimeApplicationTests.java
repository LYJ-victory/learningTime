package com.gzcc;

import com.gzcc.config.properties.MailProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LearningTimeApplicationTests {

//	@Test
//	public void contextLoads() {
//	}
//	@Autowired
//	private WebApplicationContext webApplicationContext;
//	private MockMvc mockMvc;
//	@Before
//	public void setup(){
//		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//	}
////	@Test
//	public void whenSuccess()throws Exception{
//		String result = mockMvc.perform(MockMvcRequestBuilders.get("/student/1")
//						.contentType(MediaType.APPLICATION_JSON_UTF8))
//						.andExpect(MockMvcResultMatchers.status().isOk())
//						.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(6))
//						.andReturn().getResponse().getContentAsString();
//		System.out.println(result);
//	}
////	@Test
//	public void whenFail() throws Exception {
//		String result=mockMvc.perform(MockMvcRequestBuilders.get("/student/")
//				.contentType(MediaType.APPLICATION_JSON_UTF8))
//				.andExpect(MockMvcResultMatchers.status().is4xxClientError())
//				.andReturn().getResponse().getContentAsString();
//		System.out.println(result);
//	}
//	@Autowired
//	private JavaMailSender javaMailSender;
//	@Autowired
//	private MailProperties mailProperties;
//	@Test
//	public void sendEmail(){
//		SimpleMailMessage mailMessage = new SimpleMailMessage();
//		mailMessage.setFrom(mailProperties.getFrom());
//		mailMessage.setTo(mailProperties.getTo());
//
//		mailMessage.setSubject("gzcc提醒");
//		mailMessage.setText("注册成功");
//
//		javaMailSender.send(mailMessage);
//		System.out.println("成功");
//	}
//	@Test
//	public void register(){
////		String result = mockMvc.perform(MockMvcRequestBuilders.post())
//	}

}
