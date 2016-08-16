package com.github.ledoyen.automocker.examples;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.github.ledoyen.automocker.SpringAutomocker;
import com.github.ledoyen.automocker.SpringAutomockerJUnit4ClassRunner;

@RunWith(SpringAutomockerJUnit4ClassRunner.class)
@ContextConfiguration(classes = MvcApplication.class)
@SpringAutomocker
public class MvcApplicationTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void repository_is_available_and_consistent() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/create_user?firstName=Alyson&lastName=Hannigan")).andExpect(MockMvcResultMatchers.status().isOk());

		mvc.perform(MockMvcRequestBuilders.get("/create_user?firstName=Angelina&lastName=Jolie")).andExpect(MockMvcResultMatchers.status().isOk());

		mvc.perform(MockMvcRequestBuilders.get("/list_users")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("[\"Alyson Hannigan\",\"Angelina Jolie\"]"));
	}
}
