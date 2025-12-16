package com.zz.modules.invoicemanage.request.dto;

import lombok.Data;

@Data
public class ExtractedFileInfo {
    private String xmlFilePath;
    private String pdfFilePath;
    private String ofdFilePath;
    
    public boolean hasXml() {
        return xmlFilePath != null && !xmlFilePath.isEmpty();
    }
    
    public boolean hasPdf() {
        return pdfFilePath != null && !pdfFilePath.isEmpty();
    }
    
    public boolean hasOfd() {
        return ofdFilePath != null && !ofdFilePath.isEmpty();
    }
}