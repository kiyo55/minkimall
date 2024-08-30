package com.mall.common;

import lombok.Data;

//페이지 분리용
@Data
public class PaginationVo {

	private int pageSize; // 한 페이지에 표시할 레코드 수
    private int pageNumber; // 현재 페이지 번호
    
	public int getOffset() {
        return (pageNumber - 1) * pageSize;
    }
}
