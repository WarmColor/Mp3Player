package net.warmcolor.blog.mp3player;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import net.warmcolor.blog.download.HttpDownloader;
import net.warmcolor.blog.model.Mp3Info;
import net.warmcolor.blog.xml.Mp3ListContentHandler;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

public class Mp3ListActivity extends ListActivity {

    private static final int UPDATE = 1;
    private static final int ABOUT = 2;
    private List<Mp3Info> mp3Infos = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3_list);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        updateListView();
    }

    // 在用户点击MENU按钮之后，会调用该方法，我们可以在这个方法当中加入自己的按钮控件

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, UPDATE, 1, R.string.mp3list_update);
        menu.add(0, ABOUT, 2, R.string.mp3list_about);
        return super.onCreateOptionsMenu(menu);
    }

    public String url = "http://192.168.168.6:8080/mp3/resources.xml";

    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("ItemId------>" + item.getItemId());
        if (item.getItemId() == UPDATE) {
            // 用户点击了更新列表按钮
            String xml = downloadXML(url);

        } else if (item.getItemId() == ABOUT) {
            //用户点击了关于按钮
        }
        return super.onOptionsItemSelected(item);
    }

    private  void updateListView(){
        // 用户点击了更新列表按钮
        // 下载包含所有Mp3基本信息的xml文件
        String xml=downloadXML(url);
        // 对xml文件进行解析，并将解析的结果放置到Mp3Info对象当中
        // 最后将这些Mp3Info对象放置到List当中
        List<Mp3Info> mp3Infos = parse(xml);

    }
    private String downloadXML(String urlStr) {
        HttpDownloader httpDownloader = new HttpDownloader();
        String result = httpDownloader.download(urlStr);
        return result;
    }

    private List<Mp3Info> parse(String xmlStr) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        List<Mp3Info> infos = null;
        try {
            XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
            infos = new ArrayList<Mp3Info>();
            Mp3ListContentHandler mp3ListContentHandler = new Mp3ListContentHandler(infos);
            xmlReader.setContentHandler(mp3ListContentHandler);
            xmlReader.parse(new InputSource(new StringReader(xmlStr)));
            for (Iterator iterator = infos.iterator(); iterator.hasNext(); ) {
                Mp3Info mp3Info = (Mp3Info) iterator.next();
                System.out.println(mp3Info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return infos;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent();
        startService(intent);
        super.onListItemClick(l, v, position, id);
    }
}
