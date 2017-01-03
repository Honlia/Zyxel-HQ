/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.superfw.framework.transaction.util;

import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 事务管理工具类。
 * <h4>【概要】</h4>
 * 没有发生异常的情况下强制让事务回滚。<br>
 * 使用setRollbackOnly方法把isRollbackOnly状态变成true。
 * <p>
 */
public class TransactionUtil {

    /**
     * 执行回滚。
     *
     * <p>
     * 在业务处理的事务处理中，没有发生的例外的情况下，在分支判断中实现回滚。
     * </p>
     */
    public static void setRollbackOnly() {

        // RollbackOnly设定为true。
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

    }
}
