package com.zz.modules.invoicemanage.request.dto;

import com.zz.common.constant.InvoiceInfoConstant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电子发票XML解析工具类
 */
public class EInvoiceParser {

    /**
     * 一次性提取XML中的多个指定标签
     * 
     * @param xmlContent XML内容
     * @return 包含标签名称和对应值的映射
     * @throws Exception 解析异常
     */
    public static Map<String, String> extractMultipleTags(String xmlContent) throws Exception {
        Map<String, String> RMap = new HashMap<>();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlContent)));
        // 获取根元素
        Element root = document.getDocumentElement();
        
        // 遍历所有需要提取的标签
        for (String tagName : InvoiceInfoConstant.getAllInvoiceInfoConstants()) {
            NodeList nodes = root.getElementsByTagName(tagName);
            if (nodes.getLength() > 0) {
                RMap.put(tagName, nodes.item(0).getTextContent());
            } else {
                RMap.put(tagName, null);
            }
        }
        
        return RMap;
    }
    
    /**
     * 提取所有的IssuItemInformation项目信息
     * 
     * @param xmlContent XML内容
     * @return 包含所有项目信息的列表
     * @throws Exception 解析异常
     */
    public static List<Map<String, String>> extractAllItems(String xmlContent) throws Exception {
        List<Map<String, String>> items = new ArrayList<>();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlContent)));
        // 获取根元素
        Element root = document.getDocumentElement();
        
        // 获取所有的Item节点
        NodeList itemNodes = root.getElementsByTagName("IssuItemInformation");
        
        // 遍历每个Item节点
        for (int i = 0; i < itemNodes.getLength(); i++) {
            Node itemNode = itemNodes.item(i);
            if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) itemNode;
                Map<String, String> itemMap = new HashMap<>();
                
                // 遍历所有需要提取的项目标签
                for (String tagName : InvoiceInfoConstant.getIssuItemInformation()) {
                    NodeList nodes = itemElement.getElementsByTagName(tagName);
                    if (nodes.getLength() > 0) {
                        itemMap.put(tagName, nodes.item(0).getTextContent());
                    } else {
                        itemMap.put(tagName, null);
                    }
                }
                
                items.add(itemMap);
            }
        }
        return items;
    }
    
    /**
     * 从XML中获取InherentLabel元素
     * 
     * @param xmlContent XML内容
     * @return InherentLabel元素或null（如果未找到）
     * @throws Exception 解析异常
     */
    private static Element getInherentLabelElement(String xmlContent) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlContent)));
        Element root = document.getDocumentElement();
        
        NodeList inherentLabelNodes = root.getElementsByTagName("InherentLabel");
        if (inherentLabelNodes.getLength() > 0) {
            return (Element) inherentLabelNodes.item(0);
        }
        return null;
    }
    
    /**
     * 解析电子发票类型标签
     * 
     * @return EInvoiceType枚举值
     * @throws Exception 解析异常
     */
    public static String parseEInvoiceType(Element inherentLabelElement) throws Exception {
        if (inherentLabelElement != null) {
            NodeList eInvoiceTypeNodes = inherentLabelElement.getElementsByTagName(InvoiceInfoConstant.E_INVOICE_TYPE);
            if (eInvoiceTypeNodes.getLength() > 0) {
                Element eInvoiceTypeElement = (Element) eInvoiceTypeNodes.item(0);
                NodeList labelCodeNodes = eInvoiceTypeElement.getElementsByTagName("LabelName");
                if (labelCodeNodes.getLength() > 0) {
                    return labelCodeNodes.item(0).getTextContent();
                }
            }
        }
        return null;
    }
    
    /**
     * 解析普通发票或专用发票类型标签
     * 
     * @return GeneralOrSpecialVAT枚举值
     * @throws Exception 解析异常
     */
    public static String parseGeneralOrSpecialVAT(Element inherentLabelElement) throws Exception {
        if (inherentLabelElement != null) {
            NodeList generalOrSpecialVATNodes = inherentLabelElement.getElementsByTagName(InvoiceInfoConstant.GENERAL_OR_SPECIAL_V_A_T);
            if (generalOrSpecialVATNodes.getLength() > 0) {
                Element generalOrSpecialVATElement = (Element) generalOrSpecialVATNodes.item(0);
                NodeList labelCodeNodes = generalOrSpecialVATElement.getElementsByTagName("LabelName");
                if (labelCodeNodes.getLength() > 0) {
                    return labelCodeNodes.item(0).getTextContent();
                }
            }
        }
        return null;
    }
    
    /**
     * 解析纳税人类型标签
     * 
     * @return TaxpayerType枚举值
     * @throws Exception 解析异常
     */
    public static String parseTaxpayerType(Element inherentLabelElement) throws Exception {
        if (inherentLabelElement != null) {
            NodeList taxpayerTypeNodes = inherentLabelElement.getElementsByTagName(InvoiceInfoConstant.TAXPAYER_TYPE);
            if (taxpayerTypeNodes.getLength() > 0) {
                Element taxpayerTypeElement = (Element) taxpayerTypeNodes.item(0);
                NodeList labelCodeNodes = taxpayerTypeElement.getElementsByTagName("LabelName");
                if (labelCodeNodes.getLength() > 0) {
                    return labelCodeNodes.item(0).getTextContent();
                }
            }
        }
        return null;
    }
    
    /**
     * 统一解析所有发票相关枚举类型
     * 
     * @param xmlContent XML内容
     * @return 包含所有发票相关枚举类型的映射
     * @throws Exception 解析异常
     */
    public static Map<String, String> parseInvoiceEnums(String xmlContent) throws Exception {
        Map<String, String> enumMap = new HashMap<>();
        Element inherentLabelElement = getInherentLabelElement(xmlContent);
        // 解析电子发票类型
        String eInvoiceType = parseEInvoiceType(inherentLabelElement);
        enumMap.put(InvoiceInfoConstant.E_INVOICE_TYPE, eInvoiceType);
        // 解析普通发票或专用发票类型
        String vatType = parseGeneralOrSpecialVAT(inherentLabelElement);
        enumMap.put(InvoiceInfoConstant.GENERAL_OR_SPECIAL_V_A_T, vatType);
        // 解析纳税人类型
        String taxpayerType = parseTaxpayerType(inherentLabelElement);
        enumMap.put(InvoiceInfoConstant.TAXPAYER_TYPE, taxpayerType);
        return enumMap;
    }
}