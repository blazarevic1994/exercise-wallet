package com.playtomic.tests.wallet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.entity.TransactionsEntity;
import com.playtomic.tests.wallet.entity.WalletEntity;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
public class WalletApplicationIT {

	private Logger log = LoggerFactory.getLogger(WalletApplicationIT.class);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private static String cardId = "TEST_CARD";
	private static BigDecimal initValue = new BigDecimal(100.00);
	private static BigDecimal increment = new BigDecimal(100.00);
	private static int numWorker = 10;


	// Simple test for insert new wallet in db.
	@Test
	public void a1_createWallet() throws Exception {

		WalletEntity walletEntity = new WalletEntity();
		walletEntity.setCardId(cardId);
		walletEntity.setBalance(new BigDecimal(100.00));


		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders
						.post("/wallets")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(walletEntity)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andReturn();

		WalletEntity insertedWallet = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
		});
		assert (insertedWallet != null);
		log.info("Inserted: {}.",insertedWallet);
	}

	// Make 'numWorker' charges at the same time in 'numWorker' threads.
	// Main goal of this test is check handling concurency problems.
	// All transaction should finished successfully.
	@Test
	public void a2_TopUpWallet() throws InterruptedException {

		Runnable runnable = () -> {
			try {
				MvcResult mvcResult = mockMvc
						.perform(MockMvcRequestBuilders
								.put("/wallets/top-up/"+cardId)
								.contentType(MediaType.APPLICATION_JSON)
								.content(String.valueOf(increment)))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};

		List<Thread> threads = new ArrayList<>();
		for (int i=0; i<numWorker; i++) {
			Thread t = new Thread(runnable);
			t.start();
			threads.add(t);
		}
		for (int i=0; i<numWorker; i++) {
			threads.get(i).join();
		}
	}

	// Check balance in wallet after all trasactions.
	// We have inital state plus 'numworker' charges.
	@Test
	public void a3_getWallet() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders
						.get("/wallets/"+cardId)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		WalletEntity walletEntity = objectMapper.readValue(mvcResult.getResponse().
				getContentAsString(), new TypeReference<>() {});
		assert (walletEntity != null &&
				walletEntity.getBalance().equals(
						initValue.add(increment.multiply(new BigDecimal(numWorker)))));
		log.info("Wallet is: {}.",walletEntity);

	}

	// Check if all transaction finished successfully.
	@Test
	public void a4_getAllTransactionsForCard() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders
						.get("/wallets/transactions/"+cardId)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		List <TransactionsEntity> transactionsEntities = objectMapper.readValue(mvcResult.getResponse().
				getContentAsString(), new TypeReference<List<TransactionsEntity>>() {});

		boolean acceptedStatus = true;
		for(TransactionsEntity transactionsEntity:transactionsEntities) {
			if (transactionsEntity.getStatus() == TransactionsEntity.Status.REJECTED){
				acceptedStatus = false;
				break;
			}
		}
		assert (transactionsEntities != null &&
				acceptedStatus);

	}


}
