/*
 *  Copyright 2014-2015 snakerflow.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package cn.superfw.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author superch
 * @since 0.1
 */
public class DateUtil {
    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";


    public static Date getCurrentDate() {
        Date date = new Date();
        return date;
    }

    /**
     * 返回指定的日期格式。
     *
     * @param target 日期
     * @param pattern 格式
     *
     * @return 变换后的日期
     */
    public static String formatDate(Date target, String pattern) {

        if (target == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        return formatter.format(target);
    }

}
