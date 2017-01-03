package cn.superfw.framework.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public class BeanUtil extends BeanUtils {

	private static final Logger log = LoggerFactory.getLogger(BeanUtils.class);

	public static void copyPropertiesNotNull(Object source, Object target) {

		List<String> notCopyPdsList = new ArrayList<String>();

		PropertyDescriptor[] sourcePds = getPropertyDescriptors(source.getClass());
		for (PropertyDescriptor sourcePd : sourcePds) {
			Method readMethod = sourcePd.getReadMethod();
			try {
				if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
					readMethod.setAccessible(true);
				}
				Object value = readMethod.invoke(source);

				if (value == null) {
					String methodName = readMethod.getName();
					String notCopyPd = StringUtil.uncapitalize(methodName.replace("get", ""));
					notCopyPdsList.add(notCopyPd);
				}
			} catch (Throwable ex) {
				log.error("copyPropertiesNotNull error!", ex);
			}
		}

		copyProperties(source, target, notCopyPdsList.toArray(new String[]{}));
	}
}
