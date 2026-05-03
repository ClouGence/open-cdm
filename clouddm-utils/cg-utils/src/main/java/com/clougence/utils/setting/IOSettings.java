/*
 * Copyright 2008-2009 the original author or authors.
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
package com.clougence.utils.setting;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import com.clougence.utils.setting.provider.StreamType;

/**
 * 需要经过解析读取配置文件资源的{@link  Settings}
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2013-9-9
 */
public interface IOSettings extends Settings {
    void loadResource(String mainSettings, StreamType type) throws IOException;

    void loadStream(InputStream mainSettings, StreamType type) throws IOException;

    void loadStream(InputStream mainSettings, Charset charset, StreamType type) throws IOException;

    void loadStringBody(String mainSettings, StreamType type) throws IOException;

    void loadReader(Reader mainSettings, StreamType type) throws IOException;
}
