/*
 *
 * * Copyright 2008-2016 the original author or authors.
 * *
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * * you may not use this file except in compliance with the License.
 * * You may obtain a copy of the License at
 * *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * *
 * * Unless required by applicable law or agreed to in writing, software
 * * distributed under the License is distributed on an "AS IS" BASIS,
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 *
 */
package com.clougence.utils.setting.provider.xml;
import java.io.IOException;
import java.io.Reader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import com.clougence.utils.setting.Settings;
import com.clougence.utils.setting.provider.SettingsReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author 赵永春 (zyc@byshell.org)
 * @version : 2021-02-01
 */
public class XmlSettingsReader implements SettingsReader {
    @Override
    public void readSetting(Reader reader, Settings readTo) throws IOException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
            factory.setFeature("http://xml.org/sax/features/namespaces", true);
            SAXParser parser = factory.newSAXParser();
            SaxXmlParser handler = new SaxXmlParser(readTo);
            InputSource inputSource = new InputSource(reader);
            parser.parse(inputSource, handler);
        } catch (SAXException | ParserConfigurationException e) {
            throw new IOException("parsing failed -> " + e.getMessage(), e);
        }
    }
}
