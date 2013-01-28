package fr.voltanite.synchrotags;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

public class HTTPPoster {
    public static HttpResponse doPost(String url, JSONObject c) throws ClientProtocolException, IOException 
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);
        HttpEntity entity;
        StringEntity s = new StringEntity(c.toString());
        s.setContentEncoding(new BasicHeader("UTF_8", "application/json"));
        entity = s;
        request.setEntity(entity);
        HttpResponse response;
        response = httpclient.execute(request);
        return response;
    }
}
