package com.yumfee.extremeworld.rest.dto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springside.modules.mapper.BeanMapper;

public class MyPage<T,E> {

	private int curPage;
	private int totalPages;
	private Long totalElements;
	private List<T> contents;
	
	public MyPage(){
		
	}
	
	public MyPage(Class<T> destinationClass, Page<E> page){
		this.curPage = page.getNumber();
		this.totalPages = page.getTotalPages();
		this.totalElements = page.getTotalElements();
		//Entity-->DTO
		this.contents = BeanMapper.mapList(page.getContent(),destinationClass);
	}
	
	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public List<T> getContents() {
		return contents;
	}
	public void setContents(List<T> contents) {
		this.contents = contents;
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}
	
	
	
}
