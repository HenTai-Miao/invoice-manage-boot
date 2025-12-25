package com.zz.modules.invoicemanage.request.dto;

import lombok.Data;

import java.util.Map;

@Data
public class FileGroupInfo {
    private String baseName;
    private String xmlFilePath;
    private String pdfFilePath;
    private String ofdFilePath;
    
    // 存储原始文件名的映射，key为文件类型(xml/pdf/ofd)，value为原始文件名
    private Map<String, String> originalFileNames;
    
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
     * 获取XML文件的原始文件名
     */
    public String getXmlOriginalFileName() {
        return originalFileNames != null ? originalFileNames.get("xml") : null;
    }
    
    /**
     * 获取PDF文件的原始文件名
     */
    public String getPdfOriginalFileName() {
        return originalFileNames != null ? originalFileNames.get("pdf") : null;
    }
    
    /**
     * 获取OFD文件的原始文件名
     */
    public String getOfdOriginalFileName() {
        return originalFileNames != null ? originalFileNames.get("ofd") : null;
    }
    
    /**
     * Check if this group is valid (must have XML file at minimum)
     * @return true if the group has an XML file, false otherwise
     */
    public boolean isValid() {
        return hasXml();
    }
}