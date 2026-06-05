/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.comm.model;

/**
 * @author bucketli 2021/1/18 13:05
 */
public class RSocketAuthException extends RuntimeException {

    public RSocketAuthException(){
    }

    public RSocketAuthException(String message){
        super(message);
    }

    public RSocketAuthException(String message, Throwable cause){
        super(message, cause);
    }

    public RSocketAuthException(Throwable cause){
        super(cause);
    }

    public RSocketAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
