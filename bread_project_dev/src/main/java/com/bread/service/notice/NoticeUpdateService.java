package com.bread.service.notice;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bread.app.dao.NoticeDAO;
import com.bread.app.vo.NoticeVO;

import lombok.AllArgsConstructor;

@Service("nUpdate")
@AllArgsConstructor
public class NoticeUpdateService implements NoticeService {

	private NoticeDAO dao;
	
	@Override
	public int update(NoticeVO vo, HttpServletRequest request) {
		int result = 0;
		
		MultipartFile uploadFile = vo.getUploadFile();
		
		if (uploadFile.getSize() != 0) {
			
			String originFileName = uploadFile.getOriginalFilename();
			String ext = originFileName.substring(originFileName.lastIndexOf("."));
			String now = new SimpleDateFormat("yyyyMMdd_HmsS").format(new Date());
			String saveFileName = now+ext;
			
			String saveDirectory = request.getServletContext().getRealPath("resources/uploads/");
			String fullPath = saveDirectory + saveFileName;
		
		
			try {
				uploadFile.transferTo(new File(fullPath));
			} catch (Exception e) {
				System.out.println("파일 저장 중 예외 발생");
				e.printStackTrace();
			}
			//3. 다른 폼의 전달값을 BoardVO에 저장하기
			vo.setOriginfile_name(originFileName);
			vo.setSavefile_name(saveFileName);
		}
	
		result = dao.update(vo);
		
		return result;
			
	}
}
