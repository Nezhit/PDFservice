package com.example.CodeInside.pojo;

import org.antlr.v4.runtime.misc.NotNull;

public class MarkDTO {

    Long docId;
    int currentPage;

    public MarkDTO() {
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
