package com.clougence.rdp.service.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.clougence.rdp.constant.auth.CanBeReplaced;
import com.clougence.rdp.dal.mapper.RdpUserMapper;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.utils.ExceptionUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/3/4
 **/
@Deprecated
@Component
@Slf4j
public class RdpMapperProxy<T> {

    @Resource
    private RdpUserMapper rdpUserMapper;

    private String getPrimaryUid(String uid) {
        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);
        if (userDO == null) {
            throw new IllegalArgumentException("uid not exist.");
        }

        if (userDO.getParentId() == null || userDO.getParentId() <= 0) {
            return uid;
        } else {
            RdpUserDO parentUserDO = rdpUserMapper.queryById(userDO.getParentId());
            return parentUserDO.getUid();
        }
    }

    @SuppressWarnings("unchecked")
    public T getReplaceUidMapper(T mapper) {
        Class<T>[] interfaceClz = (Class<T>[]) mapper.getClass().getInterfaces();
        Object result = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaceClz, (proxy, method, args) -> {
            try {
                if (args.length == 1 && (method.getName().equals("insert") || method.getName().equals("update"))) {
                    processParentMethod(args);
                    return method.invoke(mapper, args);
                }

                Annotation[][] annotations = method.getParameterAnnotations();
                int haveAnnotationIndex = -1;
                int index = 0;
                for (Annotation[] annotation1 : annotations) {
                    for (Annotation annotation : annotation1) {
                        if (annotation instanceof CanBeReplaced) {
                            haveAnnotationIndex = index;
                            break;
                        }
                    }
                    if (haveAnnotationIndex != -1) {
                        break;
                    }
                    index++;
                }
                if (haveAnnotationIndex == -1) {
                    return method.invoke(mapper, args);
                } else {
                    String rawUid = String.valueOf(args[haveAnnotationIndex]);
                    String primaryUid = getPrimaryUid(rawUid);
                    args[haveAnnotationIndex] = primaryUid;
                    return method.invoke(mapper, args);
                }

            } catch (Exception e) {
                String errMsg = "Replace uid in proxy failed with exception. Root cause is " + ExceptionUtils.getRootCauseMessage(e);
                log.error(errMsg, e);
                throw e;
            }
        });
        return (T) result;
    }

    /**
     * If user invoke parent mapper method like mapper.insert() and mapper.update(), uid are in DO's field and also need to be replaced
     */
    @SneakyThrows
    private void processParentMethod(Object[] args) {
        Object paramObj = args[0];
        Field[] fields = paramObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            Annotation[] fieldAnnotations = field.getAnnotations();
            for (Annotation annotation : fieldAnnotations) {
                if (annotation.annotationType().equals(CanBeReplaced.class)) {
                    field.setAccessible(true);
                    String rawUid = String.valueOf(field.get(paramObj));
                    String primaryUid = getPrimaryUid(rawUid);
                    field.set(paramObj, primaryUid);
                    break;
                }
            }
        }
    }
}
