package com.csrcb.design.esquery;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EsSqlQuery {
    private String query;
    private Long fetch_size;
    private String cursor;

    public EsSqlQuery(String cursor) {
        this.cursor = cursor;
    }

    public EsSqlQuery(String query, long fetch_size) {
        this.query = query;
        this.fetch_size = fetch_size;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Long getFetch_size() {
        return fetch_size;
    }

    public void setFetch_size(Long fetch_size) {
        this.fetch_size = fetch_size;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
