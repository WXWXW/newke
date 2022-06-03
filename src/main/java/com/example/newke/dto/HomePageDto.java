package com.example.newke.dto;

import lombok.Data;


@Data
public class HomePageDto {

    private Integer rows;

    private String path;

    private Integer current =1;

    private Integer limit = 10;


    public Integer getTotal() {
        // rows / limit [+1]
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }


            /**
             * 获取起始页码
             *
             * @return
             */
    public Integer getFrom() {
        Integer from = current - 2;
        return from < 1 ? 1 : from;
    }

    /**
     * 获取结束页码
     *
     * @return
     */
    public Integer getTo() {
        Integer to = current + 2;
        Integer total = getTotal();
        return to > total ? total : to;
    }


    @Override
    public String toString() {
        return "HomePageDto{" +
                "rows=" + rows +
                ", path='" + path + '\'' +
                ", current=" + current +
                ", limit=" + limit +
                '}';
    }
}
