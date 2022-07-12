package com.sml.pump;

import com.sml.utils.common.CommonController;

/**
 * 날짜       : 2022-06-16 / 목요일 / 오후 6:59
 * 설명       :
 */


public class PumpVO extends CommonController {

	private String searchTRADE_DATE;
	private String searchKOR_NM;

	public String getSearchTRADE_DATE() {
		return searchTRADE_DATE;
	}

	public void setSearchTRADE_DATE(String searchTRADE_DATE) {
		this.searchTRADE_DATE = searchTRADE_DATE;
	}

	public String getSearchKOR_NM() {
		return searchKOR_NM;
	}

	public void setSearchKOR_NM(String searchKOR_NM) {
		this.searchKOR_NM = searchKOR_NM;
	}
}
