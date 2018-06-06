package com.kepler.tcm.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.kepler.tcm.constants.CommonConstants;

public class SysUserControllerTest extends BaseControllerTest {

	/*@Test
	//@Rollback(false)
	public void testDeleteByExample() {

	}

	@Test
	//@Rollback(false)
	public void testDeleteById() {
		try {
			 for (int i = 0; i < 1; i++) {
				 
				 MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>(); 
				 
				 form.add("id", "1");
				 MockHttpServletRequestBuilder request = delete("/user/deleteById.json")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
						//.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
						.params(form);
				 
				 ResultActions resp = mockMvc.perform(request)
				 .andExpect(status().isOk())
				 .andExpect(content().contentType("application/json;charset=UTF-8"))
				 .andExpect(content().string("1"))
				 .andDo(print()); // print
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}
	

	@Test
	//@Rollback(false)
	public void testDelete() {
		try {
			 for (int i = 0; i < 1; i++) {
				 
				 MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>(); 
				 
				 //form.add("id", "1");
				 MockHttpServletRequestBuilder request = delete("/user/delete/1.json")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
						//.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
						.params(form);
				 
				 ResultActions resp = mockMvc.perform(request)
				 .andExpect(status().isOk())
				 .andExpect(content().contentType("application/json;charset=UTF-8"))
				 .andExpect(content().string("1"))
				 .andDo(print()); // print
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}*/

	@Test
	//@Rollback(false)
	public void testInsertSelective() {
		
		 try {
			 for (int i = 0; i < 1; i++) {
				 MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>(); 
				 
				 form.add("1", "4CDCDCEB4109CF4AE05010AC1D0E7785");
				 MockHttpServletRequestBuilder request = post("/interface/querySqlList.json")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
						//.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
						.params(form);
				 
				 ResultActions resp = mockMvc.perform(request)
				 .andExpect(status().isOk())
				 .andExpect(content().contentType("application/json;charset=UTF-8"))
				 .andExpect(content().string("1"))
				 .andDo(print()); // print
			}
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}

	/*@Test
	public void testSelectById() {
		try {
			 for (int i = 0; i < 1; i++) {
				 
				 MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>(); 
				 
				 form.add("id", "1");

				 MockHttpServletRequestBuilder request = get("/user/selectById.json")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
						//.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
						.params(form);
				 
				 ResultActions resp = mockMvc.perform(request)
				 .andExpect(status().isOk())
				 .andExpect(content().contentType("application/json;charset=UTF-8"))
				 //.andExpect(content().string("1"))
				 .andDo(print()); // print
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}

	@Test
	public void testSelect() {
		try {
			 for (int i = 0; i < 1; i++) {
				 
				 MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>(); 
				 
				 //form.add("id", "1");

				 MockHttpServletRequestBuilder request = get("/user/select/1.json")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
						//.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
						.params(form);
				 
				 ResultActions resp = mockMvc.perform(request)
				 .andExpect(status().isOk())
				 .andExpect(content().contentType("application/json;charset=UTF-8"))
				 //.andExpect(content().string("1"))
				 .andDo(print()); // print
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}

	@Test
	//@Rollback(false)
	public void testUpdateById() {
		try {
			 for (int i = 0; i < 1; i++) {
				 
				 MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>(); 
				 
				 form.add("id", "1");
				 form.add("password", "admi123");
				 MockHttpServletRequestBuilder request = put("/user/updateById.json")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
						//.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
						.params(form);
				 
				 ResultActions resp = mockMvc.perform(request)
				 .andExpect(status().isOk())
				 .andExpect(content().contentType("application/json;charset=UTF-8"))
				 //.andExpect(content().string("1"))
				 .andDo(print()); // print
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}

	@Test
	//@Rollback(false)
	public void testUpdate() {
		try {
			 for (int i = 0; i < 1; i++) {
				 
				 MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>(); 
				 
				 //form.add("id", "1");
				 form.add("password", "admi123");
				 MockHttpServletRequestBuilder request = put("/user/update/1.json")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
						//.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
						.params(form);
				 
				 ResultActions resp = mockMvc.perform(request)
				 .andExpect(status().isOk())
				 .andExpect(content().contentType("application/json;charset=UTF-8"))
				 .andExpect(content().string("1"))
				 .andDo(print()); // print
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}

	@Test
	public void testQueryPageIntIntSysUserString() {
		try {
			 for (int i = 0; i < 3; i++) {
				 
				 MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>(); 
				 
				 form.add("pageNum", "1");
				 form.add("pageSize", "8");
				 form.add("loginName", "bonc");
				 form.add("orderBy", "createDate desc");
				 
				 MockHttpServletRequestBuilder request = get("/user/query.json")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
						//.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
						.params(form);
				 
				 ResultActions resp = mockMvc.perform(request)
				 .andExpect(status().isOk())
				 .andExpect(content().contentType("application/json;charset=UTF-8"))
				 //.andExpect(content().string("1"))
				 .andDo(print()); // print
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}

	@Test
	public void testQueryPageIntIntSysUserSysUserExampleString() {
		try {
			 for (int i = 0; i < 3; i++) {
				 
				 MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>(); 
				 
				 form.add("pageNum", "1");
				 form.add("pageSize", "8");
				 //form.add("loginName", "bonc");
				 form.add("orderBy", "createDate desc");
				 
				 MockHttpServletRequestBuilder request = get("/user/search.json")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
						//.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
						.params(form);
				 
				 ResultActions resp = mockMvc.perform(request)
				 .andExpect(status().isOk())
				 .andExpect(content().contentType("application/json;charset=UTF-8"))
				 //.andExpect(content().string("1"))
				 .andDo(print()); // print
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}*/

}
