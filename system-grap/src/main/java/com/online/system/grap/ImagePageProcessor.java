package com.online.system.grap;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.io.Files;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 模拟图片爬虫
 */
@Component("imagePageProcessor")
public class ImagePageProcessor extends WebCrawler {

    //爬虫的地址
    private static final List<String> URL_PATTERNS = new ArrayList<>();

    //线程数
    private static final int THREAD_CRAWLERS = 7;

    private static final String ROOT_FOLDER = "D:\\Program Files\\download\\data"; // 定义爬虫数据存储位置
    private static final String STORAGE_FOLDER = "D:\\Program Files\\download\\resource"; // 定义爬取的图片本地存储位置
    /*
     * 指定文件后缀过滤
     */
    private static final Pattern FILTERS = Pattern
            .compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    /*
     * 正则匹配图片文件
     */
    private static final Pattern IMG_PATTERNS = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");

    private static  File storageFolder; // 爬取的图片本地存储地址

    /**
     * 配置方法 指定域名和本地存储文件
     *
     * @param domain
     * @param storageFolderName
     */
    public static void configure(List<String> domain, String storageFolderName) {
        storageFolder = new File(storageFolderName); // 实例化
        if (!storageFolder.exists()) { // 假如文件不存在
            storageFolder.mkdirs(); // 我们创建一个
        }
    }

    /**
     * 这个方法主要是决定哪些url我们需要抓取，返回true表示是我们需要的，返回false表示不是我们需要的Url
     * 第一个参数referringPage封装了当前爬取的页面信息
     * 第二个参数url封装了当前爬取的页面url信息
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase(); // 得到小写的url
        if (FILTERS.matcher(href).matches()) { // 过滤指定后缀url
            return false;
        }

        if (IMG_PATTERNS.matcher(href).matches()) { // 匹配指定图片后缀文件
            return true;
        }

        for (String domain : URL_PATTERNS) { // 匹配指定域名
            if (href.startsWith(domain)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当我们爬到我们需要的页面，这个方法会被调用，我们可以尽情的处理这个页面
     * page参数封装了所有页面信息
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL(); // 获取url

        // 只爬取大于等于10kB的图片文件
        try {
            if (IMG_PATTERNS.matcher(url).matches() && (page.getParseData() instanceof BinaryParseData) && (page.getContentData().length > (10 * 1024))) {
                // 获取图片后缀
                String hashedName = url.substring(url.lastIndexOf("/") + 1);
                String filename = storageFolder.getAbsolutePath() + "/" + hashedName;
                Files.write(page.getContentData(), new File(filename)); // 把爬取到的文件存储到指定文件
                System.out.println("爬取图片的url:" + url);
            } else if (page.getParseData() instanceof HtmlParseData) {
                //捕获页面
                parseHtml(url);
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析图片
     * 使用htmlUtil 捕获动态渲染的页面
     *
     * @param url
     */
    private void parseHtml(String url) {
        Document doc = null;
        List<String> fullImages = new ArrayList<>();
        List<String> baseImages = new ArrayList<>();
        List<String> images = new ArrayList<>();
        try {
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            //是否使用不安全的SSL
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setJavaScriptEnabled(true);              // 启用JS解释器，默认为true
            webClient.getOptions().setCssEnabled(false);                    // 禁用css支持
            webClient.getOptions().setThrowExceptionOnScriptError(false);   // js运行错误时，是否抛出异常
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            //设置Ajax异步处理控制器即启用Ajax支持
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setTimeout(10 * 1000);                   // 设置连接超时时间
            HtmlPage page = webClient.getPage(url);
            webClient.waitForBackgroundJavaScript(30 * 1000);
            String pageAsXml = page.asXml();
            doc = Jsoup.parse(pageAsXml, url);
            Elements elements = doc.body().select("img[src]");
            if (!elements.isEmpty()) {
                for (Element element : elements) {
                    Attributes node = element.attributes();
                    Iterator<Attribute> iterator = node.iterator();
                    while (iterator.hasNext()) {
                        Attribute attribute = iterator.next();
                        String key = attribute.getKey();
                        //属性中包含“src”字符串，但不是src的属性
                        if (key.equals("src")) {
                            String imageSrc = attribute.getValue();
                            if (imageSrc.startsWith("http")) {
                                //不在本地址库
                                fullImages.add(imageSrc);
                            } else if (imageSrc.startsWith("data:image")) {
                                //base64
                                baseImages.add(imageSrc);
                            } else {
                                //本库的
                                images.add(imageSrc);
                            }
                            System.out.println("====通过解析页面爬取图片的url:====" + imageSrc);
                            break;
                        }

                    }
                }
            }
            //处理图片地址
            if (!fullImages.isEmpty()) {
                for (String imageUrl : fullImages) {
                    // 构造URL
                    URL urlV = new URL(imageUrl);
                    // 打开URL连接
                    URLConnection con = urlV.openConnection();
                    HttpURLConnection httpCon = (HttpURLConnection) con;
                    //加入User-Agent 防止403
                    httpCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36");
                    // 得到URL的输入流
                    InputStream input = httpCon.getInputStream();
                    // 设置数据缓冲
                    byte[] bs = new byte[1024 * 2];
                    // 读取到的数据长度
                    int len;
                    // 输出的文件流保存图片至本地
                    String hashedName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                    // 定义存储文件
                    String filename = storageFolder.getAbsolutePath() + "/" + hashedName;
                    OutputStream os = new FileOutputStream(new File(filename));
                    while ((len = input.read(bs)) != -1) {
                        os.write(bs, 0, len);
                    }
                    os.close();
                    input.close();
                }
            }
            if (!baseImages.isEmpty()) {

            }
            if (!images.isEmpty()) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行爬虫
     */
    public void execute() throws  Exception{
        // 实例化爬虫配置
        CrawlConfig config = new CrawlConfig();

        // 设置爬虫文件存储位置
        config.setCrawlStorageFolder(ROOT_FOLDER);

        //设置允许爬取二进制文件
        config.setIncludeBinaryContentInCrawling(true);

        //添加爬虫地址
        String pattern = "https://www.58pic.com/tupian/meitu.html";
        includePathPatterns(pattern);
        // 实例化页面获取器
        PageFetcher pageFetcher = new PageFetcher(config);
        // 实例化爬虫机器人配置 比如可以设置 user-agent
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        // 实例化爬虫机器人对目标服务器的配置，每个网站都有一个robots.txt文件 规定了该网站哪些页面可以爬，哪些页面禁止爬，该类是对robots.txt规范的实现
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        // 实例化爬虫控制器
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        //配置爬虫种子页面，就是规定的从哪里开始爬，可以配置多个种子页面
        for (String domain : URL_PATTERNS) {
            controller.addSeed(domain);
        }
        // 配置爬虫域名，以及本地存储位置
        ImagePageProcessor.configure(URL_PATTERNS, STORAGE_FOLDER);
        //启动爬虫，爬虫从此刻开始执行爬虫任务，根据以上配置
        controller.start(ImagePageProcessor.class, THREAD_CRAWLERS);
    }

    /**
     * 添加无需过滤的请求
     * @param pattern
     */
    private static void includePathPatterns(String pattern){
        String[] patterns = null;
        if(StringUtils.isNotBlank(pattern)){
            patterns = pattern.split(",");
        }
        URL_PATTERNS.addAll(Arrays.asList(patterns));
    }
}
