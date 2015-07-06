package com.jixianxueyuan.util;

import com.jixianxueyuan.server.StaticResourceConfig;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

/**
 * Created by pengchao on 5/23/15.
 */
public class AnalyzeContent {

    public static LinkedList<ContentFragment> analyzeContent(String content)
    {
        MyLog.d("TopicDetailActivity","enter analyzeContent()");
        MyLog.d("TopicDetailActivity", "string=" + content);

        //String regex = "<img .*?>";
        String regex="(<img)|(/>)";
        String regexPIC_URL="http://.*?(jpg|png|jpeg|gif)";

        String[] replyted = content.split(regex);

        LinkedList<ContentFragment> result = new LinkedList<ContentFragment>();
        for(int n = 0; n != replyted.length; n++)
        {
            replyted[n] = replyted[n].replaceFirst("\\s*?src=", "");
            replyted[n] = replyted[n].replace("\"", "");
            MyLog.d("TopicDetailActivity", replyted[n]);

            ContentFragment temp = new ContentFragment();
            temp.mText = replyted[n];

            if( replyted[n].matches(regexPIC_URL) )
            {

                temp.mType = ContentFragment.IMG_URL_TYPE;
                MyLog.d("TopicDetailActivity", "is img");

            }
            else
            {
                temp.mType = ContentFragment.TEXT_TYPE;
                MyLog.d("TopicDetailActivity", "is text");
            }
            result.add(temp);
        }

        return result;
    }

    public static LinkedList<ContentFragment> analyzeContent2(String content)
    {
        LinkedList<ContentFragment> result = new LinkedList<ContentFragment>();


        Document doc = Jsoup.parse(content);

        List<Element> elementList = doc.getAllElements();
        for(Element element : elementList)
        {

            if(element.tagName().equals("p"))
            {
                ContentFragment fragment = new ContentFragment();
                fragment.mType = ContentFragment.TEXT_TYPE;
                fragment.mText = element.text();
                result.add(fragment);
            }
            else if(element.tagName().equals("img"))
            {
                ContentFragment fragment = new ContentFragment();
                fragment.mType = ContentFragment.IMG_URL_TYPE;
                fragment.mText = element.attr("src");
                result.add(fragment);
            }

        }

        return result;
    }

    public static class ContentFragment
    {
        static public final int TEXT_TYPE = 0;
        static public final int IMG_URL_TYPE = 1;

        public String mText;
        public int mType;

    }
}
