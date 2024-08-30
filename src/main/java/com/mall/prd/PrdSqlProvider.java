package com.mall.prd;

import java.util.Map;

public class PrdSqlProvider {
	
	public String buildSeachPrdQuery(Map<String, Object> params) {
        String searchCdt = (String) params.get("searchCdt");
        String searchKw = (String) params.get("searchKw");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM products WHERE 1=1 ");

        if (searchCdt != null && searchKw != null && !searchCdt.isEmpty() && !searchKw.isEmpty()) {
            query.append("AND ").append(searchCdt)
                 .append(" LIKE CONCAT('%', #{searchKw}, '%') ");
        }

        query.append("ORDER BY product_code DESC");
        return query.toString();
    }

}
