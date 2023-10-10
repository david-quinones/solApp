package com.solapp.common;

public class BackendValues extends JsonData {

    private String query;

    public BackendValues() {super();}

    public BackendValues(String query) {

        super();
        this.query = query;

    }

    public String getquery() {

        return query;
    }

    public void setquery(String query) {
        this.query = query;
    }
}
