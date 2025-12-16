package com.zz.modules.invoicemanage.request.dto;

import lombok.Data;

@Data
public class FileGroupInfo {
    private String baseName;
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
    
    /**
     * Check if this group is valid (must have XML file at minimum)
     * @return true if the group has an XML file, false otherwise
     */
    public boolean isValid() {
        return hasXml();
    }
}