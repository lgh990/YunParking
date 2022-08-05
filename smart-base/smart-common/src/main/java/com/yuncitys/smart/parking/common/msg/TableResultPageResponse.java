package com.yuncitys.smart.parking.common.msg;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author smart
 * @version 2022-06-14 22:40
 */
public class TableResultPageResponse<T> extends BaseResponse {

    TableData<T> data;

    public TableResultPageResponse(long total, List<T> rows,long offset,long limit) {
        this.data = new TableData<T>(total, rows,offset,limit);
    }

    public TableResultPageResponse() {
        this.data = new TableData<T>();
    }

    TableResultPageResponse<T> total(int total) {
        this.data.setTotal(total);
        return this;
    }

    TableResultPageResponse<T> total(List<T> rows) {
        this.data.setRows(rows);
        return this;
    }

    public TableData<T> getData() {
        return data;
    }

    public void setData(TableData<T> data) {
        this.data = data;
    }

    public class TableData<T> {
        long offset;
        long limit;
        long total;
        List<T> rows;

        public TableData(long total, List<T> rows,long offset,long limit) {
            this.total = total;
            this.rows = rows;
            this.offset = offset;
            this.limit = limit;
        }

        public TableData() {
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public List<T> getRows() {
            return rows;
        }

        public void setRows(List<T> rows) {
            this.rows = rows;
        }

        public long getOffset() {
            return offset;
        }

        public void setOffset(long offset) {
            this.offset = offset;
        }

        public long getLimit() {
            return limit;
        }

        public void setLimit(long limit) {
            this.limit = limit;
        }
    }
    public TableResultPageResponse BaseResponse(int status, String message) {
        this.setMessage(message);
        this.setStatus(status);
        return this;
    }

}
