/*
    WEB-DICOM - preserving and providing information to the DICOM devices
	
    Copyright (C) 2009-2010 psystems.org
    Copyright (C) 2009-2010 Dmitry Derenok 

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
    
    The Original Code is part of WEB-DICOM, an implementation hosted at 
    <http://code.google.com/p/web-dicom/>
    
    In the project WEB-DICOM used the library open source project dcm4che
    The Original Code is part of dcm4che, an implementation of DICOM(TM) in
    Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
    
    =======================================================================
    
    WEB-DICOM - Сохранение и предоставление информации с DICOM устройств

    Copyright (C) 2009-2010 psystems.org 
    Copyright (C) 2009-2010 Dmitry Derenok 

    Это программа является свободным программным обеспечением. Вы можете 
    распространять и/или модифицировать её согласно условиям Стандартной 
    Общественной Лицензии GNU, опубликованной Фондом Свободного Программного 
    Обеспечения, версии 3 или, по Вашему желанию, любой более поздней версии. 
    Эта программа распространяется в надежде, что она будет полезной, но
    БЕЗ ВСЯКИХ ГАРАНТИЙ, в том числе подразумеваемых гарантий ТОВАРНОГО СОСТОЯНИЯ ПРИ 
    ПРОДАЖЕ и ГОДНОСТИ ДЛЯ ОПРЕДЕЛЁННОГО ПРИМЕНЕНИЯ. Смотрите Стандартную 
    Общественную Лицензию GNU для получения дополнительной информации. 
    Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе 
    с программой. В случае её отсутствия, посмотрите <http://www.gnu.org/licenses/>
    Русский перевод <http://code.google.com/p/gpl3rus/wiki/LatestRelease>
    
    Оригинальный исходный код WEB-DICOM можно получить на
    <http://code.google.com/p/web-dicom/>
    
    В проекте WEB-DICOM использованы библиотеки открытого проекта dcm4che/
    Оригинальный исходный код проекта dcm4che, и его имплементация DICOM(TM) in
    Java(TM), находится здесь http://sourceforge.net/projects/dcm4che.
    
    
 */
package org.psystems.webdicom2.ws;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AttachementServlet extends HttpServlet {

	private static final long serialVersionUID = 6818437843452375781L;
	private static Logger logger = Logger.getLogger(AttachementServlet.class
			.getName());

	static {
		// PropertyConfigurator.configure("WEB-INF/log4j.properties");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String path = req.getPathInfo().replaceFirst("/", "");
		String fileName = null;
		resp.setCharacterEncoding("utf-8");
		
//		System.out.println("!!! path="+path);

		String id = null;
		String type = null;
		
		if(path.endsWith(".pdf")) {
			id = path.replaceAll(".pdf", "");
			type = "pdf";
		} else if(path.endsWith(".jpg")) {
			id = path.replaceAll(".jpg", "");
			type="jpg";
		}
		
		String filePath = null;
		
//		System.out.println("!!!! id="+id+" type="+type);

		File rootDir = new File(Gate.testDrnDataDir);

		File[] drnDirs = rootDir.listFiles();
		for (File drnDir : drnDirs) {
			if(drnDir.isDirectory()) {
				File[] studyDirs = drnDir.listFiles();
				
				for(File studyDir : studyDirs) {
					if(studyDir.isDirectory() && studyDir.getName().equals(id)) {
						filePath = Gate.testDrnDataDir + File.separator + drnDir.getName() + File.separator + id + File.separator;
						if(type.equals("pdf")) {
							resp.setContentType("application/pdf");
							filePath+="data.pdf";
						}
						else if(type.equals("jpg")) {
							filePath+="data.jpg";
							resp.setContentType("image/jpeg");
						}
						break;
					}
				}
			}
		}
		
//		System.out.println("!!! filePath="+filePath);
		
		
		FileInputStream in = null;
		try {
			in = new FileInputStream(filePath);
		} catch (FileNotFoundException ex) {
			resp.setCharacterEncoding("utf-8");// FIXME Не работает!!!
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found! "
					+ fileName);
			ex.printStackTrace();
			return;
		}
		BufferedOutputStream out = new BufferedOutputStream(resp
				.getOutputStream());

		byte b[] = new byte[8];
		int count;
		while ((count = in.read(b)) != -1) {
			out.write(b, 0, count);

		}
		out.flush();
		out.close();
		in.close();
	}

}
