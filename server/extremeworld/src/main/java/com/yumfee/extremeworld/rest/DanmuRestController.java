package com.yumfee.extremeworld.rest;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.Danmu;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.entity.Video;
import com.yumfee.extremeworld.rest.dto.DanmuDTO;
import com.yumfee.extremeworld.service.DanmuService;


@RestController
@RequestMapping(value = "/api/v1/danmu")
public class DanmuRestController {

	private static Logger logger = LoggerFactory.getLogger(DanmuRestController.class);

	@Autowired
	DanmuService danmuService;
	
	@RequestMapping(value = "/{videoId}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public List<String> list(@PathVariable("videoId") Long videoId)
	{
		List<String> result = new ArrayList<String>();
		
		List<Danmu> danmuList =  danmuService.getAllDanmuByVideoId(videoId);
		List<DanmuDTO> danmuDTOList = BeanMapper.mapList(danmuList, DanmuDTO.class);
		for(DanmuDTO danmuDTO : danmuDTOList)
		{
			ObjectMapper mapper = new ObjectMapper();
			
			try {
				
				result.add(mapper.writeValueAsString(danmuDTO));
				
				
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return result;
	}
	
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
	public String create(String danmu,HttpServletRequest  request ,HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException{
	
		String videoIdString = request.getParameter("videoId");
		String userIdString = request.getParameter("userId");
		String danmuString = new String(danmu.getBytes("UTF-8"));

		System.out.println("videoId=" + videoIdString);
		System.out.println("userId=" + userIdString);
		System.out.println("danmu=" + danmuString);
		
		User userInfo = new User();
		userInfo.setId(Long.valueOf(userIdString));
		
		Video video = new Video();
		video.setId(Long.valueOf(videoIdString));
		
		ObjectMapper mapper = new ObjectMapper();
		DanmuDTO danmuDTO = mapper.readValue(danmuString, DanmuDTO.class);
		
		Danmu danmuEntity = new Danmu();
		danmuEntity.setText(danmuDTO.getText());
		danmuEntity.setColor(danmuDTO.getColor());
		danmuEntity.setPosition(danmuDTO.getPosition());
		danmuEntity.setSize(danmuDTO.getSize());
		danmuEntity.setTime(danmuDTO.getTime());
		danmuEntity.setUser(userInfo);
		danmuEntity.setVideo(video);
		
		danmuService.SaveDanmu(danmuEntity);
		
		
		return "true";
	}
	
/*	@RequestMapping(value = "/{videoId}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public List<DanmuDTO> list(@PathVariable("videoId") Long videoId)
	{
		
		List<Danmu> danmuList =  danmuService.getAllDanmuByVideoId(videoId);
		return BeanMapper.mapList(danmuList, DanmuDTO.class);
	}*/
	
}
