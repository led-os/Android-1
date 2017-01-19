/**
* Copyright (c) 2014 All rights reserved
* 名称：ResourceInject.java
* 描述：TODO
* @author wzgao
* @date：2014-3-27 下午4:23:11
* @version v1.0
*/
package com.iflytek.android.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wzgao
 *
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceInject {

	int id() default -1;
}
