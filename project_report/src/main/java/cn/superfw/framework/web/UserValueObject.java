/*
 * Copyright (c) 2007 NTT DATA Corporation
 *
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

package cn.superfw.framework.web;


import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.superfw.application.config.SystemConfig;
import cn.superfw.framework.CommonContants;
import cn.superfw.framework.exception.ClassLoadException;
import cn.superfw.framework.exception.SystemException;
import cn.superfw.framework.utils.ClassUtil;


public abstract class UserValueObject implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5599633655303505172L;

	private static final Logger log = LoggerFactory.getLogger(UserValueObject.class);



	private String access_token;
	private String expires_in;
	private String refesh_in;
	private String openId;
	private String scope;

	private String username;
	private String password;



    public String getAccess_token() {
		return access_token;
	}



	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}



	public String getExpires_in() {
		return expires_in;
	}



	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}



	public String getRefesh_in() {
		return refesh_in;
	}



	public void setRefesh_in(String refesh_in) {
		this.refesh_in = refesh_in;
	}



	public String getOpenId() {
		return openId;
	}



	public void setOpenId(String openId) {
		this.openId = openId;
	}



	public String getScope() {
		return scope;
	}



	public void setScope(String scope) {
		this.scope = scope;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public static UserValueObject createUserValueObject() {

        UserValueObject userValueObject = null;
        String className = SystemConfig.USER_VALUE_OBJECT.value();
        if (className != null) {
            try {
                userValueObject = (UserValueObject) ClassUtil.create(className);
            } catch (ClassLoadException e) {
                log.error("illegal uvo class:" + className, e);
                throw new SystemException(e);
            } catch (ClassCastException e) {
                log.error("illegal uvo class:" + className, e);
                throw new SystemException(e);
            }
        } else {
            log.error("specify " + CommonContants.UVO + ".");
            return null;
        }

        return userValueObject;
    }

}
