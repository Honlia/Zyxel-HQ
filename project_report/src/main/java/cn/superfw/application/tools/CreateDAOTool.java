package cn.superfw.application.tools;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class CreateDAOTool {

	private static final String templeate = "" +
			"package cn.superfw.application.dao;"+
			"\r\n\r\n"+
			"import org.springframework.stereotype.Repository;"+
			"\r\n\r\n"+
			"import cn.superfw.application.domain.${DOMAIN};"+
			"import cn.superfw.framework.orm.hibernate.HibernateDao;"+
			"\r\n\r\n"+
			"@Repository"+
			"\r\n"+
			"public class ${DOMAIN}DAO extends HibernateDao<${DOMAIN}, Long> {"+
			"\r\n\r\n"+
			"}"+
			"\r\n\r\n";


	public static void main(String[] args) throws Exception {

		String userDir = System.getProperty("user.dir");

		File inputDir = new File(userDir, "src\\main\\java\\cn\\superfw\\application\\domain");

		String outputPath = userDir + "\\src\\main\\java\\cn\\superfw\\application\\dao";

		String[] fileNames = inputDir.list();
		for(String fileName : fileNames) {
			if (fileName.endsWith(".java")) {
				fileName = fileName.replace(".java", "");

				if ("BaseEntity".equals(fileName)) {
				    continue;
				}

				String classStr = templeate.replace("${DOMAIN}", fileName);

				FileUtils.writeStringToFile(new File(outputPath, fileName.concat("DAO.java")), classStr);

				System.out.println(classStr);
			}

		}




	}

}
