package com.sx.drainage.common;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/10/28
 * Time: 15:04
 */
@Component
public class WordToPdf {
    /**
     * 将doc格式文件转成html
     *
     * @param docPath  doc文件路径
     * @param imageDir doc文件中图片存储目录
     * @return html
     */

    public static String doc2Html(String docPath, String imageDir) {
        String content = null;
        ByteArrayOutputStream baos = null;
        try {
            HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(docPath));
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            wordToHtmlConverter.setPicturesManager(new PicturesManager() {
                @Override
                public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
                    File file = new File(imageDir + suggestedName);
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file);
                        fos.write(content);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return imageDir + suggestedName;
                }
            });
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();
            DOMSource domSource = new DOMSource(htmlDocument);
            baos = new ByteArrayOutputStream();
            StreamResult streamResult = new StreamResult(baos);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    content = new String(baos.toByteArray(), "utf-8");
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 将docx格式文件转成html
     *
     * @param docxPath docx文件路径
     * @param imageDir docx文件中图片存储目录
     * @return html
     */
    public static String docx2Html(String docxPath, String imageDir) {
        String content = null;

        FileInputStream in = null;
        ByteArrayOutputStream baos = null;
        try {
            // 1> 加载文档到XWPFDocument
            in = new FileInputStream(new File(docxPath));
            XWPFDocument document = new XWPFDocument(in);
            // 2> 解析XHTML配置（这里设置IURIResolver来设置图片存放的目录）
            XHTMLOptions options = XHTMLOptions.create();
            // 存放word中图片的目录
            options.setExtractor(new FileImageExtractor(new File(imageDir)));
            options.URIResolver(new BasicURIResolver(imageDir));
            options.setIgnoreStylesIfUnused(false);
            options.setFragment(true);
            // 3> 将XWPFDocument转换成XHTML
            baos = new ByteArrayOutputStream();
            XHTMLConverter.getInstance().convert(document, baos, options);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (baos != null) {
                    content = new String(baos.toByteArray(), "utf-8");
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 使用jsoup规范化html
     *
     * @param html html内容
     * @return 规范化后的html
     */
    private static String formatHtml(String html) {
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        // 去除过大的宽度
        String style = doc.attr("style");
        if (StringUtils.isNotEmpty(style) && style.contains("width")) {
            doc.attr("style", "");
        }
        Elements divs = doc.select("div");
        for (Element div : divs) {
            String divStyle = div.attr("style");
            if (StringUtils.isNotEmpty(divStyle) && divStyle.contains("width")) {
                div.attr("style", "");
            }
        }
        // jsoup生成闭合标签
        doc.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        return doc.html();
    }


    /**
     * html转成pdf
     *
     * @param html          html
     * @param outputPdfPath 输出pdf路径
     */
    private static void htmlToPdf(String html, String outputPdfPath) {
        com.itextpdf.text.Document document = null;
        try {
            // 纸
            document = new com.itextpdf.text.Document(PageSize.A4);
            // 笔
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPdfPath));
            document.open();
            // html转pdf
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(html.getBytes()),
                    Charset.forName("UTF-8"), new FontProvider() {
                        @Override
                        public boolean isRegistered(String s) {
                            return false;
                        }

                        @Override
                        public Font getFont(String s, String s1, boolean embedded, float size, int style, BaseColor baseColor) {
                            // 配置字体
                            Font font = null;
                            try {
                                // 方案一：使用本地字体(本地需要有字体)
//                              BaseFont bf = BaseFont.createFont("c:/Windows/Fonts/simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                                // 方案二：使用jar包：iTextAsian，这样只需一个jar包就可以了
                                BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
                                font = new Font(bf, size, style, baseColor);
                                font.setColor(baseColor);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return font;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    private static byte[] htmlToPdfStream(String html) {
        byte[] result=null;
        com.itextpdf.text.Document document = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//构建字节输出流
        try {
            // 纸
            document = new com.itextpdf.text.Document(PageSize.A4);
            // 笔
            PdfWriter writer = PdfWriter.getInstance(document,baos);
            document.open();
            // html转pdf
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(html.getBytes()),
                    Charset.forName("UTF-8"), new FontProvider() {
                        @Override
                        public boolean isRegistered(String s) {
                            return false;
                        }

                        @Override
                        public Font getFont(String s, String s1, boolean embedded, float size, int style, BaseColor baseColor) {
                            // 配置字体
                            Font font = null;
                            try {
                                // 方案一：使用本地字体(本地需要有字体)
//                              BaseFont bf = BaseFont.createFont("c:/Windows/Fonts/simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                                // 方案二：使用jar包：iTextAsian，这样只需一个jar包就可以了
                                BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
                                font = new Font(bf, size, style, baseColor);
                                font.setColor(baseColor);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return font;
                        }
                    });
            writer.close();
            result=baos.toByteArray();
//            result =new String(baos.toByteArray(), "ISO-8859-1");//转字符串设置编码
//            result = java.net.URLEncoder.encode(result, "ISO-8859-1");//如果跨域需设置编码
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
        }
        return result;
    }
    /*
    * doc文件转pdf流
    * @params filePath 文件全路径 f:/文件/test.doc
    * @params imageUrl 文件包含图片存储路径 f:/文件/images/
    * */
    public static byte[] docToPdfStream(String filePath,String imageUrl){
        String docHtml = doc2Html(filePath, imageUrl);
        docHtml = formatHtml(docHtml);
        byte[] bytes = htmlToPdfStream(docHtml);
        return bytes;
    }
    /*
    * docx文件转pdf流
    * */
    public static byte[] docxToPdfStream(String filePath,String imageUrl){
        String docxHtml = docx2Html(filePath, imageUrl);
        docxHtml = formatHtml(docxHtml);
        byte[] bytes = htmlToPdfStream(docxHtml);
        return bytes;
    }
    /*
     * doc文件转pdf流
     * @params filePath 文件全路径 f:/文件/test.doc
     * @params imageUrl 文件包含图片存储路径 f:/文件/images/
     * @params pdfUrl 生成文件全路径 f:/文件/test.pdf
     * */
    public static void docToPdf(String filePath,String imageUrl,String pdfUrl){
        String docHtml = doc2Html(filePath, imageUrl);
        docHtml = formatHtml(docHtml);
        htmlToPdf(docHtml,pdfUrl);
    }
    /*
    * docx文件转pdf
    * */
    public static void docxToPdf(String filePath,String imageUrl,String pdfUrl){
        String docxHtml = docx2Html(filePath, imageUrl);
        UUID uuid = UUID.randomUUID();
        docxHtml = formatHtml(docxHtml);
        htmlToPdf(docxHtml,pdfUrl);
    }
}
