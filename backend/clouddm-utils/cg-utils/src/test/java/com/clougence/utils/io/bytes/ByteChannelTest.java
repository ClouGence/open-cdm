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
package com.clougence.utils.io.bytes;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteChannelTest {
    @Test
    public void writeByteTest01() throws IOException {
        BytesIO io = BytesIO.ring(4);

        io.writeBytes(new byte[] { 20, 30 });
        ByteBuffer array = ByteBuffer.wrap(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 });
        int writeStep1 = io.write(array);
        assert writeStep1 == 2;
        io.flush();

        assert io.readByte() == 20;
        assert io.readByte() == 30;
        assert io.readByte() == 1;
        assert io.readByte() == 2;
        io.markReader();

        int writeStep2 = io.write(array);
        assert writeStep2 == 4;
        io.flush();

        assert io.readByte() == 3;
        assert io.readByte() == 4;
        assert io.readByte() == 5;
        assert io.readByte() == 6;
        io.markReader();

        int writeStep3 = io.write(array);
        assert writeStep3 == 2;
        io.flush();

        assert io.readByte() == 7;
        assert io.readByte() == 8;
        io.markReader();
    }

    @Test
    public void readerByteTest01() throws IOException {
        BytesIO io = BytesIO.auto(10);

        io.write(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 }));
        io.flush();

        ByteBuffer array1 = ByteBuffer.allocate(2);
        assert io.readBuffer(array1) == 2;
        assert array1.get(0) == 1;
        assert array1.get(1) == 2;

        ByteBuffer array2 = ByteBuffer.allocate(10);
        assert io.readBuffer(array2) == 6;
        assert array2.get(0) == 3;
        assert array2.get(1) == 4;
        assert array2.get(2) == 5;
        assert array2.get(3) == 6;
        assert array2.get(4) == 7;
        assert array2.get(5) == 8;
    }
}
