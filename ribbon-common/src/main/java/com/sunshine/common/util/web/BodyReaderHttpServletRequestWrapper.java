package com.sunshine.common.util.web;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/20 23:17
 **/
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static final String UTF_8 = "UTF-8";
    private Map<String, String[]> paramsMap;


    private final byte[] body; // 报文


    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = readBytes(request.getInputStream());


        // 首先从POST中获取数据
        if ("POST".equals(request.getMethod().toUpperCase())) {
            paramsMap = getParamMapFromPost(this);
        } else {
            paramsMap = getParamMapFromGet(this);
        }


    }


    @Override
    public Map<String, String[]> getParameterMap() {
        return paramsMap;
    }


    @Override
    public String getParameter(String name) {// 重写getParameter，代表参数从当前类中的map获取
        String[] values = paramsMap.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }


    @Override
    public String[] getParameterValues(String name) {// 同上
        return paramsMap.get(name);
    }


    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(paramsMap.keySet());
    }


    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {


            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }


    private Map<String, String[]> getParamMapFromGet(HttpServletRequest request) {
        return parseQueryString(request.getQueryString());
    }


    private HashMap<String, String[]> getParamMapFromPost(HttpServletRequest request) {


        String body = "";
        try {
            body = getRequestBody(request.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String, String[]> result = new HashMap<String, String[]>();


        if (null == body || 0 == body.length()) {
            return result;
        }


        return parseQueryString(body);
    }


    private String getRequestBody(InputStream stream) throws IOException {
        String line = "";
        StringBuilder body = new StringBuilder();
        int counter = 0;


        // 读取POST提交的数据内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        while ((line = reader.readLine()) != null) {
            if (counter > 0) {
                body.append("rn");
            }
            body.append(line);
            counter++;
        }
        reader.close();
        return body.toString();
    }


    public HashMap<String, String[]> parseQueryString(String s) {
        String valArray[] = null;
        if (s == null) {
            throw new IllegalArgumentException();
        }
        HashMap<String, String[]> ht = new HashMap<String, String[]>();
        StringTokenizer st = new StringTokenizer(s, "&");
        while (st.hasMoreTokens()) {
            String pair = (String) st.nextToken();
            int pos = pair.indexOf('=');
            if (pos == -1) {
                continue;
            }
            String key = pair.substring(0, pos);
            String val = pair.substring(pos + 1, pair.length());
            if (ht.containsKey(key)) {
                String oldVals[] = (String[]) ht.get(key);
                valArray = new String[oldVals.length + 1];
                for (int i = 0; i < oldVals.length; i++) {
                    valArray[i] = oldVals[i];
                }
                valArray[oldVals.length] = decodeValue(val);
            } else {
                valArray = new String[1];
                valArray[0] = decodeValue(val);
            }
            ht.put(key, valArray);
        }
        return ht;
    }


    private static byte[] readBytes(InputStream in) throws IOException {
        BufferedInputStream bufin = new BufferedInputStream(in);
        final int buffSize = 1024;
        ByteArrayOutputStream out = new ByteArrayOutputStream(buffSize);


        byte[] temp = new byte[buffSize];
        int size = 0;
        while ((size = bufin.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        out.flush();
        byte[] content = out.toByteArray();
        bufin.close();
        out.close();
        return content;
    }


  /*  *//**
     * 自定义解码函数
     *
     * @param value
     * @return
     */
    private String decodeValue(String value) {
        return value;
        /*if (value.contains("%u")) {
            return Encodes.urlDecode(value);
        } else {
            try {
                return URLDecoder.decode(value, UTF_8);
            } catch (UnsupportedEncodingException e) {
                return "";// 非UTF-8编码
            }
        }*/
    }
}
