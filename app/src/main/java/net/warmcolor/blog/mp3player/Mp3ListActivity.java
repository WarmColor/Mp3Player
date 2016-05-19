package net.warmcolor.blog.mp3player;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import net.warmcolor.blog.download.HttpDownloader;
import net.warmcolor.blog.model.Mp3Info;
import net.warmcolor.blog.service.DownloadService;
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

    /**
     * 在用户点击MENU按钮之后，会调用该方法，我们可以在这个方法当中加入自己的按钮控件
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, UPDATE, 1, R.string.mp3list_update);
        menu.add(0, ABOUT, 2, R.string.mp3list_about);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Called when the activity is first created.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3_list);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        updateListView();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("ItemId------>" + item.getItemId());
        if (item.getItemId() == UPDATE) {
            // 用户点击了更新列表按钮
            updateListView();
        } else if (item.getItemId() == ABOUT) {
            //用户点击了关于按钮
        }
        return super.onOptionsItemSelected(item);
    }

    private SimpleAdapter buildSimpleAdapter(List<Mp3Info> mp3Infos) {

    }

    private void updateListView() {
        // 用户点击了更新列表按钮
        // 下载包含所有Mp3基本信息的xml文件
        String xml = downloadXML("http://192.168.168.6:8080/mp3/resources.xml");
        // 对xml文件进行解析，并将解析的结果放置到Mp3Info对象当中
        // 最后将这些Mp3Info对象放置到List当中
        mp3Infos = parse(xml);
        SimpleAdapter simpleAdapter = buildSimpleAdapter(mp3Infos);
        setListAdapter(simpleAdapter);
    }

    private String downloadXML(String urlStr) {
        HttpDownloader httpDownloader = new HttpDownloader();
        String result = httpDownloader.download(urlStr);
        return result;
    }

    private List<Mp3Info> parse(String xmlStr) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        List<Mp3Info> infos = new ArrayList<Mp3Info>();
        try {
            XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
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

    protected void onListItemClick(ListView l, View v, int position, long id) {
        // 根据用户点击列表当中的位置来得到相应的Mp3Info对象
        Mp3Info mp3Info = mp3Infos.get(position);
        // System.out.println("mp3info--->" + mp3Info);
        // 生成Intent对象
        Intent intent = new Intent();
        // 将Mp3Info对象存入到Intent对象当中
        intent.putExtra("mp3Info", mp3Info);
        intent.setClass(this, DownloadService.class);
        // 启动service
        startService(intent);
        super.onListItemClick(l, v, position, id);
    }
}
