package com.example;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;

@Controller
@Scope("prototype")
public class LibSearch_ctrl {

  public String post_libsearch(HttpServletRequest request) throws Exception {
    
    System.setProperty("javax.net.ssl.trustStore", "jssecacerts.cert");
    //選択した都道府県セット
    String input_address = request.getParameter("example");
    input_Sample.setInputAddress(input_address);
    //Modelへ
    request.setAttribute("input_info", input_Sample);
    request.setAttribute("option", MainConstants.address);

    //リクエスト start
    String encodedResult = URLEncoder.encode(input_address, "UTF-8");
    URL url = new URL("https://api.calil.jp/library?appkey=eff2329beb9938a9b6443b5795ff2db1&pref="+ encodedResult);
    HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
    urlConn.setRequestMethod("GET");
    urlConn.connect();
    //Map headerFields = urlConn.getHeaderFields();
    //リクエスト end

    int rspCode = urlConn.getResponseCode();
    if (rspCode == 200) {
        InputStream in = urlConn.getInputStream();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(in);
        in.close();
    
        //3. 解析して中身をとりだします。
        Element bookList = doc.getDocumentElement();
        NodeList nodes = bookList.getElementsByTagName("Library");
        ArrayList<Map<String, String>> liblist = new ArrayList<Map<String, String>>();
        for(int i=0; i<nodes.getLength();i++) {
          Map<String, String> infomap = new HashMap<>();
          Node personNode = nodes.item(i);
          NodeList chnodes = nodes.item(i).getChildNodes();
          for(int j=0; j<chnodes.getLength();j++){
            String chname = chnodes.item(j).getNodeName();
            if(chname == "formal" || chname == "url_pc" || chname == "post" || chname == "address" || chname == "tel" || chname == "category"){
              infomap.put(chname,chnodes.item(j).getTextContent());
            }
          }
          liblist.add(infomap);
        }
        request.setAttribute("libmap",liblist);
      }
      return "article";
    }
  }
}