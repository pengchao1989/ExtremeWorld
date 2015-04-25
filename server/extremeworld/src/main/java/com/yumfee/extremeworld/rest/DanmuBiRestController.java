package com.yumfee.extremeworld.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.Danmu;
import com.yumfee.extremeworld.service.DanmuService;

//提供B站格式的弹幕xml
//<d p="0,1,25,16777215,1312863760,0,eff85771,42759017">前排占位置</d>
/*中几个逗号分割的数据
第一个参数是弹幕出现的时间 以秒数为单位。
第二个参数是弹幕的模式1..3 滚动弹幕 4底端弹幕 5顶端弹幕 6.逆向弹幕 7精准定位 8高级弹幕
第三个参数是字号， 12非常小,16特小,18小,25中,36大,45很大,64特别大
第四个参数是字体的颜色 以HTML颜色的十位数为准
第五个参数是Unix格式的时间戳。基准时间为 1970-1-1 08:00:00
第六个参数是弹幕池 0普通池 1字幕池 2特殊池 【目前特殊池为高级弹幕专用】
第七个参数是发送者的ID，用于“屏蔽此弹幕的发送者”功能
第八个参数是弹幕在弹幕数据库中rowID 用于“历史弹幕”功能。*/

@RestController
@RequestMapping(value = "/api/v1/danmubi")
public class DanmuBiRestController {
	
	
	private static Logger logger = LoggerFactory.getLogger(DanmuBiRestController.class);
	

	@Autowired
	DanmuService danmuService;
	
	@RequestMapping(value = "/{videoId}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public String get(@PathVariable("videoId") Long videoId)
	{
		String result = "";
		
		List<Danmu> danmuList =  danmuService.getAllDanmuByVideoId(videoId);
		
		for(Danmu danmu : danmuList)
		{
			//Integer color = Integer.valueOf(danmu.getColor(),16);
			String colorString = Integer.valueOf(danmu.getColor().replace("#", ""),16).toString();
			
			String singleDanmuStr = "<d p=\"" + danmu.getTime() / 10 + ",1,25," + colorString + ",1312863760,0,eff85771,42759017\">" +danmu.getText() + "</d>";
			
			result += singleDanmuStr;
		}
		
		return result;
	}

}
